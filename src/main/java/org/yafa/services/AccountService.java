package org.yafa.services;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import com.google.common.collect.Lists;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.yafa.api.dto.Asset;
import org.yafa.api.dto.inbound.Account;
import org.yafa.api.dto.inbound.Trade;
import org.yafa.api.dto.outbound.Holding;
import org.yafa.api.dto.outbound.Order;
import org.yafa.state.StateStore;

@Slf4j
@Dependent
public class AccountService {

  @Inject StateStore stateStore;

  public org.yafa.api.dto.outbound.Account create(@Valid Account account) {
    return stateStore.createAccount(account);
  }

  public Collection<org.yafa.api.dto.outbound.Account> getAccounts() {
    return stateStore.getAccounts();
  }

  public org.yafa.api.dto.outbound.Account getAccount(String accountId) {
    return stateStore.getAccount(accountId);
  }

  public Collection<Trade> listTrades(org.yafa.api.dto.outbound.Account account) {
    return stateStore.getTrades(account);
  }

  private Collection<Trade> resolveOrder(
      org.yafa.api.dto.outbound.Account account, org.yafa.api.dto.inbound.Order order) {
    return Lists.newLinkedList();
  }

  public Collection<Holding> listHoldings(
      org.yafa.api.dto.outbound.Account account, LocalDateTime timestamp) {
    timestamp = LocalDateTime.now();
    log.error(timestamp.toString());
    Collection<Trade> trades = stateStore.getTrades(account);
    Map<Asset, List<Trade>> tradesByAsset =
        trades.stream().collect(groupingBy(Trade::getAsset, toList()));
    List<Holding> holdings = Lists.newLinkedList();
    for (Entry<Asset, List<Trade>> assetListEntry : tradesByAsset.entrySet()) {
      Asset asset = assetListEntry.getKey();
      Double quantity =
          assetListEntry.getValue().stream().map(Trade::getQuantity).reduce(0.0, Double::sum);
      Double cashFlow =
          assetListEntry.getValue().stream().map(Trade::getCashFlow).reduce(0.0, Double::sum);
      holdings.add(
          Holding.builder()
              .asset(asset)
              .bookValue(cashFlow)
              .timestamp(timestamp)
              .quantity(quantity)
              .marketValue(cashFlow)
              .build());
    }

    return holdings;
  }

  public Order submitOrder(
      org.yafa.api.dto.outbound.Account account, org.yafa.api.dto.inbound.Order order) {
    resolveOrder(account, order).forEach(trade -> stateStore.saveTrade(account, trade));
    return stateStore.saveOrder(account, order);
  }

  public Collection<Order> listOrders(org.yafa.api.dto.outbound.Account account) {
    return stateStore.getOrders(account);
  }

  public org.yafa.api.dto.outbound.Trade recordTrade(
      org.yafa.api.dto.outbound.Account account, Trade trade) {
    log.debug("recording trade: {}", trade.toString());
    return stateStore.saveTrade(account, trade);
  }
}

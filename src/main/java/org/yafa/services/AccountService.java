package org.yafa.services;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import com.google.common.collect.Lists;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.yafa.api.dto.Asset;
import org.yafa.api.dto.inbound.ClientSideAccount;
import org.yafa.api.dto.inbound.ClientSideOrder;
import org.yafa.api.dto.inbound.ClientSideTrade;
import org.yafa.api.dto.outbound.Holding;
import org.yafa.api.dto.outbound.ServerSideAccount;
import org.yafa.api.dto.outbound.ServerSideOrder;
import org.yafa.api.dto.outbound.ServerSideTrade;
import org.yafa.state.StateStore;

@Slf4j
@Dependent
public class AccountService {

  @Inject StateStore stateStore;

  public ServerSideAccount create(@Valid ClientSideAccount clientSideAccount) {
    return stateStore.createAccount(clientSideAccount);
  }

  public Collection<ServerSideAccount> getAccounts() {
    return stateStore.getAccounts();
  }

  public ServerSideAccount getAccount(String accountId) {
    return stateStore.getAccount(accountId);
  }

  public Collection<ClientSideTrade> listTrades(ServerSideAccount serverSideAccount) {
    return stateStore.getTrades(serverSideAccount);
  }

  private Collection<ClientSideTrade> resolveOrder(
      ServerSideAccount serverSideAccount, ClientSideOrder clientSideOrder) {
    return Lists.newLinkedList();
  }

  public Collection<Holding> listHoldings(
      ServerSideAccount serverSideAccount, ZonedDateTime timestamp) {
    log.error(timestamp.toString());
    Collection<ClientSideTrade> clientSideTrades = stateStore.getTrades(serverSideAccount);
    Map<Asset, List<ClientSideTrade>> tradesByAsset =
        clientSideTrades.stream().collect(groupingBy(ClientSideTrade::getAsset, toList()));
    List<Holding> holdings = Lists.newLinkedList();
    for (Entry<Asset, List<ClientSideTrade>> assetListEntry : tradesByAsset.entrySet()) {
      Asset asset = assetListEntry.getKey();
      BigDecimal quantity =
          assetListEntry.getValue().stream().map(ClientSideTrade::getQuantity)
              .reduce(BigDecimal.ZERO, BigDecimal::add);
      BigDecimal cashFlow =
          assetListEntry.getValue().stream().map(ClientSideTrade::getCashFlow)
              .reduce(BigDecimal.ZERO, BigDecimal::add);
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

  public ServerSideOrder submitOrder(
      ServerSideAccount serverSideAccount, ClientSideOrder clientSideOrder) {
    resolveOrder(serverSideAccount, clientSideOrder).forEach(trade -> stateStore.saveTrade(
        serverSideAccount, trade));
    return stateStore.saveOrder(serverSideAccount, clientSideOrder);
  }

  public Collection<ServerSideOrder> listOrders(ServerSideAccount serverSideAccount) {
    return stateStore.getOrders(serverSideAccount);
  }

  public ServerSideTrade recordTrade(
      ServerSideAccount serverSideAccount, ClientSideTrade clientSideTrade) {
    log.debug("recording trade: {}", clientSideTrade.toString());
    return stateStore.saveTrade(serverSideAccount, clientSideTrade);
  }
}

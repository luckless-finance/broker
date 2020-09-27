package org.yafa.state;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import lombok.extern.slf4j.Slf4j;
import org.yafa.api.dto.Id;
import org.yafa.api.dto.inbound.Account;
import org.yafa.api.dto.inbound.Trade;
import org.yafa.api.dto.outbound.Order;
import org.yafa.exceptions.ConflictException;
import org.yafa.exceptions.NotFoundException;

@Default
@Dependent
@Slf4j
public class InMemory implements StateStore {

  Map<String, org.yafa.api.dto.outbound.Account> accountsByName = Maps.newHashMap();
  Map<String, org.yafa.api.dto.outbound.Account> accounts = Maps.newHashMap();
  Map<org.yafa.api.dto.outbound.Account, Map<String, Order>> orders = Maps.newHashMap();
  Map<org.yafa.api.dto.outbound.Account, Map<String, Trade>> trades = Maps.newHashMap();

  private Optional<org.yafa.api.dto.outbound.Account> findByName(String name) {
    return accounts.values().stream().filter(account -> account.getName().equals(name)).findFirst();
  }

  @Override
  public org.yafa.api.dto.outbound.Account createAccount(Account account) {
    log.debug("creating account: {}", account.getName());
    Optional<org.yafa.api.dto.outbound.Account> foundAccount = findByName(account.getName());
    if (foundAccount.isPresent()) {
      throw new ConflictException("Account: " + foundAccount.get().toString() + " already exists");
    } else {
      org.yafa.api.dto.outbound.Account persistedAccount = withId(account);
      accounts.put(persistedAccount.getId(), persistedAccount);
      trades.put(persistedAccount, Maps.newHashMap());
      orders.put(persistedAccount, Maps.newHashMap());
      return getAccount(persistedAccount.getId());
    }
  }

  @Override
  public Collection<org.yafa.api.dto.outbound.Account> getAccounts() {
    return accounts.values();
  }

  @Override
  public org.yafa.api.dto.outbound.Account getAccount(String accountId) {
    if (accounts.containsKey(accountId)) {
      return accounts.get(accountId);
    } else {
      throw new NotFoundException("Account Id: " + accountId + " not found.");
    }
  }

  private Order withId(org.yafa.api.dto.inbound.Order order) {
    return Order.builder()
        .id(Id.create())
        .orderStatus(order.getOrderStatus())
        .asset(order.getAsset())
        .cashFlow(order.getCashFlow())
        .quantity(order.getQuantity())
        .timestamp(order.getTimestamp())
        .build();
  }

  private org.yafa.api.dto.outbound.Trade withId(Trade trade) {
    return org.yafa.api.dto.outbound.Trade.builder()
        .id(Id.create())
        .bookValue(trade.getBookValue())
        .marketUnitValue(trade.getMarketUnitValue())
        .marketValue(trade.getMarketValue())
        .asset(trade.getAsset())
        .timestamp(trade.getTimestamp())
        .build();
  }

  private org.yafa.api.dto.outbound.Account withId(org.yafa.api.dto.inbound.Account account) {
    return org.yafa.api.dto.outbound.Account.builder()
        .id(Id.create())
        .name(account.getName())
        .build();
  }

  @Override
  public Order saveOrder(
      org.yafa.api.dto.outbound.Account account, org.yafa.api.dto.inbound.Order order) {
    final Order persistedOrder = withId(order);
    getAccount(account.getId());
    orders.get(account).put(persistedOrder.getId(), persistedOrder);
    return persistedOrder;
  }

  @Override
  public Collection<Order> getOrders(org.yafa.api.dto.outbound.Account account) {
    getAccount(account.getId());
    return orders.get(account).values();
  }

  @Override
  public Collection<Trade> getTrades(org.yafa.api.dto.outbound.Account account) {
    getAccount(account.getId());
    return trades.get(account).values();
  }

  @Override
  public org.yafa.api.dto.outbound.Trade saveTrade(
      org.yafa.api.dto.outbound.Account account, Trade trade) {
    getAccount(account.getId());
    org.yafa.api.dto.outbound.Trade tradeWithId = withId(trade);
    trades.get(account).put(tradeWithId.getId(), tradeWithId);
    return tradeWithId;
  }
}

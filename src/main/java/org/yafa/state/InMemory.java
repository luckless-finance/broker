package org.yafa.state;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import lombok.extern.slf4j.Slf4j;
import org.yafa.api.dto.Id;
import org.yafa.api.dto.inbound.ClientSideAccount;
import org.yafa.api.dto.inbound.ClientSideOrder;
import org.yafa.api.dto.inbound.ClientSideTrade;
import org.yafa.api.dto.inbound.OrderStatus;
import org.yafa.api.dto.outbound.ServerSideAccount;
import org.yafa.api.dto.outbound.ServerSideOrder;
import org.yafa.api.dto.outbound.ServerSideTrade;
import org.yafa.exceptions.ConflictException;
import org.yafa.exceptions.NotFoundException;

@Default
@Dependent
@Slf4j
public class InMemory implements StateStore {

  Map<String, ServerSideAccount> accountsByName = Maps.newHashMap();
  Map<String, ServerSideAccount> accounts = Maps.newHashMap();
  Map<ServerSideAccount, Map<String, ServerSideOrder>> orders = Maps.newHashMap();
  Map<ServerSideAccount, Map<String, ClientSideTrade>> trades = Maps.newHashMap();

  private Optional<ServerSideAccount> findByName(String name) {
    return accounts.values().stream().filter(account -> account.getName().equals(name)).findFirst();
  }

  @Override
  public ServerSideAccount createAccount(ClientSideAccount clientSideAccount) {
    log.debug("creating account: {}", clientSideAccount.getName());
    Optional<ServerSideAccount> foundAccount = findByName(clientSideAccount.getName());
    if (foundAccount.isPresent()) {
      throw new ConflictException("Account: " + foundAccount.get().toString() + " already exists");
    } else {
      ServerSideAccount serverSideAccount = withId(clientSideAccount);
      accounts.put(serverSideAccount.getId(), serverSideAccount);
      trades.put(serverSideAccount, Maps.newHashMap());
      orders.put(serverSideAccount, Maps.newHashMap());
      return getAccount(serverSideAccount.getId());
    }
  }

  @Override
  public Collection<ServerSideAccount> getAccounts() {
    return accounts.values();
  }

  @Override
  public ServerSideAccount getAccount(String accountId) {
    if (accounts.containsKey(accountId)) {
      return accounts.get(accountId);
    } else {
      throw new NotFoundException("Account Id: " + accountId + " not found.");
    }
  }

  private ServerSideOrder withId(ClientSideOrder clientSideOrder) {
    return ServerSideOrder.builder()
        .id(Id.create())
        .orderStatus(OrderStatus.COMPLETE)
        .asset(clientSideOrder.getAsset())
        .cashFlow(clientSideOrder.getCashFlow())
        .quantity(clientSideOrder.getQuantity())
        .timestamp(clientSideOrder.getTimestamp())
        .build();
  }

  private ServerSideTrade withId(ClientSideTrade clientSideTrade) {
    return ServerSideTrade.builder()
        .id(Id.create())
        .quantity(clientSideTrade.getQuantity())
        .unitPrice(clientSideTrade.getUnitPrice())
        .cashFlow(clientSideTrade.getCashFlow())
        .asset(clientSideTrade.getAsset())
        .timestamp(clientSideTrade.getTimestamp())
        .build();
  }

  private ServerSideAccount withId(ClientSideAccount clientSideAccount) {
    return ServerSideAccount.builder().id(Id.create()).name(clientSideAccount.getName()).build();
  }

  @Override
  public ServerSideOrder saveOrder(
      ServerSideAccount serverSideAccount, ClientSideOrder clientSideOrder) {
    final ServerSideOrder serverSideOrder = withId(clientSideOrder);
    getAccount(serverSideAccount.getId());
    orders.get(serverSideAccount).put(serverSideOrder.getId(), serverSideOrder);
    return serverSideOrder;
  }

  @Override
  public Collection<ServerSideOrder> getOrders(ServerSideAccount serverSideAccount) {
    getAccount(serverSideAccount.getId());
    return orders.get(serverSideAccount).values();
  }

  @Override
  public Collection<ClientSideTrade> getTrades(ServerSideAccount serverSideAccount) {
    getAccount(serverSideAccount.getId());
    return trades.get(serverSideAccount).values();
  }

  @Override
  public ServerSideTrade saveTrade(
      ServerSideAccount serverSideAccount, ClientSideTrade clientSideTrade) {
    getAccount(serverSideAccount.getId());
    ServerSideTrade tradeWithId = withId(clientSideTrade);
    trades.get(serverSideAccount).put(tradeWithId.getId(), tradeWithId);
    return tradeWithId;
  }
}

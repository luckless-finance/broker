package org.yafa.state;

import java.util.Collection;
import org.yafa.api.dto.inbound.Account;
import org.yafa.api.dto.inbound.Trade;
import org.yafa.api.dto.outbound.Order;

public interface StateStore {

  org.yafa.api.dto.outbound.Account createAccount(Account account);

  Collection<org.yafa.api.dto.outbound.Account> getAccounts();

  org.yafa.api.dto.outbound.Account getAccount(String accountId);

  Order saveOrder(org.yafa.api.dto.outbound.Account account, org.yafa.api.dto.inbound.Order order);

  Collection<Order> getOrders(org.yafa.api.dto.outbound.Account account);

  Collection<Trade> getTrades(org.yafa.api.dto.outbound.Account account);

  org.yafa.api.dto.outbound.Trade saveTrade(org.yafa.api.dto.outbound.Account account, Trade trade);
}

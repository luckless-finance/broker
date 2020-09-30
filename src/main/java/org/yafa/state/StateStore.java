package org.yafa.state;

import java.util.Collection;
import org.yafa.api.dto.inbound.ClientSideAccount;
import org.yafa.api.dto.inbound.ClientSideOrder;
import org.yafa.api.dto.inbound.ClientSideTrade;
import org.yafa.api.dto.outbound.ServerSideAccount;
import org.yafa.api.dto.outbound.ServerSideOrder;
import org.yafa.api.dto.outbound.ServerSideTrade;

public interface StateStore {

  ServerSideAccount createAccount(ClientSideAccount clientSideAccount);

  Collection<ServerSideAccount> getAccounts();

  ServerSideAccount getAccount(String accountId);

  ServerSideOrder saveOrder(ServerSideAccount serverSideAccount, ClientSideOrder clientSideOrder);

  Collection<ServerSideOrder> getOrders(ServerSideAccount serverSideAccount);

  Collection<ClientSideTrade> getTrades(ServerSideAccount serverSideAccount);

  ServerSideTrade saveTrade(ServerSideAccount serverSideAccount, ClientSideTrade clientSideTrade);
}

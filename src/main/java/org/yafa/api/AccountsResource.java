package org.yafa.api;

import java.time.ZonedDateTime;
import java.util.Collection;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.yafa.annotations.DateTimeFormat;
import org.yafa.api.dto.inbound.ClientSideAccount;
import org.yafa.api.dto.inbound.ClientSideOrder;
import org.yafa.api.dto.inbound.ClientSideTrade;
import org.yafa.api.dto.outbound.Holding;
import org.yafa.api.dto.outbound.ServerSideAccount;
import org.yafa.api.dto.outbound.ServerSideOrder;
import org.yafa.api.dto.outbound.ServerSideTrade;
import org.yafa.exceptions.ConflictException;
import org.yafa.services.AccountService;

/** FIXME timestamps are returned at arrays */
@Slf4j
@Path("/accounts")
@Tag(name = "Accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountsResource {

  @Inject AccountService accountService;

  @POST
  @Operation(summary = "create new Account")
  public ServerSideAccount create(@Valid ClientSideAccount clientSideAccount) {
    try {
      return accountService.create(clientSideAccount);
    } catch (ConflictException e) {
      // TODO add accountId to error message
      throw new ClientErrorException(
          clientSideAccount.getName() + " already exists.", Status.CONFLICT);
    }
  }

  @GET
  @Path("/{accountId}")
  // TODO change type of accountId to Id
  public ServerSideAccount getAccount(@PathParam("accountId") String accountId) {
    try {
      return accountService.getAccount(accountId);
    } catch (org.yafa.exceptions.NotFoundException e) {
      throw new NotFoundException(e);
    }
  }

  @GET
  public Collection<ServerSideAccount> listAccounts() {
    return accountService.getAccounts();
  }

  @POST
  @Path("/{accountId}/orders")
  public ServerSideOrder submitOrder(
      @PathParam("accountId") String accountId, @Valid ClientSideOrder clientSideOrder) {
    return accountService.submitOrder(accountService.getAccount(accountId), clientSideOrder);
  }

  @GET
  @Path("/{accountId}/orders")
  public Collection<ServerSideOrder> listOrders(@PathParam("accountId") String accountId) {
    return accountService.listOrders(accountService.getAccount(accountId));
  }

  @POST
  @Path("/{accountId}/trades")
  public ServerSideTrade recordTrade(
      @PathParam("accountId") String accountId, @Valid ClientSideTrade clientSideTrade) {
    return accountService.recordTrade(accountService.getAccount(accountId), clientSideTrade);
  }

  @GET
  @Path("/{accountId}/trades")
  public Collection<ClientSideTrade> listTrades(@PathParam("accountId") String accountId) {
    return accountService.listTrades(accountService.getAccount(accountId));
  }

  @GET
  @Path("/{accountId}/holdings")
  public Collection<Holding> listHoldings(
      @PathParam("accountId") String accountId,
      @DateTimeFormat @QueryParam("timestamp") ZonedDateTime timestamp) {
    return accountService.listHoldings(accountService.getAccount(accountId), ZonedDateTime.now());
  }
}

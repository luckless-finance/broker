package org.yafa.api;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
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
import org.yafa.annotations.DateFormat;
import org.yafa.api.dto.inbound.Account;
import org.yafa.api.dto.inbound.Trade;
import org.yafa.api.dto.outbound.Holding;
import org.yafa.exceptions.ConflictException;
import org.yafa.services.AccountService;

/** FIXME timestamps are returned at arrays */
@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {

  @Inject AccountService accountService;

  @POST
  public org.yafa.api.dto.outbound.Account create(@Valid Account account) {
    try {
      return accountService.create(account);
    } catch (ConflictException e) {
      // TODO add accountId to error message
      throw new ClientErrorException(account.getName() + " already exists.", Status.CONFLICT);
    }
  }

  @GET
  @Path("/{accountId}")
  // TODO change type of accountId to Id
  public org.yafa.api.dto.outbound.Account getAccount(@PathParam("accountId") String accountId) {
    try {
      return accountService.getAccount(accountId);
    } catch (org.yafa.exceptions.NotFoundException e) {
      throw new NotFoundException(e);
    }
  }

  @GET
  public Collection<org.yafa.api.dto.outbound.Account> listAccounts() {
    return accountService.getAccounts();
  }

  @POST
  @Path("/{accountId}/cash")
  public org.yafa.api.dto.outbound.Order cashFlow(
      @PathParam("accountId") String accountId, @Valid org.yafa.api.dto.inbound.Order order) {
    return accountService.submitOrder(accountService.getAccount(accountId), order);
  }

  @POST
  @Path("/{accountId}/orders")
  public org.yafa.api.dto.outbound.Order submitOrder(
      @PathParam("accountId") String accountId, @Valid org.yafa.api.dto.inbound.Order order) {
    return accountService.submitOrder(accountService.getAccount(accountId), order);
  }

  @GET
  @Path("/{accountId}/orders")
  public Collection<org.yafa.api.dto.outbound.Order> listOrders(
      @PathParam("accountId") String accountId) {
    return accountService.listOrders(accountService.getAccount(accountId));
  }

  @GET
  @Path("/{accountId}/trades")
  public Collection<Trade> listTrades(@PathParam("accountId") String accountId) {
    return accountService.listTrades(accountService.getAccount(accountId));
  }

  @GET
  @Path("/{accountId}/holdings")
  public Collection<Holding> listHoldings(
      @PathParam("accountId") String accountId,
      @DateFormat @QueryParam("timestamp") Date timestamp) {
    return accountService.listHoldings(
        accountService.getAccount(accountId), LocalDateTime.from(timestamp.toInstant()));
  }
}

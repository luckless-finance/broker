package org.yafa.api;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
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
import org.yafa.api.dto.Account;
import org.yafa.api.dto.Holding;
import org.yafa.api.dto.Order;
import org.yafa.api.dto.Trade;
import org.yafa.services.AccountService;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {

  @Inject
  AccountService accountService;

  @POST
  public Account create(@Valid Account account) {
    Optional<Account> optionalAccountDto = accountService.create(account);
    if (optionalAccountDto.isEmpty()) {
      throw new ClientErrorException(Status.CONFLICT);
    }
    return optionalAccountDto.get();
  }

  @GET
  public Collection<Account> get() {
    Collection<Account> accounts = accountService.getAccounts();
    if (accounts.isEmpty()) {
      throw new NotFoundException("No Accounts Exist.  Create an Account.");
    }
    return accounts;
  }

  @GET
  @Path("/{accountId}")
  public Account get(@PathParam("accountId") String accountId) {
    Optional<Account> optionalAccountDto = accountService.getAccount(accountId);
    if (optionalAccountDto.isEmpty()) {
      throw new NotFoundException(String.format("Account: %s NOT FOUND", accountId));
    }
    return optionalAccountDto.get();
  }

  @GET
  @Path("/{accountId}/trades")
  public Collection<Trade> listTrades(@PathParam("accountId") String accountId) {
    throw new ClientErrorException(Status.NOT_IMPLEMENTED);
  }

  @GET
  @Path("/{accountId}/holdings")
  public Collection<Holding> listHoldings(
      @PathParam("accountId")
          String accountId,
      // FIXME: implement datetime QueryParam
      // https://zenidas.wordpress.com/recipes/configurable-date-format-in-jax-rs-as-queryparam/
      @QueryParam("timestamp")
      @DateFormat Date timestamp) {
    throw new ClientErrorException(Status.NOT_IMPLEMENTED);
  }

  @POST
  @Path("/{accountId}/orders")
  public Trade submitOrder(@PathParam("accountId") String accountId, @Valid Order order) {
    throw new ClientErrorException(Status.NOT_IMPLEMENTED);
  }
}

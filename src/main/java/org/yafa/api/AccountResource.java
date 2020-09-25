package org.yafa.api;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.yafa.dto.AccountDto;
import org.yafa.services.AccountService;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {

  @Inject
  AccountService accountService;

  @POST
  public AccountDto create(AccountDto accountDto) {
    return accountService.create(accountDto);
  }
}

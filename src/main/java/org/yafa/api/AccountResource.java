package org.yafa.api;

import java.util.Optional;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import org.yafa.api.dto.AccountDto;
import org.yafa.services.AccountService;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {

  @Inject
  AccountService accountService;

  @GET
  public AccountDto get(@NotBlank @QueryParam("name") String name) {
    Optional<AccountDto> optionalAccountDto = accountService.find(name);
    if (optionalAccountDto.isEmpty()) {
      throw new NotFoundException(String.format("Account: %s NOT FOUND", name));
    }
    return optionalAccountDto.get();
  }

  @POST
  public AccountDto create(@Valid AccountDto accountDto) {
    Optional<AccountDto> optionalAccountDto = accountService.create(accountDto);
    if (optionalAccountDto.isEmpty()) {
      throw new ClientErrorException(Status.CONFLICT);
    }
    return optionalAccountDto.get();
  }
}

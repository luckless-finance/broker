package org.yafa.services;

import java.util.Optional;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.yafa.api.dto.AccountDto;
import org.yafa.state.StateStore;

@Slf4j
@Dependent
public class AccountService {

  @Inject
  StateStore stateStore;

  public Optional<AccountDto> create(@Valid AccountDto accountDto) {
    return stateStore.createAccount(accountDto);
  }

  public Optional<AccountDto> find(String name) {
    return stateStore.findAccount(name);
  }
}

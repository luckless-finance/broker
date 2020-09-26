package org.yafa.services;

import java.util.Collection;
import java.util.Optional;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.yafa.api.dto.Account;
import org.yafa.state.StateStore;

@Slf4j
@Dependent
public class AccountService {

  @Inject
  StateStore stateStore;

  public Optional<Account> create(@Valid Account account) {
    return stateStore.createAccount(account);
  }

  public Collection<Account> getAccounts() {
    return stateStore.getAccounts();
  }

  public Optional<Account> getAccount(String accountId) {
    return stateStore.getAccount(accountId);
  }
}

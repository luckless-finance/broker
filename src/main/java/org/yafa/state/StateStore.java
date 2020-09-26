package org.yafa.state;

import java.util.Collection;
import java.util.Optional;
import org.yafa.api.dto.Account;

public interface StateStore {

  Optional<Account> createAccount(Account account);

  Collection<Account> getAccounts();

  Optional<Account> getAccount(String accountId);




}

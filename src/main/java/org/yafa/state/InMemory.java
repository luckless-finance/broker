package org.yafa.state;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import lombok.extern.slf4j.Slf4j;
import org.yafa.api.dto.Account;

@Default
@Dependent
@Slf4j
public class InMemory implements StateStore {

  Map<String, Account> accounts = Maps.newHashMap();

  @Override
  public Optional<Account> createAccount(Account account) {
    log.debug("creating account: {}", account.getName());
    if (accounts.containsKey(account.getName())) {
      return Optional.empty();
    } else {
      accounts.put(account.getName(), account);
      return getAccount(account.getName());
    }
  }

  @Override
  public Collection<Account> getAccounts() {
    return accounts.values();
  }

  @Override
  public Optional<Account> getAccount(String accountId) {
    return Optional.ofNullable(accounts.getOrDefault(accountId, null));
  }
}

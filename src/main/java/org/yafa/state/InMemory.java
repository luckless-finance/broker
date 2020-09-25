package org.yafa.state;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Optional;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import lombok.extern.slf4j.Slf4j;
import org.yafa.api.dto.AccountDto;

@Default
@Dependent
@Slf4j
public class InMemory implements StateStore {

  Map<String, AccountDto> accounts = Maps.newHashMap();


  @Override
  public Optional<AccountDto> findAccount(String name) {
    return Optional.ofNullable(accounts.getOrDefault(name, null));
  }

  @Override
  public Optional<AccountDto> createAccount(AccountDto accountDto) {
    log.debug("creating account: {}", accountDto.getName());
    if (accounts.containsKey(accountDto.getName())) {
      return Optional.empty();
    } else {
      accounts.put(accountDto.getName(), accountDto);
      return findAccount(accountDto.getName());
    }
  }
}

package org.yafa.state;

import java.util.Optional;
import org.yafa.api.dto.AccountDto;

public interface StateStore {

  Optional<AccountDto> findAccount(String name);

  Optional<AccountDto> createAccount(AccountDto accountDto);


}

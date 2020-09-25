package org.yafa.services;

import javax.enterprise.context.Dependent;
import org.yafa.dto.AccountDto;

@Dependent
public class AccountService {

  public AccountDto create(AccountDto accountDto) {
    return accountDto;
  }
}

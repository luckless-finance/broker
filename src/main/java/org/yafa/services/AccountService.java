package org.yafa.services;

import javax.enterprise.context.Dependent;
import javax.validation.Valid;
import org.yafa.dto.AccountDto;

@Dependent
public class AccountService {

  public AccountDto create(@Valid AccountDto accountDto) {
    return accountDto;
  }
}

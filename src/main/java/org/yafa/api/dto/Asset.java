package org.yafa.api.dto;

import java.util.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Asset {

  String symbol;
  Currency currency;
}

package org.yafa.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Asset {

  final String symbol;
  final CurrencyCode currency;
}

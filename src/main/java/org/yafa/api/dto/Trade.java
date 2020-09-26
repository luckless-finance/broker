package org.yafa.api.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Trade {

  Asset asset;
  LocalDateTime timestamp;
  float quantity;
  float marketUnitValue;
  float marketValue;
  float bookValue;

}

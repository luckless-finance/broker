package org.yafa.api.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Holding {

  Asset asset;
  LocalDateTime timestamp;
  float quantity;
  float marketValue;
  float bookValue;

}

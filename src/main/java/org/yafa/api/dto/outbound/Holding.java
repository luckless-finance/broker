package org.yafa.api.dto.outbound;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;
import org.yafa.api.dto.Asset;

@Value
@Builder
public class Holding {

  Asset asset;
  LocalDateTime timestamp;
  double quantity;
  double marketValue;
  double bookValue;
}

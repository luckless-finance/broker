package org.yafa.api.dto.outbound;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.ZonedDateTime;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import org.yafa.api.dto.Asset;
import org.yafa.api.dto.Config;

@Value
@Builder
public class Holding {

  Asset asset;

  @JsonFormat(pattern = Config.TIME_STAMP_PATTERN)
  @NotNull
  ZonedDateTime timestamp;

  double quantity;
  double marketValue;
  double bookValue;
}

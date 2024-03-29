package org.yafa.api.dto.outbound;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
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

  BigDecimal quantity;

  @JsonProperty("market_value")
  BigDecimal marketValue;

  @JsonProperty("book_value")
  BigDecimal bookValue;
}

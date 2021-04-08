package org.yafa.api.dto.inbound;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.yafa.api.dto.Asset;
import org.yafa.api.dto.Config;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ClientSideTrade {

  @NotNull Asset asset;

  @JsonFormat(pattern = Config.TIME_STAMP_PATTERN)
  @NotNull
  ZonedDateTime timestamp;

  @NotNull BigDecimal quantity;

  @JsonProperty("unit_price")
  @NotNull
  BigDecimal unitPrice;

  @JsonProperty("cash_flow")
  @NotNull
  BigDecimal cashFlow;
}

package org.yafa.api.dto.inbound;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class Trade {

  @NotNull Asset asset;

  @JsonFormat(pattern = Config.TIME_STAMP_PATTERN)
  @NotNull
  ZonedDateTime timestamp;

  @NotNull BigDecimal quantity;
  @NotNull BigDecimal unitPrice;
  @NotNull BigDecimal cashFlow;
}

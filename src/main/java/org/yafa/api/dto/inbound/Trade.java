package org.yafa.api.dto.inbound;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.yafa.api.dto.Asset;

@Data
@SuperBuilder
public class Trade {

  @NotNull final Asset asset;
  @NotNull final LocalDateTime timestamp;
  @NotNull final double quantity;
  @NotNull final double cashFlow;
  @NotNull final float marketUnitValue;
  @NotNull final float marketValue;
  @NotNull final float bookValue;
}

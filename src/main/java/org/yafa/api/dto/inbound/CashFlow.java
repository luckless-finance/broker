package org.yafa.api.dto.inbound;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.yafa.api.dto.CurrencyCode;

@Data
@SuperBuilder
public class CashFlow {

  @NotNull final CurrencyCode currency;
  @NotNull final LocalDateTime timestamp;
  @NotNull final double quantity;
  @NotNull final double cashFlow;
  @NotNull final double marketUnitValue;
  @NotNull final double marketValue;
  @NotNull final double bookValue;
}

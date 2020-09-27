package org.yafa.api.dto.inbound;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.yafa.api.dto.Asset;

@Data
@SuperBuilder
@AllArgsConstructor
public class Order {

  @NotNull final Asset asset;
  @NotNull final LocalDateTime timestamp;
  @NotNull final OrderStatus orderStatus;
  @NotNull final double quantity;
  @NotNull final double cashFlow;
}

package org.yafa.api.dto.inbound;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.yafa.api.dto.Asset;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

  @NotNull Asset asset;
  @NotNull LocalDateTime timestamp;
  @NotNull double quantity;
  @NotNull double cashFlow;
}

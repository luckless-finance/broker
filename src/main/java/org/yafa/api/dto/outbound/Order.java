package org.yafa.api.dto.outbound;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import org.yafa.api.dto.inbound.OrderStatus;

@Value
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Order extends org.yafa.api.dto.inbound.Order {

  @NotBlank String id;
  @NotNull OrderStatus orderStatus;
}

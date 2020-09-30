package org.yafa.api.dto.outbound;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import org.yafa.api.dto.inbound.ClientSideOrder;
import org.yafa.api.dto.inbound.OrderStatus;

@Value
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServerSideOrder extends ClientSideOrder {

  @NotBlank String id;
  @NotNull OrderStatus orderStatus;
}

package org.yafa.api.dto.outbound;

import javax.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;


@Value
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Order extends org.yafa.api.dto.inbound.Order {

  @NotBlank
  String id;
}

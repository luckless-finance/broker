package org.yafa.api.dto.outbound;

import javax.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@Value
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Trade extends org.yafa.api.dto.inbound.Trade {

  @NotBlank String id;
}

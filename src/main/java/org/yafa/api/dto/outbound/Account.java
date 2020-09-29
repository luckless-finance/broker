package org.yafa.api.dto.outbound;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@Value
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Account extends org.yafa.api.dto.inbound.Account {

  @NotBlank String id;
}

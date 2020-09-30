package org.yafa.api.dto.outbound;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import org.yafa.api.dto.inbound.ClientSideAccount;

@Value
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServerSideAccount extends ClientSideAccount {

  @NotBlank String id;
}

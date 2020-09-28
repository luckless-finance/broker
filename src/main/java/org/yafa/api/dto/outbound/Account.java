package org.yafa.api.dto.outbound;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@Value
@SuperBuilder
@AllArgsConstructor
public class Account {

  @NotBlank String id;
  @NotBlank String name;
}

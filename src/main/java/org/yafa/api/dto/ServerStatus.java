package org.yafa.api.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class ServerStatus {

  @NotNull
  Status status;

  public enum Status {
    OK
  }
}

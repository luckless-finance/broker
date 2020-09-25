package org.yafa.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class StatusDto {

  @NotNull
  Status status;

  public enum Status {
    OK,
    ERROR
  }
}

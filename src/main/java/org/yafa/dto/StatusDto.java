package org.yafa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class StatusDto {

  Status status;

  public enum Status {
    OK,
    ERROR
  }
}

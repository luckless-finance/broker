package org.yafa.api.dto.outbound;

import java.time.LocalDateTime;
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

  @NotNull
  LocalDateTime timestamp = LocalDateTime.now();

  public enum Status {
    OK
  }
}

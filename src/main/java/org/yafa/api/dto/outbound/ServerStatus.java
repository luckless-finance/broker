package org.yafa.api.dto.outbound;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.ZonedDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.yafa.api.dto.Config;

@Value
@Builder
@AllArgsConstructor
public class ServerStatus {

  @NotNull Status status;

  @JsonFormat(pattern = Config.TIME_STAMP_PATTERN)
  @NotNull
  ZonedDateTime timestamp = ZonedDateTime.now();

  public enum Status {
    OK
  }
}

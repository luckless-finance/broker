package org.yafa.services;

import javax.enterprise.context.Dependent;
import org.yafa.dto.StatusDto;

@Dependent
public class StatusService {
  public StatusDto getStatus() {
    return StatusDto.builder().status(StatusDto.Status.OK).build();
  }
}

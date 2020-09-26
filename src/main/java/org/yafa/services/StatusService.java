package org.yafa.services;

import javax.enterprise.context.Dependent;
import org.yafa.api.dto.ServerStatus;

@Dependent
public class StatusService {
  public ServerStatus getStatus() {
    return ServerStatus.builder().status(ServerStatus.Status.OK).build();
  }
}

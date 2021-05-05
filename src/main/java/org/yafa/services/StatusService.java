package org.yafa.services;

import javax.enterprise.context.Dependent;
import lombok.extern.slf4j.Slf4j;
import org.yafa.api.dto.outbound.ServerStatus;

@Slf4j
@Dependent
public class StatusService {
  public ServerStatus getStatus() {
    log.debug("debug");
    log.info("info");
    log.warn("warn");
    log.error("error");
    return ServerStatus.builder().status(ServerStatus.Status.OK).build();
  }
}

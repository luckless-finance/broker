package org.yafa.api;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.yafa.api.dto.outbound.ServerStatus;
import org.yafa.services.StatusService;

@Path("/status")
@Produces(MediaType.APPLICATION_JSON)
public class StatusResource {

  @Inject StatusService statusService;

  @GET
  public ServerStatus getStatus() {
    return statusService.getStatus();
  }
}

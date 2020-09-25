package org.yafa.api;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.yafa.dto.StatusDto;
import org.yafa.services.StatusService;

@Path("/status")
public class StatusResource {

  @Inject
  StatusService statusService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public StatusDto hello() {
    return statusService.getStatus();
  }
}

package org.yafa.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.UUID;
import javax.validation.constraints.NotBlank;

@JsonDeserialize(as = String.class)
public class Id {

  @NotBlank @JsonIgnore private final String id;

  private Id(String id) {
    this.id = id;
  }

  public static String create() {
    return new Id(UUID.randomUUID().toString().substring(0, 8)).id;
  }
}

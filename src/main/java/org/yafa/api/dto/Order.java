package org.yafa.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;


@Value
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Order extends Transaction {

  Status orderStatus;

  private enum Status {
    COMPLETE
  }
}

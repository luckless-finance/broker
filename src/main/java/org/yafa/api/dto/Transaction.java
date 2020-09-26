package org.yafa.api.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

  Asset asset;
  LocalDateTime timestamp;
  float quantity;
  float cashFlow;
}

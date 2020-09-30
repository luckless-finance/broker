package org.yafa.api.dto.outbound;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yafa.api.dto.Asset;
import org.yafa.api.dto.CurrencyCode;
import org.yafa.api.dto.inbound.ClientSideOrder;

class OrderTest {

  ClientSideOrder clientSideOrder;
  String symbol = "FOO";
  CurrencyCode currency = CurrencyCode.CAD;
  BigDecimal cashFlow = BigDecimal.valueOf(123);
  BigDecimal quantity = BigDecimal.valueOf(12.3);
  ZonedDateTime timestamp = ZonedDateTime.now();

  @BeforeEach
  public void setup() {
    clientSideOrder =
        ClientSideOrder.builder()
            .asset(Asset.builder().symbol(symbol).currency(currency).build())
            .cashFlow(cashFlow)
            .quantity(quantity)
            .timestamp(timestamp)
            .build();
  }

  @Test
  void testEquivalence() {
    ClientSideOrder equivalentClientSideOrder =
        ClientSideOrder.builder()
            .asset(Asset.builder().symbol(symbol).currency(currency).build())
            .cashFlow(cashFlow)
            .quantity(quantity)
            .timestamp(timestamp)
            .build();
    Assertions.assertEquals(clientSideOrder, equivalentClientSideOrder);
    Assertions.assertEquals(clientSideOrder.hashCode(), equivalentClientSideOrder.hashCode());

    ZonedDateTime differentTimestamp = ZonedDateTime.now();
    ClientSideOrder differentClientSideOrder =
        ClientSideOrder.builder()
            .asset(Asset.builder().symbol(symbol).currency(currency).build())
            .cashFlow(cashFlow)
            .quantity(quantity)
            .timestamp(differentTimestamp)
            .build();
    Assertions.assertNotEquals(clientSideOrder, differentClientSideOrder);
    Assertions.assertNotEquals(clientSideOrder.hashCode(), differentClientSideOrder.hashCode());
  }
}

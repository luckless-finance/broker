package org.yafa.api.dto.outbound;

import java.time.ZonedDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yafa.api.dto.Asset;
import org.yafa.api.dto.CurrencyCode;
import org.yafa.api.dto.inbound.Order;

class OrderTest {

  Order order;
  String symbol = "FOO";
  CurrencyCode currency = CurrencyCode.CAD;
  double cashFlow = 123;
  double quantity = 12.3;
  ZonedDateTime timestamp = ZonedDateTime.now();

  @BeforeEach
  public void setup() {
    order =
        Order.builder()
            .asset(Asset.builder().symbol(symbol).currency(currency).build())
            .cashFlow(cashFlow)
            .quantity(quantity)
            .timestamp(timestamp)
            .build();
  }

  @Test
  void testEquivalence() {
    Order equivalentOrder =
        Order.builder()
            .asset(Asset.builder().symbol(symbol).currency(currency).build())
            .cashFlow(cashFlow)
            .quantity(quantity)
            .timestamp(timestamp)
            .build();
    Assertions.assertEquals(order, equivalentOrder);
    Assertions.assertEquals(order.hashCode(), equivalentOrder.hashCode());

    ZonedDateTime differentTimestamp = ZonedDateTime.now();
    Order differentOrder =
        Order.builder()
            .asset(Asset.builder().symbol(symbol).currency(currency).build())
            .cashFlow(cashFlow)
            .quantity(quantity)
            .timestamp(differentTimestamp)
            .build();
    Assertions.assertNotEquals(order, differentOrder);
    Assertions.assertNotEquals(order.hashCode(), differentOrder.hashCode());
  }
}

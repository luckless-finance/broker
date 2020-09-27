package org.yafa.api.dto.outbound;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yafa.api.dto.Asset;
import org.yafa.api.dto.CurrencyCode;
import org.yafa.api.dto.inbound.Order;
import org.yafa.api.dto.inbound.OrderStatus;

class OrderTest {

  Order order;
  String symbol = "FOO";
  CurrencyCode currency = CurrencyCode.CAD;
  double cashFlow = 123;
  double quantity = 12.3;
  LocalDateTime timestamp = LocalDateTime.now();

  @BeforeEach
  public void setup() {
    order =
        Order.builder()
            .orderStatus(OrderStatus.COMPLETE)
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
            .orderStatus(OrderStatus.COMPLETE)
            .asset(Asset.builder().symbol(symbol).currency(currency).build())
            .cashFlow(cashFlow)
            .quantity(quantity)
            .timestamp(timestamp)
            .build();
    Assertions.assertEquals(order, equivalentOrder);
    Assertions.assertEquals(order.hashCode(), equivalentOrder.hashCode());

    LocalDateTime differentTimestamp = LocalDateTime.now();
    Order differentOrder =
        Order.builder()
            .orderStatus(OrderStatus.COMPLETE)
            .asset(Asset.builder().symbol(symbol).currency(currency).build())
            .cashFlow(cashFlow)
            .quantity(quantity)
            .timestamp(differentTimestamp)
            .build();
    Assertions.assertNotEquals(order, differentOrder);
    Assertions.assertNotEquals(order.hashCode(), differentOrder.hashCode());
  }
}

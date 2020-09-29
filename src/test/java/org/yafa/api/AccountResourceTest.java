package org.yafa.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.wildfly.common.Assert.assertNotNull;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yafa.api.dto.Asset;
import org.yafa.api.dto.Config;
import org.yafa.api.dto.CurrencyCode;
import org.yafa.api.dto.inbound.Account;
import org.yafa.api.dto.inbound.Order;
import org.yafa.api.dto.inbound.OrderStatus;
import org.yafa.api.dto.inbound.Trade;
import org.yafa.api.dto.outbound.Holding;

@QuarkusTest
@TestHTTPEndpoint(AccountResource.class)
@Slf4j
class AccountResourceTest {

  Account account;
  org.yafa.api.dto.outbound.Account serverAccount;

  Account generateAccount() {
    return Account.builder().name("some account" + UUID.randomUUID().toString()).build();
  }

  org.yafa.api.dto.outbound.Account createAccount(Account account) {
    Response response =
        given()
            .contentType(ContentType.JSON)
            .when()
            .body(account)
            .post()
            .then()
            .statusCode(200)
            .extract()
            .response();
    return response.as(org.yafa.api.dto.outbound.Account.class);
  }

  org.yafa.api.dto.outbound.Account createAccount() {
    return createAccount(generateAccount());
  }

  @BeforeEach
  void setUpEach() {
    account = generateAccount();
    serverAccount = createAccount(account);
    assertThat(account.getName(), equalTo(serverAccount.getName()));
    assertNotNull(serverAccount.getId());
  }

  @Test
  void getAccount() {
    Response response =
        given()
            .contentType(ContentType.JSON)
            .when()
            .pathParam("accountId", serverAccount.getId())
            .get("/{accountId}")
            .then()
            .statusCode(200)
            .extract()
            .response();
    org.yafa.api.dto.outbound.Account gotServerAccount =
        response.as(org.yafa.api.dto.outbound.Account.class);
    assertThat(gotServerAccount.getName(), equalTo(serverAccount.getName()));
    assertThat(gotServerAccount.getId(), equalTo(serverAccount.getId()));
  }

  @Test
  void listAccounts() {

    org.yafa.api.dto.outbound.Account[] serverAccounts =
        new org.yafa.api.dto.outbound.Account[] {serverAccount, createAccount(), createAccount()};

    Response response =
        given()
            .contentType(ContentType.JSON)
            .when()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .response();
    List<org.yafa.api.dto.outbound.Account> accounts =
        response.body().jsonPath().getList(".", org.yafa.api.dto.outbound.Account.class);
    assertThat(accounts, containsInAnyOrder(serverAccounts));
  }

  @Test
  void listTrades() {}

  Asset generateAsset() {
    return Asset.builder().symbol("POT").currency(CurrencyCode.AED).build();
  }

  Trade generateTrade(Asset asset) {
    return Trade.builder()
        .asset(asset)
        .cashFlow(BigDecimal.valueOf(123.0))
        .quantity(BigDecimal.valueOf(10))
        .unitPrice(BigDecimal.valueOf(12.3))
        .timestamp(ZonedDateTime.now())
        .build();
  }

  Trade generateTrade() {
    return generateTrade(generateAsset());
  }

  org.yafa.api.dto.outbound.Trade createTrade(Trade trade) {
    Response response =
        given()
            .contentType(ContentType.JSON)
            .pathParam("accountId", serverAccount.getId())
            .when()
            .body(trade)
            .post("/{accountId}/trades")
            .then()
            .statusCode(200)
            .extract()
            .response();
    return response.as(org.yafa.api.dto.outbound.Trade.class);
  }

  org.yafa.api.dto.outbound.Trade createTrade() {
    return createTrade(generateTrade());
  }

  @Test
  void recordTrade() {
    Trade trade = generateTrade();
    org.yafa.api.dto.outbound.Trade serverTrade = createTrade(trade);

    assertThat(trade.getAsset(), equalTo(serverTrade.getAsset()));
    assertThat(trade.getCashFlow(), equalTo(serverTrade.getCashFlow()));
    assertThat(trade.getQuantity(), equalTo(serverTrade.getQuantity()));
    assertThat(trade.getUnitPrice(), equalTo(serverTrade.getUnitPrice()));
  }

  Order generateOrder(@NotNull Asset asset) {
    return Order.builder()
        .timestamp(LocalDateTime.now().atZone(ZoneId.of("UTC")))
        .asset(asset)
        .cashFlow(BigDecimal.valueOf(123.0))
        .quantity(BigDecimal.valueOf(10))
        .build();
  }

  Order generateOrder() {
    return generateOrder(generateAsset());
  }

  org.yafa.api.dto.outbound.Order createOrder(Order order) {
    Response response =
        given()
            .contentType(ContentType.JSON)
            .pathParam("accountId", serverAccount.getId())
            .when()
            .body(order)
            .post("/{accountId}/orders")
            .then()
            .statusCode(200)
            .extract()
            .response();
    return response.as(org.yafa.api.dto.outbound.Order.class);
  }

  org.yafa.api.dto.outbound.Order createOrder() {
    return createOrder(generateOrder());
  }

  @Test
  void submitOrder() {
    Order order = generateOrder();
    org.yafa.api.dto.outbound.Order serverOrder = createOrder(order);
    assertThat(serverOrder.getOrderStatus(), equalTo(OrderStatus.COMPLETE));
    assertThat(order.getAsset(), equalTo(serverOrder.getAsset()));
    assertThat(order.getCashFlow(), equalTo(serverOrder.getCashFlow()));
    assertThat(order.getQuantity(), equalTo(serverOrder.getQuantity()));
    assertThat(order.getTimestamp(), equalTo(serverOrder.getTimestamp()));
  }

  @Test
  void listOrders() {
    org.yafa.api.dto.outbound.Order[] serverOrders =
        new org.yafa.api.dto.outbound.Order[] {createOrder(), createOrder(), createOrder()};

    Response response =
        given()
            .contentType(ContentType.JSON)
            .pathParam("accountId", serverAccount.getId())
            .when()
            .get("/{accountId}/orders")
            .then()
            .statusCode(200)
            .extract()
            .response();
    List<org.yafa.api.dto.outbound.Order> accounts =
        response.body().jsonPath().getList(".", org.yafa.api.dto.outbound.Order.class);
    assertThat(accounts, containsInAnyOrder(serverOrders));
  }

  @Test
  void listHolding() {
    org.yafa.api.dto.outbound.Trade trade = createTrade();
    Response response =
        given()
            .contentType(ContentType.JSON)
            .pathParam("accountId", serverAccount.getId())
            .queryParam("timestamp", trade.getTimestamp().format(Config.TIME_STAMP_FORMATTER))
            .when()
            .get("/{accountId}/holdings")
            .then()
            .statusCode(200)
            .extract()
            .response();
    List<org.yafa.api.dto.outbound.Holding> holdings =
        response.body().jsonPath().getList(".", org.yafa.api.dto.outbound.Holding.class);

    assertThat(holdings, hasSize(1));
    Holding holding = holdings.get(0);
    assertThat(holding.getAsset(), equalTo(trade.getAsset()));
    assertThat(holding.getQuantity(), equalTo(trade.getQuantity()));
    assertThat(holding.getBookValue(), equalTo(trade.getCashFlow()));
  }

  @Test
  void listHoldings() {
    org.yafa.api.dto.outbound.Trade[] trades = new org.yafa.api.dto.outbound.Trade[]{createTrade(),
        createTrade(), createTrade()};
    org.yafa.api.dto.outbound.Trade lastTrade = trades[trades.length - 1];

    Response response =
        given()
            .contentType(ContentType.JSON)
            .pathParam("accountId", serverAccount.getId())
            .queryParam("timestamp", lastTrade.getTimestamp().format(Config.TIME_STAMP_FORMATTER))
            .when()
            .get("/{accountId}/holdings")
            .then()
            .statusCode(200)
            .extract()
            .response();
    List<org.yafa.api.dto.outbound.Holding> holdings =
        response.body().jsonPath().getList(".", org.yafa.api.dto.outbound.Holding.class);

    assertThat(holdings, hasSize(1));
    Holding holding = holdings.get(0);
    assertThat(holding.getAsset(), equalTo(lastTrade.getAsset()));
    assertThat(holding.getQuantity(),
        equalTo(lastTrade.getQuantity().multiply(BigDecimal.valueOf(trades.length))));
    assertThat(holding.getBookValue(),
        equalTo(lastTrade.getCashFlow().multiply(BigDecimal.valueOf(trades.length))));
  }
}

package org.yafa.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.wildfly.common.Assert.assertNotNull;
import static org.wildfly.common.Assert.assertTrue;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response.Status;
import lombok.extern.slf4j.Slf4j;
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


  @Test
  void create() {
    Account account = generateAccount();
    org.yafa.api.dto.outbound.Account serverAccount = createAccount(account);
    assertThat(account.getName(), equalTo(serverAccount.getName()));
    assertNotNull(serverAccount.getId());
  }

  @Test
  void accountNotFound() {
    given()
        .contentType(ContentType.JSON)
        .when()
        .pathParam("accountId", "does not exist")
        .get("/{accountId}")
        .then()
        .statusCode(Status.NOT_FOUND.getStatusCode());
  }

  @Test
  void accountConflict() {
    Account account = generateAccount();
    org.yafa.api.dto.outbound.Account serverAccount = createAccount(account);
    given()
        .contentType(ContentType.JSON)
        .when()
        .body(account)
        .post()
        .then()
        .statusCode(Status.CONFLICT.getStatusCode());
  }

  @Test
  void getAccount() {

    Account account = generateAccount();
    org.yafa.api.dto.outbound.Account serverAccount = createAccount(account);
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

    org.yafa.api.dto.outbound.Account[] createdAccounts =
        new org.yafa.api.dto.outbound.Account[]{createAccount(), createAccount(), createAccount()};

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
    assertTrue(accounts.containsAll(Arrays.asList(createdAccounts)));
    // TODO: why does this fail? but above passes?
    //  assertThat(accounts, containsInAnyOrder(Arrays.asList(createdAccounts)));
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

  org.yafa.api.dto.outbound.Trade createTrade(org.yafa.api.dto.outbound.Account serverAccount,
      Trade trade) {

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
    return createTrade(createAccount(), generateTrade());
  }

  @Test
  void recordTrade() {
    org.yafa.api.dto.outbound.Account serverAccount = createAccount();
    Trade trade = generateTrade();
    org.yafa.api.dto.outbound.Trade serverTrade = createTrade(serverAccount, trade);

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

  org.yafa.api.dto.outbound.Order createOrder(org.yafa.api.dto.outbound.Account serverAccount,
      Order order) {
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
    return createOrder(createAccount(), generateOrder());
  }

  @Test
  void submitOrder() {
    Order order = generateOrder();
    org.yafa.api.dto.outbound.Order serverOrder = createOrder(createAccount(), order);
    assertThat(serverOrder.getOrderStatus(), equalTo(OrderStatus.COMPLETE));
    assertThat(order.getAsset(), equalTo(serverOrder.getAsset()));
    assertThat(order.getCashFlow(), equalTo(serverOrder.getCashFlow()));
    assertThat(order.getQuantity(), equalTo(serverOrder.getQuantity()));
    assertThat(order.getTimestamp(), equalTo(serverOrder.getTimestamp()));
  }

  @Test
  void listOrders() {

    Account account = generateAccount();
    org.yafa.api.dto.outbound.Account serverAccount = createAccount(account);

    org.yafa.api.dto.outbound.Order[] serverOrders =
        new org.yafa.api.dto.outbound.Order[]{
            createOrder(serverAccount, generateOrder()),
            createOrder(serverAccount, generateOrder()),
            createOrder(serverAccount, generateOrder())};

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

    Account account = generateAccount();
    org.yafa.api.dto.outbound.Account serverAccount = createAccount(account);

    org.yafa.api.dto.outbound.Trade trade = createTrade(serverAccount, generateTrade());
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
    Account account = generateAccount();
    org.yafa.api.dto.outbound.Account serverAccount = createAccount(account);
    Asset assetPOT = Asset.builder()
        .symbol("POT")
        .currency(CurrencyCode.AED)
        .build();
    Asset assetABC = Asset.builder()
        .symbol("ABC")
        .currency(CurrencyCode.AED)
        .build();

    org.yafa.api.dto.outbound.Trade[] trades = new org.yafa.api.dto.outbound.Trade[]{
        createTrade(serverAccount, generateTrade(assetABC)),
        createTrade(serverAccount, generateTrade(assetPOT)),
        createTrade(serverAccount, generateTrade(assetABC))
    };

    List<org.yafa.api.dto.outbound.Trade> abcTrades = Arrays.asList(trades[0], trades[2]);
    List<org.yafa.api.dto.outbound.Trade> potTrades = Arrays.asList(trades[1]);
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

    assertThat(holdings, hasSize(2));
    Holding holdingABC = holdings.stream()
        .filter(holding -> holding.getAsset().equals(assetABC)).findFirst().get();
    assertThat(holdingABC.getAsset(),
        equalTo(abcTrades.get(0).getAsset()));
    assertThat(holdingABC.getQuantity(),
        equalTo(abcTrades.get(0).getQuantity().multiply(BigDecimal.valueOf(abcTrades.size()))));
    assertThat(holdingABC.getBookValue(),
        equalTo(abcTrades.get(0).getCashFlow().multiply(BigDecimal.valueOf(abcTrades.size()))));

    Holding holdingPOT = holdings.stream()
        .filter(holding -> holding.getAsset().equals(assetPOT)).findFirst().get();
    assertThat(holdingPOT.getAsset(),
        equalTo(potTrades.get(0).getAsset()));
    assertThat(holdingPOT.getQuantity(),
        equalTo(potTrades.get(0).getQuantity().multiply(BigDecimal.valueOf(potTrades.size()))));
    assertThat(holdingPOT.getBookValue(),
        equalTo(potTrades.get(0).getCashFlow().multiply(BigDecimal.valueOf(potTrades.size()))));
  }
}

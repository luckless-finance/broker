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
import org.yafa.api.dto.inbound.ClientSideAccount;
import org.yafa.api.dto.inbound.ClientSideOrder;
import org.yafa.api.dto.inbound.ClientSideTrade;
import org.yafa.api.dto.inbound.OrderStatus;
import org.yafa.api.dto.outbound.Holding;
import org.yafa.api.dto.outbound.ServerSideAccount;
import org.yafa.api.dto.outbound.ServerSideOrder;
import org.yafa.api.dto.outbound.ServerSideTrade;

@QuarkusTest
@TestHTTPEndpoint(AccountsResource.class)
@Slf4j
class AccountsResourceTest {


  ClientSideAccount generateAccount() {
    return ClientSideAccount.builder().name("some account" + UUID.randomUUID().toString()).build();
  }

  ServerSideAccount createAccount(ClientSideAccount clientSideAccount) {
    Response response =
        given()
            .contentType(ContentType.JSON)
            .when()
            .body(clientSideAccount)
            .post()
            .then()
            .statusCode(200)
            .extract()
            .response();
    return response.as(ServerSideAccount.class);
  }

  ServerSideAccount createAccount() {
    return createAccount(generateAccount());
  }


  @Test
  void create() {
    ClientSideAccount clientSideAccount = generateAccount();
    ServerSideAccount serverSideAccount = createAccount(clientSideAccount);
    assertThat(clientSideAccount.getName(), equalTo(serverSideAccount.getName()));
    assertNotNull(serverSideAccount.getId());
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
    ClientSideAccount clientSideAccount = generateAccount();
    ServerSideAccount serverSideAccount = createAccount(clientSideAccount);
    given()
        .contentType(ContentType.JSON)
        .when()
        .body(clientSideAccount)
        .post()
        .then()
        .statusCode(Status.CONFLICT.getStatusCode());
  }

  @Test
  void getAccount() {

    ClientSideAccount clientSideAccount = generateAccount();
    ServerSideAccount serverSideAccount = createAccount(clientSideAccount);
    Response response =
        given()
            .contentType(ContentType.JSON)
            .when()
            .pathParam("accountId", serverSideAccount.getId())
            .get("/{accountId}")
            .then()
            .statusCode(200)
            .extract()
            .response();
    ServerSideAccount gotServerSideAccount =
        response.as(ServerSideAccount.class);
    assertThat(gotServerSideAccount.getName(), equalTo(serverSideAccount.getName()));
    assertThat(gotServerSideAccount.getId(), equalTo(serverSideAccount.getId()));
  }

  @Test
  void listAccounts() {

    ServerSideAccount[] serverSideAccounts =
        new ServerSideAccount[]{createAccount(), createAccount(), createAccount()};

    Response response =
        given()
            .contentType(ContentType.JSON)
            .when()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .response();
    List<ServerSideAccount> gotServerSideAccounts =
        response.body().jsonPath().getList(".", ServerSideAccount.class);
    assertTrue(gotServerSideAccounts.containsAll(Arrays.asList(serverSideAccounts)));
    // TODO: why does this fail? but above passes?
    //  assertThat(accounts, containsInAnyOrder(Arrays.asList(createdAccounts)));
  }

  @Test
  void listTrades() {
  }

  Asset generateAsset() {
    return Asset.builder().symbol("POT").currency(CurrencyCode.AED).build();
  }

  ClientSideTrade generateTrade(Asset asset) {
    return ClientSideTrade.builder()
        .asset(asset)
        .cashFlow(BigDecimal.valueOf(123.0))
        .quantity(BigDecimal.valueOf(10))
        .unitPrice(BigDecimal.valueOf(12.3))
        .timestamp(ZonedDateTime.now())
        .build();
  }

  ClientSideTrade generateTrade() {
    return generateTrade(generateAsset());
  }

  ServerSideTrade createTrade(ServerSideAccount serverSideAccount,
      ClientSideTrade clientSideTrade) {

    Response response =
        given()
            .contentType(ContentType.JSON)
            .pathParam("accountId", serverSideAccount.getId())
            .when()
            .body(clientSideTrade)
            .post("/{accountId}/trades")
            .then()
            .statusCode(200)
            .extract()
            .response();
    return response.as(ServerSideTrade.class);
  }

  ServerSideTrade createTrade() {
    return createTrade(createAccount(), generateTrade());
  }

  @Test
  void recordTrade() {
    ServerSideAccount serverSideAccount = createAccount();
    ClientSideTrade clientSideTrade = generateTrade();
    ServerSideTrade serverTrade = createTrade(serverSideAccount, clientSideTrade);

    assertThat(clientSideTrade.getAsset(), equalTo(serverTrade.getAsset()));
    assertThat(clientSideTrade.getCashFlow(), equalTo(serverTrade.getCashFlow()));
    assertThat(clientSideTrade.getQuantity(), equalTo(serverTrade.getQuantity()));
    assertThat(clientSideTrade.getUnitPrice(), equalTo(serverTrade.getUnitPrice()));
  }

  ClientSideOrder generateOrder(@NotNull Asset asset) {
    return ClientSideOrder.builder()
        .timestamp(LocalDateTime.now().atZone(ZoneId.of("UTC")))
        .asset(asset)
        .cashFlow(BigDecimal.valueOf(123.0))
        .quantity(BigDecimal.valueOf(10))
        .build();
  }

  ClientSideOrder generateOrder() {
    return generateOrder(generateAsset());
  }

  ServerSideOrder createOrder(ServerSideAccount serverSideAccount,
      ClientSideOrder clientSideOrder) {
    Response response =
        given()
            .contentType(ContentType.JSON)
            .pathParam("accountId", serverSideAccount.getId())
            .when()
            .body(clientSideOrder)
            .post("/{accountId}/orders")
            .then()
            .statusCode(200)
            .extract()
            .response();
    return response.as(ServerSideOrder.class);
  }

  ServerSideOrder createOrder() {
    return createOrder(createAccount(), generateOrder());
  }

  @Test
  void submitOrder() {
    ClientSideOrder clientSideOrder = generateOrder();
    ServerSideOrder serverSideOrder = createOrder(createAccount(), clientSideOrder);
    assertThat(serverSideOrder.getOrderStatus(), equalTo(OrderStatus.COMPLETE));
    assertThat(clientSideOrder.getAsset(), equalTo(serverSideOrder.getAsset()));
    assertThat(clientSideOrder.getCashFlow(), equalTo(serverSideOrder.getCashFlow()));
    assertThat(clientSideOrder.getQuantity(), equalTo(serverSideOrder.getQuantity()));
    assertThat(clientSideOrder.getTimestamp(), equalTo(serverSideOrder.getTimestamp()));
  }

  @Test
  void listOrders() {

    ClientSideAccount clientSideAccount = generateAccount();
    ServerSideAccount serverSideAccount = createAccount(clientSideAccount);

    ServerSideOrder[] serverSideOrders =
        new ServerSideOrder[]{
            createOrder(serverSideAccount, generateOrder()),
            createOrder(serverSideAccount, generateOrder()),
            createOrder(serverSideAccount, generateOrder())};

    Response response =
        given()
            .contentType(ContentType.JSON)
            .pathParam("accountId", serverSideAccount.getId())
            .when()
            .get("/{accountId}/orders")
            .then()
            .statusCode(200)
            .extract()
            .response();
    List<ServerSideOrder> accounts =
        response.body().jsonPath().getList(".", ServerSideOrder.class);
    assertThat(accounts, containsInAnyOrder(serverSideOrders));
  }

  @Test
  void listHolding() {

    ClientSideAccount clientSideAccount = generateAccount();
    ServerSideAccount serverSideAccount = createAccount(clientSideAccount);

    ServerSideTrade trade = createTrade(serverSideAccount, generateTrade());
    Response response =
        given()
            .contentType(ContentType.JSON)
            .pathParam("accountId", serverSideAccount.getId())
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
    ClientSideAccount clientSideAccount = generateAccount();
    ServerSideAccount serverSideAccount = createAccount(clientSideAccount);
    Asset assetPOT = Asset.builder()
        .symbol("POT")
        .currency(CurrencyCode.AED)
        .build();
    Asset assetABC = Asset.builder()
        .symbol("ABC")
        .currency(CurrencyCode.AED)
        .build();

    ServerSideTrade[] trades = new ServerSideTrade[]{
        createTrade(serverSideAccount, generateTrade(assetABC)),
        createTrade(serverSideAccount, generateTrade(assetPOT)),
        createTrade(serverSideAccount, generateTrade(assetABC))
    };

    List<ServerSideTrade> abcTrades = Arrays.asList(trades[0], trades[2]);
    List<ServerSideTrade> potTrades = Arrays.asList(trades[1]);
    ServerSideTrade lastTrade = trades[trades.length - 1];
    Response response =
        given()
            .contentType(ContentType.JSON)
            .pathParam("accountId", serverSideAccount.getId())
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

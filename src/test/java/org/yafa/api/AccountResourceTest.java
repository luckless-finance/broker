package org.yafa.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.wildfly.common.Assert.assertNotNull;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yafa.api.dto.Asset;
import org.yafa.api.dto.CurrencyCode;
import org.yafa.api.dto.inbound.Account;
import org.yafa.api.dto.inbound.Trade;

@QuarkusTest
@TestHTTPEndpoint(AccountResource.class)
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
  void listTrades() {
    Trade trade =
        Trade.builder()
            .asset(Asset.builder().currency(CurrencyCode.AFN).symbol("POT").build())
            .cashFlow(123.0)
            .quantity(12.30)
            .timestamp(LocalDateTime.now())
            .build();
    //
    //    Response response =
    //        given()
    //            .contentType(ContentType.JSON)
    //            .pathParam("accountId", serverAccount.getId())
    //            .when()
    //            .body(trade)
    //            .post("/{accountId}/trades")
    //            .then()
    //            .statusCode(200)
    //            .extract()
    //            .response();
    //    response.as(org.yafa.api.dto.outbound.Trade.class);

  }

  @Test
  void listHoldings() {}

  @Test
  void submitOrder() {}

  @Test
  void listOrders() {}
}

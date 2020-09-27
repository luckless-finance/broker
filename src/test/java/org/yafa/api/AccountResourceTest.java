package org.yafa.api;

import static io.restassured.RestAssured.given;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yafa.api.dto.inbound.Account;

@QuarkusTest
@TestHTTPEndpoint(AccountResource.class)
class AccountResourceTest {

  Account account = Account.builder().name("some account").build();

  @BeforeEach
  void setUp() {
  }

  @Test
  void create() {

  }

  @Test
  void getAccount() {
  }

  @Test
  void listAccounts() {
    Response response = given().contentType(ContentType.JSON).when()
        .get()
        .then()
        .statusCode(200)
        .extract()
        .response();

  }

  @Test
  void listTrades() {
  }

  @Test
  void listHoldings() {
  }

  @Test
  void submitOrder() {
  }

  @Test
  void listOrders() {
  }
}
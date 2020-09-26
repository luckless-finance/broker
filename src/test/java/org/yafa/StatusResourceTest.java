package org.yafa;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.yafa.api.dto.ServerStatus;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class StatusResourceTest {

  @Test
  public void testHelloEndpoint() {
    Response response = given().when().get("/status").then().statusCode(200).extract().response();
    ServerStatus serverStatus = response.as(ServerStatus.class);
    assertEquals(ServerStatus.Status.OK, serverStatus.getStatus());
  }
}

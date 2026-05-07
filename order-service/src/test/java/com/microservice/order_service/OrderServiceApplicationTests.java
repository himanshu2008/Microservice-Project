package com.microservice.order_service;

import static org.junit.Assert.assertEquals;

import java.time.Duration;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;

import com.microservice.order_service.stubs.InventoryClientStub;

import io.restassured.RestAssured;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class OrderServiceApplicationTests {

	@ServiceConnection
	static MySQLContainer mySQLContainer = (MySQLContainer) new MySQLContainer<>("mysql:8.3.0")
													.withDatabaseName("order_service")
													.withUsername("root")
													.withPassword("mysql")
													.withStartupTimeout(Duration.ofMinutes(2));

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	// static {
	// 	mySQLContainer.start();
	// }

	@Test
	void shouldPlaceOrder() {
		String placeOrderJson = """
				{
					"skuCode": "iPhone 17",
					"price": 90000,
					"quantity": 1
				}
				""";

		InventoryClientStub.stubInventoryCall("iPhone 17", 1);

		var responseBodyString = RestAssured.given()
				.contentType("application/json")
				.body(placeOrderJson)
				.when()
				.post("/api/order")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().asString();

		assertEquals(responseBodyString, Matchers.is("Order Placed Successfully"));
	}

}

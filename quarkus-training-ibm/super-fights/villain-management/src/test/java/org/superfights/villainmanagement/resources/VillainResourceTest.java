package org.superfights.villainmanagement.resources;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@QuarkusTestResource(DatabaseResource.class)
public class VillainResourceTest {

	@Test
	public void testHelloEndpoint() {
		given().when().get("/api/villains/hello").then().statusCode(200).body(is("Hello Villains!"));
	}

}
package org.superfights.fightmanagement;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Random;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.superfights.fightmanagement.dtos.Fighters;
import org.superfights.fightmanagement.dtos.Hero;
import org.superfights.fightmanagement.dtos.Villain;
import org.superfights.fightmanagement.entities.Fight;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;

@QuarkusTest
@QuarkusTestResource(DatabaseResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FightResourceTest {

	private static final String DEFAULT_WINNER_NAME = "Super Baguette";
	private static final String DEFAULT_WINNER_PICTURE = "super_baguette.png";
	private static final int DEFAULT_WINNER_LEVEL = 42;
	private static final String DEFAULT_LOSER_NAME = "Super Chocolatine";
	private static final String DEFAULT_LOSER_PICTURE = "super_chocolatine.png";
	private static final int DEFAULT_LOSER_LEVEL = 6;

	private static final int NB_FIGHTS = 10;
	private static String fightId;

	@Test
	public void testHelloEndpoint() {
		given().when().get("/api/fights/hello").then().statusCode(200).body(is("Hello Fights!"));
	}

	@Test
	void shouldNotGetUnknownFight() {
		Long randomId = new Random().nextLong();
		given().pathParam("id", randomId).when().get("/api/fights/{id}").then().statusCode(NO_CONTENT.getStatusCode());
	}

	@Test
	void shouldNotAddInvalidItem() {
		Fighters fighters = new Fighters();
		fighters.hero = null;
		fighters.villain = null;

		given().body(fighters).header(CONTENT_TYPE, APPLICATION_JSON).header(ACCEPT, APPLICATION_JSON).when()
				.post("/api/fights").then().statusCode(BAD_REQUEST.getStatusCode());
	}

	@Test
	@Order(1)
	void shouldGetInitialItems() {
		List<Fight> fights = get("/api/fights").then().statusCode(OK.getStatusCode())
				.header(CONTENT_TYPE, APPLICATION_JSON).extract().body().as(getFightTypeRef());
		assertEquals(NB_FIGHTS, fights.size());
	}

	@Test
	@Order(2)
	void shouldAddAnItem() {
		Hero hero = new Hero();
		hero.name = DEFAULT_WINNER_NAME;
		hero.picture = DEFAULT_WINNER_PICTURE;
		hero.level = DEFAULT_WINNER_LEVEL;
		Villain villain = new Villain();
		villain.name = DEFAULT_LOSER_NAME;
		villain.picture = DEFAULT_LOSER_PICTURE;
		villain.level = DEFAULT_LOSER_LEVEL;
		Fighters fighters = new Fighters();
		fighters.hero = hero;
		fighters.villain = villain;

		fightId = given().body(fighters).header(CONTENT_TYPE, APPLICATION_JSON).header(ACCEPT, APPLICATION_JSON).when()
				.post("/api/fights").then().statusCode(OK.getStatusCode())
				.body(containsString("winner"), containsString("loser")).extract().body().jsonPath().getString("id");

		assertNotNull(fightId);

		given().pathParam("id", fightId).when().get("/api/fights/{id}").then().statusCode(OK.getStatusCode())
				.header(CONTENT_TYPE, APPLICATION_JSON).body("winnerName", Is.is(DEFAULT_WINNER_NAME))
				.body("winnerPicture", Is.is(DEFAULT_WINNER_PICTURE)).body("winnerLevel", Is.is(DEFAULT_WINNER_LEVEL))
				.body("loserName", Is.is(DEFAULT_LOSER_NAME)).body("loserPicture", Is.is(DEFAULT_LOSER_PICTURE))
				.body("loserLevel", Is.is(DEFAULT_LOSER_LEVEL)).body("fightDate", Is.is(notNullValue()));

		List<Fight> fights = get("/api/fights").then().statusCode(OK.getStatusCode())
				.header(CONTENT_TYPE, APPLICATION_JSON).extract().body().as(getFightTypeRef());
		assertEquals(NB_FIGHTS + 1, fights.size());
	}

	@Test
	void shouldPingOpenAPI() {
		given().header(ACCEPT, APPLICATION_JSON).when().get("/openapi").then().statusCode(OK.getStatusCode());
	}

	@Test
	void shouldPingSwaggerUI() {
		given().when().get("/swagger-ui").then().statusCode(OK.getStatusCode());
	}

	private TypeRef<List<Fight>> getFightTypeRef() {
		return new TypeRef<List<Fight>>() {
			// Kept empty on purpose
		};
	}

}
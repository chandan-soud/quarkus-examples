package org.superfights.heromanagement.resources;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.logging.Logger;
import org.superfights.heromanagement.entities.Hero;
import org.superfights.heromanagement.services.HeroService;

@Tag(name = "heroes", description = "apis for hero resource")
@Path("/api/heroes")
@Produces(MediaType.APPLICATION_JSON)
public class HeroResource {

	private static final Logger LOGGER = Logger.getLogger(HeroResource.class);

	@Inject
	HeroService heroService;

	@Operation(summary = "Returns \"Hello Heroes!\" string, serves as a dummy api")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.TEXT_PLAIN, schema = @Schema(implementation = String.class)))
	@Tag(ref = "generic")
	@GET
	@Path("/hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {
		return "Hello Heroes!";
	}

	@Operation(summary = "Returns a random hero")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Hero.class, required = true)))
	@GET
	@Path("/random")
	public Response getRandomHero() {
		Hero hero = heroService.findRandomHero();
		LOGGER.debug("Found random hero " + hero);
		return Response.ok(hero).build();
	}

	@Operation(summary = "Returns all the heroes from the database")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Hero.class, type = SchemaType.ARRAY)))
	@APIResponse(responseCode = "204", description = "No heroes")
	@GET
	public Response getAllHeroes() {
		List<Hero> heroes = heroService.findAllHeroes();
		LOGGER.debug("All heroes " + heroes);
		return Response.ok(heroes).build();
	}

	@Operation(summary = "Returns a hero for a given identifier")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Hero.class)))
	@APIResponse(responseCode = "204", description = "The hero is not found for a given identifier")
	@GET
	@Path("/{id}")
	public Response getHero(@PathParam("id") Long id) {
		Hero hero = heroService.findHeroById(id);
		if (hero != null) {
			LOGGER.debug("Found hero " + hero);
			return Response.ok(hero).build();
		} else {
			LOGGER.debug("No hero found with id " + id);
			return Response.noContent().build();
		}
	}

	@Operation(summary = "Creates a valid hero")
	@APIResponse(responseCode = "201", description = "The URI of the created hero", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = URI.class)))
	@POST
	public Response createHero(@Valid Hero hero, @Context UriInfo uriInfo) {
		hero = heroService.persistHero(hero);
		UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(hero.id));
		LOGGER.debug("New hero created with URI " + builder.build().toString());
		return Response.created(builder.build()).build();
	}

	@Operation(summary = "Updates an exiting  hero")
	@APIResponse(responseCode = "200", description = "The updated hero", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Hero.class)))
	@PUT
	public Response updateHero(@Valid Hero hero) {
		hero = heroService.updateHero(hero);
		LOGGER.debug("Hero updated with new values " + hero);
		return Response.ok(hero).build();
	}

	@Operation(summary = "Deletes an exiting hero")
	@APIResponse(responseCode = "204")
	@DELETE
	@Path("/{id}")
	public Response deleteHero(@PathParam("id") Long id) {
		heroService.deleteHero(id);
		LOGGER.debug("Hero deleted with id " + id);
		return Response.noContent().build();
	}

}
package org.superfights.heromanagement.resources;

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

import org.jboss.logging.Logger;
import org.superfights.heromanagement.entities.Hero;
import org.superfights.heromanagement.services.HeroService;

@Path("/api/heroes")
@Produces(MediaType.APPLICATION_JSON)
public class HeroResource {

	private static final Logger LOGGER = Logger.getLogger(HeroResource.class);

	@Inject
	HeroService heroService;

	@GET
	@Path("/hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {
		return "Hello Heroes!";
	}

	@GET
	@Path("/random")
	public Response getRandomHero() {
		Hero hero = heroService.findRandomHero();
		LOGGER.debug("Found random hero " + hero);
		return Response.ok(hero).build();
	}

	@GET
	public Response getAllHeroes() {
		List<Hero> heroes = heroService.findAllHeroes();
		LOGGER.debug("All heroes " + heroes);
		return Response.ok(heroes).build();
	}

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

	@POST
	public Response createHero(@Valid Hero hero, @Context UriInfo uriInfo) {
		hero = heroService.persistHero(hero);
		UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(hero.id));
		LOGGER.debug("New hero created with URI " + builder.build().toString());
		return Response.created(builder.build()).build();
	}

	@PUT
	public Response updateHero(@Valid Hero hero) {
		hero = heroService.updateHero(hero);
		LOGGER.debug("Hero updated with new values " + hero);
		return Response.ok(hero).build();
	}

	@DELETE
	@Path("/{id}")
	public Response deleteHero(@PathParam("id") Long id) {
		heroService.deleteHero(id);
		LOGGER.debug("Hero deleted with id " + id);
		return Response.noContent().build();
	}

}
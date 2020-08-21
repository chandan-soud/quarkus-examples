package org.superfights.villainmanagement.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

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
import org.superfights.villainmanagement.entities.Villain;
import org.superfights.villainmanagement.services.VillainService;

@Path("/api/villains")
@Produces(APPLICATION_JSON)
public class VillainResource {

	private static final Logger LOGGER = Logger.getLogger(VillainResource.class);

	@Inject
	VillainService villainService;

	@GET
	@Path("/hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {
		return "Hello Villains!";
	}

	@GET
	@Path("/random")
	public Response getRandomVillain() {
		Villain villain = villainService.findRandomVillain();
		LOGGER.debug("Found random villain " + villain);
		return Response.ok(villain).build();
	}

	@GET
	public Response getAllVillains() {
		List<Villain> villains = villainService.findAllVillains();
		LOGGER.debug("All villains " + villains);
		return Response.ok(villains).build();
	}

	@GET
	@Path("/{id}")
	public Response getVillain(@PathParam("id") Long id) {
		Villain villain = villainService.findVillainById(id);
		if (villain != null) {
			LOGGER.debug("Found villain " + villain);
			return Response.ok(villain).build();
		} else {
			LOGGER.debug("No villain found with id " + id);
			return Response.noContent().build();
		}
	}

	@POST
	public Response createVillain(@Valid Villain villain, @Context UriInfo uriInfo) {
		villain = villainService.persistVillain(villain);
		UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(villain.id));
		LOGGER.debug("New villain created with URI " + builder.build().toString());
		return Response.created(builder.build()).build();
	}

	@PUT
	public Response updateVillain(@Valid Villain villain) {
		villain = villainService.updateVillain(villain);
		LOGGER.debug("Villain updated with new values " + villain);
		return Response.ok(villain).build();
	}

	@DELETE
	@Path("/{id}")
	public Response deleteVillain(@PathParam("id") Long id) {
		villainService.deleteVillain(id);
		LOGGER.debug("Villain deleted with id " + id);
		return Response.noContent().build();
	}
}
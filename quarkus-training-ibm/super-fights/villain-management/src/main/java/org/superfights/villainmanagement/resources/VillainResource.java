package org.superfights.villainmanagement.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.superfights.villainmanagement.entities.Villain;
import org.superfights.villainmanagement.services.VillainService;

@Tag(ref = "villains")
@Path("/api/villains")
@Produces(APPLICATION_JSON)
public class VillainResource {

	private static final Logger LOGGER = Logger.getLogger(VillainResource.class);

	@Inject
	VillainService villainService;

	@Operation(summary = "Returns \"Hello Villains!\" string, serves as a dummy api")
	@APIResponse(responseCode = "200", content = @Content(mediaType = TEXT_PLAIN, schema = @Schema(implementation = String.class)))
	@Tag(ref = "generic")
	@GET
	@Path("/hello")
	@Produces(TEXT_PLAIN)
	public String hello() {
		return "Hello Villains!";
	}

	@Operation(summary = "Returns a random villain")
	@APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class, required = true)))
	@GET
	@Path("/random")
	public Response getRandomVillain() {
		Villain villain = villainService.findRandomVillain();
		LOGGER.debug("Found random villain " + villain);
		return Response.ok(villain).build();
	}

	@Operation(summary = "Returns all the villains from the database")
	@APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class, type = SchemaType.ARRAY)))
	@APIResponse(responseCode = "204", description = "No villains")
	@GET
	public Response getAllVillains() {
		List<Villain> villains = villainService.findAllVillains();
		LOGGER.debug("All villains " + villains);
		return Response.ok(villains).build();
	}

	@Operation(summary = "Returns a villain for a given identifier")
	@APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class)))
	@APIResponse(responseCode = "204", description = "The villain is not found for a given identifier")
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

	@Operation(summary = "Creates a valid villain")
	@APIResponse(responseCode = "201", description = "The URI of the created villain", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = URI.class)))
	@POST
	public Response createVillain(@Valid Villain villain, @Context UriInfo uriInfo) {
		villain = villainService.persistVillain(villain);
		UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(villain.id));
		LOGGER.debug("New villain created with URI " + builder.build().toString());
		return Response.created(builder.build()).build();
	}

	@Operation(summary = "Updates an exiting  villain")
	@APIResponse(responseCode = "200", description = "The updated villain", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class)))
	@PUT
	public Response updateVillain(@Valid Villain villain) {
		villain = villainService.updateVillain(villain);
		LOGGER.debug("Villain updated with new values " + villain);
		return Response.ok(villain).build();
	}

	@Operation(summary = "Deletes an exiting villain")
	@APIResponse(responseCode = "204")
	@DELETE
	@Path("/{id}")
	public Response deleteVillain(@PathParam("id") Long id) {
		villainService.deleteVillain(id);
		LOGGER.debug("Villain deleted with id " + id);
		return Response.noContent().build();
	}
}
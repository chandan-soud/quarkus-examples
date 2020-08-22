package org.superfights.fightmanagement.clients;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.superfights.fightmanagement.dtos.Villain;

@Path("/api/villains")
@Produces(APPLICATION_JSON)
@RegisterRestClient
public interface VillainApiClient {

	@GET
	@Path("/random")
	public Villain getRandomVillain();

}

package org.superfights.fightmanagement.clients;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.superfights.fightmanagement.dtos.Hero;

@Path("/api/heroes")
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient
public interface HeroApiClient {

	@GET
	@Path("/random")
	public Hero getRandomHero();

}

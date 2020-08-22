package org.superfights.fightmanagement;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api/fights")
public class FightResource {

	@GET
	@Path("/hello")
	@Produces(TEXT_PLAIN)
	public String hello() {
		return "Hello Fights!";
	}
}
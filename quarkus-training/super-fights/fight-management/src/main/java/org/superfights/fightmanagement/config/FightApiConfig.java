package org.superfights.fightmanagement.config;

import org.eclipse.microprofile.openapi.annotations.ExternalDocumentation;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
@OpenAPIDefinition(info = @Info(title = "Fight API", description = "This API allows a hero and a villain to fight", version = "1.0.0", contact = @Contact(name = "Quarkus", url = "https://github.com/quarkusio")), servers = {
		@Server(url = "http://localhost:8082") }, externalDocs = @ExternalDocumentation(url = "https://github.com/quarkusio/quarkus-workshops", description = "All the Quarkus workshops"), tags = {
				@Tag(name = "generic", description = "apis for generic testing"),
				@Tag(name = "fights", description = "apis for fight resource"),
				@Tag(name = "superheroes", description = "Well, superhero fights") })
public class FightApiConfig extends Application {
}

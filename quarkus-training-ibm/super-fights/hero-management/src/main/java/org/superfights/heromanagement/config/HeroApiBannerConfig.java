package org.superfights.heromanagement.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.jboss.logging.Logger;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;

@ApplicationScoped
public class HeroApiBannerConfig {

	private static final Logger LOGGER = Logger.getLogger(HeroApiBannerConfig.class);

	void onStart(@Observes StartupEvent startupEvent) {
		LOGGER.info("  _   _                      _    ____ ___ ");
		LOGGER.info(" | | | | ___ _ __ ___       / \\  |  _ \\_ _|");
		LOGGER.info(" | |_| |/ _ \\ '__/ _ \\     / _ \\ | |_) | | ");
		LOGGER.info(" |  _  |  __/ | | (_) |   / ___ \\|  __/| | ");
		LOGGER.info(" |_| |_|\\___|_|  \\___/   /_/   \\_\\_|  |___|");
		LOGGER.info("                         Powered by Quarkus");
		LOGGER.infof("The application hero-management is starting with profile `%s`",
				ProfileManager.getActiveProfile());
	}

	void onStop(@Observes ShutdownEvent shutdownEvent) {
		LOGGER.info("The application hero-management is stopping...");
	}

}

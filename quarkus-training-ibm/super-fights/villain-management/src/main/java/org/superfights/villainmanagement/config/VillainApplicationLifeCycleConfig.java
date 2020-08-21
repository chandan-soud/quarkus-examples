package org.superfights.villainmanagement.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.jboss.logging.Logger;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;

@ApplicationScoped
public class VillainApplicationLifeCycleConfig {

	private static final Logger LOGGER = Logger.getLogger(VillainApplicationLifeCycleConfig.class);

	void onStart(@Observes StartupEvent startupEvent) {
		LOGGER.info(" __     ___ _ _       _             _    ____ ___ ");
		LOGGER.info(" \\ \\   / (_) | | __ _(_)_ __       / \\  |  _ \\_ _|");
		LOGGER.info("  \\ \\ / /| | | |/ _` | | '_ \\     / _ \\ | |_) | | ");
		LOGGER.info("   \\ V / | | | | (_| | | | | |   / ___ \\|  __/| | ");
		LOGGER.info("    \\_/  |_|_|_|\\__,_|_|_| |_|  /_/   \\_\\_|  |___|");
		LOGGER.info("                         Powered by Quarkus");
		LOGGER.infof("The application villain-management is starting with profile `%s`",
				ProfileManager.getActiveProfile());
	}

	void onStop(@Observes ShutdownEvent shutdownEvent) {
		LOGGER.info("The application villain-management is stopping...");
	}

}

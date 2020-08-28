package org.superfights.fightmanagement.services;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.time.Instant;
import java.util.List;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.superfights.fightmanagement.clients.HeroApiClient;
import org.superfights.fightmanagement.clients.VillainApiClient;
import org.superfights.fightmanagement.dtos.Fighters;
import org.superfights.fightmanagement.dtos.Hero;
import org.superfights.fightmanagement.dtos.Villain;
import org.superfights.fightmanagement.entities.Fight;

@ApplicationScoped
@Transactional(SUPPORTS)
public class FightService {

	private static final Logger LOGGER = Logger.getLogger(FightService.class);

	private final Random random = new Random();

	@Inject
	@RestClient
	HeroApiClient heroApiClient;

	@Inject
	@RestClient
	VillainApiClient villainApiClient;

	@Inject
	@Channel("fights")
	Emitter<Fight> fightEmitter;

	public List<Fight> findAllFights() {
		return Fight.listAll();
	}

	public Fight findFightById(Long id) {
		return Fight.findById(id);
	}

	@Transactional(REQUIRED)
	public Fight persistFight(Fighters fighters) {

		Fight fight;

		int heroAdjust = random.nextInt(20);
		int villainAdjust = random.nextInt(20);

		if ((fighters.hero.level + heroAdjust) > (fighters.villain.level + villainAdjust)) {
			fight = heroWon(fighters);
		} else if (fighters.hero.level < fighters.villain.level) {
			fight = villainWon(fighters);
		} else {
			fight = random.nextBoolean() ? heroWon(fighters) : villainWon(fighters);
		}

		fight.fightDate = Instant.now();
		Fight.persist(fight);

		// Send the fight to Kafka
		fightEmitter.send(fight);

		return fight;
	}

	private Fight heroWon(Fighters fighters) {

		LOGGER.info("Yes, Hero won :o)");
		Fight fight = new Fight();
		fight.winnerName = fighters.hero.name;
		fight.winnerPicture = fighters.hero.picture;
		fight.winnerLevel = fighters.hero.level;
		fight.loserName = fighters.villain.name;
		fight.loserPicture = fighters.villain.picture;
		fight.loserLevel = fighters.villain.level;
		fight.winnerTeam = "heroes";
		fight.loserTeam = "villains";
		return fight;
	}

	private Fight villainWon(Fighters fighters) {

		LOGGER.info("Gee, Villain won :o(");
		Fight fight = new Fight();
		fight.winnerName = fighters.villain.name;
		fight.winnerPicture = fighters.villain.picture;
		fight.winnerLevel = fighters.villain.level;
		fight.loserName = fighters.hero.name;
		fight.loserPicture = fighters.hero.picture;
		fight.loserLevel = fighters.hero.level;
		fight.winnerTeam = "villains";
		fight.loserTeam = "heroes";
		return fight;
	}

	public Fighters findRandomFighters() {
		Hero hero = findRandomHero();
		Villain villain = findRandomVillain();
		Fighters fighters = new Fighters();
		fighters.hero = hero;
		fighters.villain = villain;
		return fighters;
	}

	@Fallback(fallbackMethod = "fallbackRandomHero")
	public Hero findRandomHero() {
		return heroApiClient.getRandomHero();
	}

	@Fallback(fallbackMethod = "fallbackRandomVillain")
	public Villain findRandomVillain() {
		return villainApiClient.getRandomVillain();
	}

	public Hero fallbackRandomHero() {
		LOGGER.warn("Falling back on Hero");
		Hero hero = new Hero();
		hero.name = "Fallback Hero";
		hero.picture = "https://dummyimage.com/280x380/1e8fff/ffffff&text=Fallback+Hero";
		hero.powers = "Fallback Hero powers";
		hero.level = 45;
		return hero;
	}

	public Villain fallbackRandomVillain() {
		LOGGER.warn("Falling back on Villain");
		Villain villain = new Villain();
		villain.name = "Fallback Villain";
		villain.picture = "https://dummyimage.com/280x380/b22222/ffffff&text=Fallback+Villain";
		villain.powers = "Fallback Villain powers";
		villain.level = 42;
		return villain;
	}

}

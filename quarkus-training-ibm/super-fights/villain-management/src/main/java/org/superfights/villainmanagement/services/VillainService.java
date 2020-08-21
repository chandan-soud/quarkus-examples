package org.superfights.villainmanagement.services;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.superfights.villainmanagement.entities.Villain;

@ApplicationScoped
@Transactional(REQUIRED)
public class VillainService {

	@ConfigProperty(name = "level.multiplier", defaultValue = "1")
	int levelMultiplier;

	@Transactional(SUPPORTS)
	public List<Villain> findAllVillains() {
		return Villain.listAll();
	}

	@Transactional(SUPPORTS)
	public Villain findVillainById(Long id) {
		return Villain.findById(id);
	}

	@Transactional(SUPPORTS)
	public Villain findRandomVillain() {
		Villain randomVillain = null;
		while (randomVillain == null) {
			randomVillain = Villain.findRandomVillain();
		}
		return randomVillain;
	}

	public Villain persistVillain(@Valid Villain villain) {
		villain.level = villain.level * levelMultiplier;
		villain.persist();
		return villain;
	}

	public Villain updateVillain(@Valid Villain villain) {
		Villain entity = Villain.findById(villain.id);
		entity.name = villain.name;
		entity.otherName = villain.otherName;
		entity.level = villain.level;
		entity.picture = villain.picture;
		entity.powers = villain.powers;
		return entity;
	}

	public void deleteVillain(Long id) {
		Villain villain = Villain.findById(id);
		villain.delete();
	}

}

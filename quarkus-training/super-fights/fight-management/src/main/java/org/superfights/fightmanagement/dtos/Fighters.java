package org.superfights.fightmanagement.dtos;

import javax.validation.constraints.NotNull;

public class Fighters {

	@NotNull
	public Hero hero;

	@NotNull
	public Villain villain;

	@Override
	public String toString() {
		return "Fighters{" + "hero = " + hero + ", villain = " + villain + '}';
	}

}

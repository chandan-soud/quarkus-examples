package org.superfights.fightmanagement.clients;

import javax.validation.constraints.NotNull;

public class Hero {

	@NotNull
	public String name;

	@NotNull
	public int level;

	@NotNull
	public String picture;

	public String powers;

	@Override
	public String toString() {
		return "Hero{" + "name = '" + name + '\'' + ", level = " + level + ", picture = '" + picture + '\''
				+ ", powers = '" + powers + '\'' + '}';
	}
}

package org.superfights.fightmanagement.clients;

import javax.validation.constraints.NotNull;

public class Villain {

	@NotNull
	public String name;

	@NotNull
	public int level;

	@NotNull
	public String picture;

	public String powers;

	@Override
	public String toString() {
		return "Villain{" + "name = '" + name + '\'' + ", level = " + level + ", picture = '" + picture + '\''
				+ ", powers = '" + powers + '\'' + '}';
	}
}

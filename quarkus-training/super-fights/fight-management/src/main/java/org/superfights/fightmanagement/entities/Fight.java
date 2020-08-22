package org.superfights.fightmanagement.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
public class Fight extends PanacheEntity {

	@NotNull
	public Instant fightDate;

	@NotNull
	public String winnerName;

	@NotNull
	public int winnerLevel;

	@NotNull
	public String winnerPicture;

	@NotNull
	public String loserName;

	@NotNull
	public int loserLevel;

	@NotNull
	public String loserPicture;

	@NotNull
	public String winnerTeam;

	@NotNull
	public String loserTeam;

	@Override
	public String toString() {
		return "Fight{" + "id = " + id + ", fightDate = " + fightDate + ", winnerName = '" + winnerName + '\''
				+ ", winnerLevel = " + winnerLevel + ", winnerPicture = '" + winnerPicture + '\'' + ", winnerTeam = '"
				+ winnerTeam + '\'' + ", loserName = '" + loserName + '\'' + ", loserLevel = " + loserLevel
				+ ", loserPicture = '" + loserPicture + '\'' + ", loserTeam = '" + loserTeam + '\'' + '}';
	}
}
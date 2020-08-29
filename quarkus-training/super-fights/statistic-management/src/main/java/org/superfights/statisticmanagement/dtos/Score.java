package org.superfights.statisticmanagement.dtos;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Score {

	public final String name;
	public final int totalScore;

	public Score(String name, int totalScore) {
		this.name = name;
		this.totalScore = totalScore;
	}
}

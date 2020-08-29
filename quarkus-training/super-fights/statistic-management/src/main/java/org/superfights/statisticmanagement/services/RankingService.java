package org.superfights.statisticmanagement.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.superfights.statisticmanagement.dtos.Score;

import io.reactivex.Maybe;

public class RankingService {

	private final int max;
	private LinkedList<Score> top = new LinkedList<>();

	public RankingService(int size) {
		max = size;
	}

	private static final Comparator<Score> comparator = (score1, score2) -> {
		if (score2.totalScore > score1.totalScore) {
			return 1;
		} else if (score2.totalScore < score1.totalScore) {
			return -1;
		}
		return 0;
	};

	public synchronized Maybe<Iterable<Score>> onNewScore(Score score) {
		// synchronized should not be required if used in a flatMap, as the call needs
		// to be serialized.

		// Remove score if already present,
		// Add the score
		// Sort
		top.removeIf(s -> s.name.equalsIgnoreCase(score.name));
		top.add(score);
		top.sort(comparator);

		// Drop on overflow
		if (top.size() > max) {
			top.remove(top.getLast());
		}

		if (top.contains(score)) {
			return Maybe.just(Collections.unmodifiableList(top));
		} else {
			return Maybe.empty();
		}
	}

}

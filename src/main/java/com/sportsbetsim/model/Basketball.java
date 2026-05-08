package com.sportsbetsim.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Concrete Sport: Basketball.
 * Uses real NBA teams and generates realistic basketball scores (85–130 range).
 */
public class Basketball extends Sport {

    /** Pool of real NBA team names. */
    private static final String[] TEAMS = {
        "Los Angeles Lakers", "Golden State Warriors", "Boston Celtics",
        "Miami Heat", "Chicago Bulls", "Brooklyn Nets",
        "Dallas Mavericks", "Phoenix Suns", "Milwaukee Bucks",
        "Denver Nuggets", "Philadelphia 76ers", "Toronto Raptors",
        "Atlanta Hawks", "Cleveland Cavaliers", "Memphis Grizzlies"
    };

    private final Random rand;

    public Basketball() {
        super("Basketball");
        rand = new Random();
    }

    /**
     * Picks two distinct teams at random and assigns each a random rating (55–95).
     * @return two-element Team array
     */
    @Override
    public Team[] generateTeams() {
        List<String> pool = new ArrayList<>(List.of(TEAMS));
        Collections.shuffle(pool, rand);
        String nameA = pool.get(0);
        String nameB = pool.get(1);
        int ratingA = 55 + rand.nextInt(41); // 55–95
        int ratingB = 55 + rand.nextInt(41);
        return new Team[]{new Team(nameA, ratingA), new Team(nameB, ratingB)};
    }

    /**
     * Simulates a basketball score.
     * Base score is 95–110; the rating advantage adds up to ~15 extra points.
     */
    @Override
    public int[] simulateScore(Team a, Team b) {
        int base = 95 + rand.nextInt(16); // 95–110
        int bonus = (a.getRating() - b.getRating()) / 6; // rating advantage
        int noise = rand.nextInt(15) - 7; // ±7 randomness

        int scoreA = Math.max(70, base + bonus + noise);
        int scoreB = Math.max(70, base - bonus + (rand.nextInt(15) - 7));

        // Avoid exact ties (uncommon in basketball)
        if (scoreA == scoreB) scoreA++;
        return new int[]{scoreA, scoreB};
    }
}

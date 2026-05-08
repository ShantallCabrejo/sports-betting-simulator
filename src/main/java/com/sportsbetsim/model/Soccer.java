package com.sportsbetsim.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Concrete Sport: Soccer.
 * Uses real MLS/international club teams and generates realistic low-scoring results (0–5 per side).
 */
public class Soccer extends Sport {

    /** Pool of real soccer club team names. */
    private static final String[] TEAMS = {
        "LA Galaxy", "Seattle Sounders", "Portland Timbers",
        "Atlanta United", "New York City FC", "Inter Miami",
        "New England Revolution", "Orlando City", "Colorado Rapids",
        "FC Dallas", "Minnesota United", "Real Salt Lake",
        "Columbus Crew", "Nashville SC", "Austin FC"
    };

    private final Random rand;

    public Soccer() {
        super("Soccer");
        rand = new Random();
    }

    @Override
    public Team[] generateTeams() {
        List<String> pool = new ArrayList<>(List.of(TEAMS));
        Collections.shuffle(pool, rand);
        int ratingA = 50 + rand.nextInt(46);
        int ratingB = 50 + rand.nextInt(46);
        return new Team[]{new Team(pool.get(0), ratingA), new Team(pool.get(1), ratingB)};
    }

    /**
     * Simulates a soccer score.
     * Base goal probability is low (0–3 per side) but influenced by rating advantage.
     * Ties are more common in soccer than in other sports.
     */
    @Override
    public int[] simulateScore(Team a, Team b) {
        double ratingDiff = (a.getRating() - b.getRating()) / 100.0; // –1 to +1
        // Expected goals follow a Poisson-like distribution; we approximate with random
        int baseGoals = 1 + rand.nextInt(3); // 1–3
        double advantageA = 1.0 + ratingDiff * 0.5;
        double advantageB = 1.0 - ratingDiff * 0.5;

        int scoreA = generateGoals(baseGoals, advantageA);
        int scoreB = generateGoals(baseGoals, advantageB);

        return new int[]{scoreA, scoreB};
    }

    /**
     * Generates a goal count by sampling with a scaled probability.
     */
    private int generateGoals(int base, double advantage) {
        int goals = 0;
        int attempts = (int)(base * advantage * 2);
        for (int i = 0; i < Math.max(1, attempts); i++) {
            if (rand.nextDouble() < 0.4 * advantage) goals++;
        }
        return Math.min(goals, 7); // cap at 7
    }
}

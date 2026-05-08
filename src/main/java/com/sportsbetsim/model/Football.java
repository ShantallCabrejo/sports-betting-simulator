package com.sportsbetsim.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Concrete Sport: American Football.
 * Uses real NFL teams and generates realistic football scores (7–42 range,
 * in realistic increments of 3 and 7).
 */
public class Football extends Sport {

    /** Pool of real NFL team names. */
    private static final String[] TEAMS = {
        "Kansas City Chiefs", "San Francisco 49ers", "Dallas Cowboys",
        "Buffalo Bills", "Philadelphia Eagles", "Cincinnati Bengals",
        "Baltimore Ravens", "Miami Dolphins", "Las Vegas Raiders",
        "Green Bay Packers", "Seattle Seahawks", "New England Patriots",
        "Los Angeles Rams", "Tampa Bay Buccaneers", "Pittsburgh Steelers"
    };

    private final Random rand;

    public Football() {
        super("Football");
        rand = new Random();
    }

    @Override
    public Team[] generateTeams() {
        List<String> pool = new ArrayList<>(List.of(TEAMS));
        Collections.shuffle(pool, rand);
        int ratingA = 50 + rand.nextInt(46); // 50–95
        int ratingB = 50 + rand.nextInt(46);
        return new Team[]{new Team(pool.get(0), ratingA), new Team(pool.get(1), ratingB)};
    }

    /**
     * Simulates a football score.
     * Scores are built from touchdowns (7 pts) and field goals (3 pts).
     * Higher-rated teams get more scoring "drives".
     */
    @Override
    public int[] simulateScore(Team a, Team b) {
        int driveBase = 6 + rand.nextInt(5); // 6–10 total drives each
        int ratingAdvantage = (a.getRating() - b.getRating()) / 20;

        int scoreA = buildFootballScore(driveBase + ratingAdvantage);
        int scoreB = buildFootballScore(driveBase - ratingAdvantage);

        return new int[]{Math.max(0, scoreA), Math.max(0, scoreB)};
    }

    /**
     * Builds a realistic football score from a number of drives.
     * Each drive either scores a TD (7 pts) or FG (3 pts).
     */
    private int buildFootballScore(int drives) {
        int score = 0;
        for (int i = 0; i < Math.max(0, drives); i++) {
            score += rand.nextBoolean() ? 7 : 3;
        }
        return score;
    }
}

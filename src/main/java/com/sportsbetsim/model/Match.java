package com.sportsbetsim.model;

/**
 * Represents a single simulated sports match.
 * Holds the two competing teams, runs the simulation, and stores results.
 */
public class Match {

    private final Sport sport;
    private final Team teamA;
    private final Team teamB;
    private int scoreA;
    private int scoreB;
    private String winner; // team name, or "Draw"

    /**
     * Constructs a Match for the given sport and teams.
     * Call {@link #runGame()} to execute the simulation.
     */
    public Match(Sport sport, Team teamA, Team teamB) {
        this.sport = sport;
        this.teamA = teamA;
        this.teamB = teamB;
    }

    /**
     * Runs the score simulation via the Sport's simulateScore method.
     * Sets scoreA, scoreB, and winner accordingly.
     */
    public void runGame() {
        int[] scores = sport.simulateScore(teamA, teamB);
        scoreA = scores[0];
        scoreB = scores[1];

        if (scoreA > scoreB) winner = teamA.getName();
        else if (scoreB > scoreA) winner = teamB.getName();
        else winner = "Draw";
    }

    public int getScoreA()  { return scoreA; }
    public int getScoreB()  { return scoreB; }
    public String getWinner() { return winner; }
    public Team getTeamA()  { return teamA; }
    public Team getTeamB()  { return teamB; }

    /**
     * Returns a single-line summary string suitable for history logging.
     * @return summary string
     */
    public String getSummary() {
        return String.format("%s %d – %d %s (Winner: %s)",
                teamA.getName(), scoreA, scoreB, teamB.getName(), winner);
    }
}

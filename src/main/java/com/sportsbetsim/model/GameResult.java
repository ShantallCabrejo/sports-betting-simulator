package com.sportsbetsim.model;

/**
 * Immutable data object representing the result of one simulated game.
 * Carries all outcome information back to the UI layer.
 */
public class GameResult {

    private final String teamA;
    private final String teamB;
    private final int scoreA;
    private final int scoreB;
    private final String winner;
    private final boolean playerWon;
    private final int pointsDelta;   // positive = won, negative = lost
    private final String betTypeName;
    private final String sport;

    /**
     * Constructs a GameResult with all relevant outcome fields.
     */
    public GameResult(String sport, String teamA, String teamB,
                      int scoreA, int scoreB, String winner,
                      boolean playerWon, int pointsDelta, String betTypeName) {
        this.sport = sport;
        this.teamA = teamA;
        this.teamB = teamB;
        this.scoreA = scoreA;
        this.scoreB = scoreB;
        this.winner = winner;
        this.playerWon = playerWon;
        this.pointsDelta = pointsDelta;
        this.betTypeName = betTypeName;
    }

    public String getSport()      { return sport; }
    public String getTeamA()      { return teamA; }
    public String getTeamB()      { return teamB; }
    public int getScoreA()        { return scoreA; }
    public int getScoreB()        { return scoreB; }
    public String getWinner()     { return winner; }
    public boolean isPlayerWon()  { return playerWon; }
    public int getPointsDelta()   { return pointsDelta; }
    public String getBetTypeName(){ return betTypeName; }

    /**
     * Returns a concise one-line summary for the history log.
     */
    public String toHistoryEntry() {
        String outcome = playerWon
                ? "WON +" + pointsDelta + " pts"
                : "LOST -" + Math.abs(pointsDelta) + " pts";
        return String.format("[%s] %s %d – %d %s | Winner: %s | Bet: %s | %s",
                sport, teamA, scoreA, scoreB, teamB, winner, betTypeName, outcome);
    }
}

package com.sportsbetsim.model;

import com.sportsbetsim.strategy.BetStrategy;

/**
 * Represents a bet placed by the player for a single match.
 * Delegates win/loss logic to the injected {@link BetStrategy}.
 *
 * <p>Design Pattern: Strategy Pattern — the Bet class holds a reference
 * to a BetStrategy and delegates isWin() to it, keeping evaluation logic
 * separate from the bet data.</p>
 */
public class Bet {

    private final String pickedTeam;
    private final int amount;
    private final BetStrategy strategy;

    /**
     * Constructs a Bet.
     * @param pickedTeam name of the team the player chose
     * @param amount     points wagered
     * @param strategy   the bet strategy to use for evaluation
     */
    public Bet(String pickedTeam, int amount, BetStrategy strategy) {
        this.pickedTeam = pickedTeam;
        this.amount = amount;
        this.strategy = strategy;
    }

    /** @return wager amount */
    public int getAmount() {
        return amount;
    }

    /** @return picked team name */
    public String getPickedTeam() {
        return pickedTeam;
    }

    /** @return the human-readable bet type name from the strategy */
    public String getBetTypeName() {
        return strategy.getBetTypeName();
    }

    /**
     * Evaluates whether the bet is a winner by delegating to the strategy.
     * @param a      Team A
     * @param b      Team B
     * @param scoreA final score for Team A
     * @param scoreB final score for Team B
     * @return true if the bet wins
     */
    public boolean isWin(Team a, Team b, int scoreA, int scoreB) {
        return strategy.isWin(pickedTeam, a, b, scoreA, scoreB);
    }

    /** @return the injected strategy (useful for spread calculation) */
    public BetStrategy getStrategy() {
        return strategy;
    }
}

package com.sportsbetsim.strategy;

import com.sportsbetsim.model.Team;

/**
 * Strategy interface for bet evaluation.
 *
 * <p>Design Pattern: Strategy Pattern
 * Each concrete strategy encapsulates a different rule for determining
 * whether the player's bet was a winning bet. This allows new bet types
 * to be added without modifying existing classes (Open/Closed Principle).</p>
 */
public interface BetStrategy {

    /**
     * Determines whether the player's bet is a winning bet.
     *
     * @param pickedTeam  name of the team the player chose
     * @param a           Team A object
     * @param b           Team B object
     * @param scoreA      simulated score for Team A
     * @param scoreB      simulated score for Team B
     * @return true if the bet is a winner
     */
    boolean isWin(String pickedTeam, Team a, Team b, int scoreA, int scoreB);

    /**
     * Returns the human-readable name of this bet type.
     * @return bet type name
     */
    String getBetTypeName();
}

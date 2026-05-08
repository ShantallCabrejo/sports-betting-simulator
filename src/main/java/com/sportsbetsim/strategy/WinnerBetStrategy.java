package com.sportsbetsim.strategy;

import com.sportsbetsim.model.Team;

/**
 * Concrete Strategy: Winner Pick.
 *
 * <p>The player simply picks which team they think will win outright.
 * If the picked team has the higher score, the bet wins.</p>
 */
public class WinnerBetStrategy implements BetStrategy {

    /**
     * Win condition: the team the player picked must have a higher score.
     * A tie is treated as a loss (no money returned).
     */
    @Override
    public boolean isWin(String pickedTeam, Team a, Team b, int scoreA, int scoreB) {
        // Determine which team the player picked and compare scores
        if (pickedTeam.equals(a.getName())) {
            return scoreA > scoreB;
        } else {
            return scoreB > scoreA;
        }
    }

    @Override
    public String getBetTypeName() {
        return "Winner Pick";
    }
}

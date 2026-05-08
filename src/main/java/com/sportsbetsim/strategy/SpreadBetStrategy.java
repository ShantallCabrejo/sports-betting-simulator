package com.sportsbetsim.strategy;

import com.sportsbetsim.model.Team;

/**
 * Concrete Strategy: Spread Pick.
 *
 * <p>The spread is derived from the absolute difference in team ratings divided by 5
 * (minimum spread of 1). The player's picked team must "cover the spread":
 * <ul>
 *   <li>If the picked team is the higher-rated team, their score must exceed
 *       the opponent's score by MORE than the spread.</li>
 *   <li>If the picked team is the lower-rated team (the underdog), they must
 *       either win outright OR lose by LESS than the spread.</li>
 * </ul>
 * </p>
 */
public class SpreadBetStrategy implements BetStrategy {

    /**
     * Calculates the point spread from the two teams' rating difference.
     * Spread = |ratingA - ratingB| / 5, minimum 1.
     *
     * @param a Team A
     * @param b Team B
     * @return the calculated spread value
     */
    public static int calculateSpread(Team a, Team b) {
        return Math.max(1, Math.abs(a.getRating() - b.getRating()) / 5);
    }

    @Override
    public boolean isWin(String pickedTeam, Team a, Team b, int scoreA, int scoreB) {
        int spread = calculateSpread(a, b);
        int scoreDiff = scoreA - scoreB; // positive means A is winning

        boolean pickedA = pickedTeam.equals(a.getName());
        boolean aIsFavored = a.getRating() >= b.getRating();

        if (pickedA) {
            // Player picked Team A
            if (aIsFavored) {
                // A is the favorite; must win by more than the spread
                return scoreDiff > spread;
            } else {
                // A is the underdog; must win outright or lose by less than spread
                return scoreDiff > -spread;
            }
        } else {
            // Player picked Team B
            int scoreDiffB = scoreB - scoreA;
            boolean bIsFavored = b.getRating() >= a.getRating();
            if (bIsFavored) {
                return scoreDiffB > spread;
            } else {
                return scoreDiffB > -spread;
            }
        }
    }

    @Override
    public String getBetTypeName() {
        return "Spread Pick";
    }
}

package com.sportsbetsim;

import com.sportsbetsim.model.Team;
import com.sportsbetsim.strategy.SpreadBetStrategy;
import com.sportsbetsim.strategy.WinnerBetStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Automated test cases for BetStrategy implementations.
 */
class BetStrategyTest {

    private Team strong;   // rating 80
    private Team weak;     // rating 60
    private WinnerBetStrategy winnerStrategy;
    private SpreadBetStrategy spreadStrategy;

    @BeforeEach
    void setUp() {
        strong = new Team("Strong FC", 80);
        weak   = new Team("Weak United", 60);
        winnerStrategy = new WinnerBetStrategy();
        spreadStrategy = new SpreadBetStrategy();
    }

    // ── WinnerBetStrategy ─────────────────────────────────────────────────────

    // TC-11: Winner: pick winning team → true
    @Test
    void testWinnerPick_PickWinner() {
        // Strong wins 110-90; player picks Strong
        assertTrue(winnerStrategy.isWin("Strong FC", strong, weak, 110, 90));
    }

    // TC-12: Winner: pick losing team → false
    @Test
    void testWinnerPick_PickLoser() {
        assertFalse(winnerStrategy.isWin("Weak United", strong, weak, 110, 90));
    }

    // TC-13: Winner: pick Team B (second team) when B wins
    @Test
    void testWinnerPick_PickTeamBWins() {
        assertTrue(winnerStrategy.isWin("Weak United", strong, weak, 80, 95));
    }

    // TC-14: Winner: tie is a loss
    @Test
    void testWinnerPick_Tie() {
        assertFalse(winnerStrategy.isWin("Strong FC", strong, weak, 100, 100));
    }

    // TC-15: getBetTypeName returns correct string
    @Test
    void testWinnerBetTypeName() {
        assertEquals("Winner Pick", winnerStrategy.getBetTypeName());
    }

    // ── SpreadBetStrategy ─────────────────────────────────────────────────────

    // TC-16: Spread calculation: |80-60| / 5 = 4
    @Test
    void testSpreadCalculation() {
        assertEquals(4, SpreadBetStrategy.calculateSpread(strong, weak));
    }

    // TC-17: Spread minimum is 1 even when ratings are equal
    @Test
    void testSpreadMinimumOne() {
        Team a = new Team("A", 70);
        Team b = new Team("B", 70);
        assertEquals(1, SpreadBetStrategy.calculateSpread(a, b));
    }

    // TC-18: Spread: favorite wins by more than spread → win
    @Test
    void testSpreadPick_FavoriteCoversSpread() {
        // strong (80) vs weak (60), spread = 4
        // Strong wins by 10 → covers spread of 4 → true
        assertTrue(spreadStrategy.isWin("Strong FC", strong, weak, 100, 90));
    }

    // TC-19: Spread: favorite wins by exactly the spread → loss (must exceed)
    @Test
    void testSpreadPick_FavoriteExactSpread() {
        // Spread = 4; strong wins by exactly 4 → does NOT cover (must exceed)
        assertFalse(spreadStrategy.isWin("Strong FC", strong, weak, 100, 96));
    }

    // TC-20: Spread: underdog beats spread (loses by less than spread)
    @Test
    void testSpreadPick_UnderdogCoversSpread() {
        // Spread = 4; underdog loses by 2 → covers spread → win
        assertTrue(spreadStrategy.isWin("Weak United", strong, weak, 100, 98));
    }

    // TC-21: Spread: underdog loses by more than spread → loss
    @Test
    void testSpreadPick_UnderdogFailsTocover() {
        // Spread = 4; underdog loses by 10 → does not cover → false
        assertFalse(spreadStrategy.isWin("Weak United", strong, weak, 110, 90));
    }

    // TC-22: getBetTypeName for spread strategy
    @Test
    void testSpreadBetTypeName() {
        assertEquals("Spread Pick", spreadStrategy.getBetTypeName());
    }
}

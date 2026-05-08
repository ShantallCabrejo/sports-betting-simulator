package com.sportsbetsim;

import com.sportsbetsim.controller.GameController;
import com.sportsbetsim.model.GameResult;
import com.sportsbetsim.model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Automated test cases for GameController (integration-style tests).
 */
class GameControllerTest {

    private GameController controller;

    @BeforeEach
    void setUp() {
        controller = new GameController();
    }

    // TC-23: Initial balance is 1000
    @Test
    void testInitialBalance() {
        assertEquals(1000, controller.getBalance());
    }

    // TC-24: loadTeams returns exactly 2 teams for Basketball
    @Test
    void testLoadTeamsBasketball() {
        Team[] teams = controller.loadTeams("Basketball");
        assertNotNull(teams);
        assertEquals(2, teams.length);
        assertNotNull(teams[0]);
        assertNotNull(teams[1]);
    }

    // TC-25: loadTeams returns 2 teams for Football
    @Test
    void testLoadTeamsFootball() {
        Team[] teams = controller.loadTeams("Football");
        assertEquals(2, teams.length);
    }

    // TC-26: loadTeams returns 2 teams for Soccer
    @Test
    void testLoadTeamsSoccer() {
        Team[] teams = controller.loadTeams("Soccer");
        assertEquals(2, teams.length);
    }

    // TC-27: loadTeams returns teams with non-null, non-empty names
    @Test
    void testLoadTeamsHaveNames() {
        Team[] teams = controller.loadTeams("Basketball");
        assertFalse(teams[0].getName().isBlank());
        assertFalse(teams[1].getName().isBlank());
    }

    // TC-28: Team ratings are within valid range (1–100)
    @Test
    void testTeamRatingsInRange() {
        Team[] teams = controller.loadTeams("Football");
        for (Team t : teams) {
            assertTrue(t.getRating() >= 1 && t.getRating() <= 100,
                    "Rating out of range: " + t.getRating());
        }
    }

    // TC-29: simulate throws on non-numeric wager
    @Test
    void testSimulateThrowsOnNonNumericAmount() {
        controller.loadTeams("Basketball");
        Team[] teams = controller.getCurrentTeams();
        assertThrows(IllegalArgumentException.class, () ->
                controller.simulate(teams[0].getName(), "Winner Pick", "abc"));
    }

    // TC-30: simulate throws on zero wager
    @Test
    void testSimulateThrowsOnZeroAmount() {
        controller.loadTeams("Basketball");
        Team[] teams = controller.getCurrentTeams();
        assertThrows(IllegalArgumentException.class, () ->
                controller.simulate(teams[0].getName(), "Winner Pick", "0"));
    }

    // TC-31: simulate throws on negative wager
    @Test
    void testSimulateThrowsOnNegativeAmount() {
        controller.loadTeams("Soccer");
        Team[] teams = controller.getCurrentTeams();
        assertThrows(IllegalArgumentException.class, () ->
                controller.simulate(teams[0].getName(), "Winner Pick", "-10"));
    }

    // TC-32: simulate throws when wager exceeds balance
    @Test
    void testSimulateThrowsWhenInsufficientBalance() {
        controller.loadTeams("Basketball");
        Team[] teams = controller.getCurrentTeams();
        assertThrows(IllegalArgumentException.class, () ->
                controller.simulate(teams[0].getName(), "Winner Pick", "9999"));
    }

    // TC-33: simulate returns a valid GameResult
    @Test
    void testSimulateReturnsResult() {
        controller.loadTeams("Basketball");
        Team[] teams = controller.getCurrentTeams();
        GameResult result = controller.simulate(teams[0].getName(), "Winner Pick", "100");
        assertNotNull(result);
        assertNotNull(result.getWinner());
        assertTrue(result.getScoreA() >= 0);
        assertTrue(result.getScoreB() >= 0);
    }

    // TC-34: balance changes after simulating
    @Test
    void testBalanceChangesAfterSimulate() {
        controller.loadTeams("Basketball");
        Team[] teams = controller.getCurrentTeams();
        int before = controller.getBalance();
        controller.simulate(teams[0].getName(), "Winner Pick", "100");
        int after = controller.getBalance();
        assertNotEquals(before, after, "Balance should change after simulation.");
    }

    // TC-35: history grows after each game
    @Test
    void testHistoryGrowsAfterGames() {
        assertEquals(0, controller.getGameCount());
        controller.loadTeams("Basketball");
        Team[] teams = controller.getCurrentTeams();
        controller.simulate(teams[0].getName(), "Winner Pick", "50");
        assertEquals(1, controller.getGameCount());

        controller.loadTeams("Football");
        teams = controller.getCurrentTeams();
        controller.simulate(teams[0].getName(), "Spread Pick", "50");
        assertEquals(2, controller.getGameCount());
    }

    // TC-36: reset clears history and restores balance
    @Test
    void testReset() {
        controller.loadTeams("Basketball");
        Team[] teams = controller.getCurrentTeams();
        controller.simulate(teams[0].getName(), "Winner Pick", "200");
        controller.reset();
        assertEquals(1000, controller.getBalance());
        assertEquals(0, controller.getGameCount());
    }

    // TC-37: simulate without loadTeams throws IllegalStateException
    @Test
    void testSimulateWithoutLoadTeams() {
        assertThrows(IllegalStateException.class, () ->
                controller.simulate("SomeTeam", "Winner Pick", "100"));
    }

    // TC-38: loadTeams for unknown sport throws IllegalArgumentException
    @Test
    void testLoadTeamsUnknownSport() {
        assertThrows(IllegalArgumentException.class, () ->
                controller.loadTeams("Tennis"));
    }
}

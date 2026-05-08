package com.sportsbetsim;

import com.sportsbetsim.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Automated test cases for the Player class.
 */
class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player();
    }

    // TC-01: New player starts with 1000 points
    @Test
    void testInitialBalance() {
        assertEquals(1000, player.getBalance(), "New player should start with 1000 points.");
    }

    // TC-02: canAfford returns false for zero
    @Test
    void testCannotAffordZero() {
        assertFalse(player.canAfford(0), "Cannot afford 0.");
    }

    // TC-03: canAfford returns false for negative amounts
    @Test
    void testCannotAffordNegative() {
        assertFalse(player.canAfford(-50));
    }

    // TC-04: canAfford returns true for exactly the balance
    @Test
    void testCanAffordExactBalance() {
        assertTrue(player.canAfford(1000));
    }

    // TC-05: canAfford returns false when amount exceeds balance
    @Test
    void testCannotAffordMoreThanBalance() {
        assertFalse(player.canAfford(1001));
    }

    // TC-06: addPoints increases balance correctly
    @Test
    void testAddPoints() {
        player.addPoints(200);
        assertEquals(1200, player.getBalance());
    }

    // TC-07: subtractPoints decreases balance correctly
    @Test
    void testSubtractPoints() {
        player.subtractPoints(300);
        assertEquals(700, player.getBalance());
    }

    // TC-08: addPoints ignores zero or negative
    @Test
    void testAddPointsIgnoresNonPositive() {
        player.addPoints(0);
        player.addPoints(-50);
        assertEquals(1000, player.getBalance());
    }

    // TC-09: reset restores balance to 1000
    @Test
    void testReset() {
        player.subtractPoints(800);
        player.reset();
        assertEquals(1000, player.getBalance());
    }

    // TC-10: Balance can go below zero after losing more than available
    @Test
    void testSubtractBeyondBalance() {
        player.subtractPoints(1500);
        assertEquals(-500, player.getBalance());
    }
}

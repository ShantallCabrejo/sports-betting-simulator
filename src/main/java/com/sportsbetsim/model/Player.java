package com.sportsbetsim.model;

/**
 * Represents the user/player in the game.
 * Tracks the player's current fake-points balance.
 */
public class Player {

    private static final int STARTING_BALANCE = 1000;

    private int balance;

    /** Creates a new Player with the default starting balance of 1000. */
    public Player() {
        this.balance = STARTING_BALANCE;
    }

    /** @return current point balance */
    public int getBalance() {
        return balance;
    }

    /**
     * Checks whether the player can afford the given amount.
     * @param amount amount to wager
     * @return true if balance >= amount and amount > 0
     */
    public boolean canAfford(int amount) {
        return amount > 0 && balance >= amount;
    }

    /**
     * Adds the specified number of points to the balance.
     * @param n points to add (must be positive)
     */
    public void addPoints(int n) {
        if (n > 0) balance += n;
    }

    /**
     * Subtracts the specified number of points from the balance.
     * @param n points to subtract (must be positive)
     */
    public void subtractPoints(int n) {
        if (n > 0) balance -= n;
    }

    /** Resets balance to the starting amount. */
    public void reset() {
        balance = STARTING_BALANCE;
    }
}

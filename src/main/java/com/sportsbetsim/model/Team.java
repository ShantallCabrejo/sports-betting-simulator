package com.sportsbetsim.model;

/**
 * Represents a sports team with a name and a skill rating.
 * Rating is used to influence score simulation and spread calculations.
 */
public class Team {

    private final String name;
    private final int rating; // 1–100

    /**
     * Constructs a Team with the given name and rating.
     * @param name   team name
     * @param rating skill rating (1–100)
     */
    public Team(String name, int rating) {
        this.name = name;
        this.rating = rating;
    }

    /** @return team name */
    public String getName() {
        return name;
    }

    /** @return skill rating */
    public int getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return name + " (Rating: " + rating + ")";
    }
}

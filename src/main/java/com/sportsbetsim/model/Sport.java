package com.sportsbetsim.model;

/**
 * Abstract base class for all sports.
 *
 * <p>Design Principle: Abstraction & Polymorphism — Sport defines the contract
 * that all concrete sport classes must fulfil. Each sport knows how to generate
 * its own teams and simulate a realistic score for that sport.</p>
 *
 * <p>Design Principle: Open/Closed — new sports can be added by extending this
 * class without changing any existing code.</p>
 */
public abstract class Sport {

    /** The display name of the sport (e.g. "Basketball"). */
    protected final String name;

    /**
     * Constructs a Sport with the given name.
     * @param name sport display name
     */
    public Sport(String name) {
        this.name = name;
    }

    /** @return sport display name */
    public String getSportName() {
        return name;
    }

    /**
     * Generates a shuffled array of two random real professional teams
     * with auto-generated skill ratings specific to this sport.
     *
     * @return two-element Team array
     */
    public abstract Team[] generateTeams();

    /**
     * Simulates a realistic final score for this sport given two teams.
     * The better-rated team should have a higher probability of winning
     * and a score advantage proportional to the rating gap.
     *
     * @param a Team A
     * @param b Team B
     * @return two-element int array: {scoreA, scoreB}
     */
    public abstract int[] simulateScore(Team a, Team b);
}

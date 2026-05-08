package com.sportsbetsim.factory;

import com.sportsbetsim.model.Basketball;
import com.sportsbetsim.model.Football;
import com.sportsbetsim.model.Soccer;
import com.sportsbetsim.model.Sport;

/**
 * Factory for creating Sport objects.
 *
 * <p>Design Pattern: Factory Pattern — centralises the creation of concrete
 * Sport objects. The caller asks for a sport by name and receives the correct
 * subclass without needing to know which class to instantiate. This supports
 * the Open/Closed Principle: adding a new sport only requires adding a new
 * {@link Sport} subclass and a new case here.</p>
 */
public class SportFactory {

    /**
     * Creates and returns the Sport instance corresponding to the given name.
     *
     * @param sportName "Basketball", "Football", or "Soccer"
     * @return a new Sport instance
     * @throws IllegalArgumentException if sportName is unrecognised
     */
    public static Sport createSport(String sportName) {
        return switch (sportName) {
            case "Basketball" -> new Basketball();
            case "Football"   -> new Football();
            case "Soccer"     -> new Soccer();
            default -> throw new IllegalArgumentException("Unknown sport: " + sportName);
        };
    }
}

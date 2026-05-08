package com.sportsbetsim.controller;

import com.sportsbetsim.factory.SportFactory;
import com.sportsbetsim.model.*;
import com.sportsbetsim.strategy.BetStrategy;
import com.sportsbetsim.strategy.SpreadBetStrategy;
import com.sportsbetsim.strategy.WinnerBetStrategy;

/**
 * Central controller (application logic layer).
 *
 * <p>Design Principle: Separation of Concerns — GameController owns all
 * game-logic decisions. The UI layer (SportsPredictionApp) only calls
 * controller methods and displays the returned data; it never directly
 * manipulates model objects.</p>
 *
 * <p>Design Principle: Single Responsibility — each method has one
 * well-defined job (load teams, simulate, reset).</p>
 */
public class GameController {

    private final Player player;
    private final HistoryManager history;

    private Sport currentSport;
    private Team[] currentTeams;

    public GameController() {
        player = new Player();
        history = new HistoryManager();
    }

    /**
     * Loads two random teams for the chosen sport using the SportFactory.
     * Stores the current sport and teams for use in the next simulation.
     *
     * @param sportName "Basketball", "Football", or "Soccer"
     * @return array of two Team objects
     */
    public Team[] loadTeams(String sportName) {
        currentSport = SportFactory.createSport(sportName);
        currentTeams = currentSport.generateTeams();
        return currentTeams;
    }

    /**
     * Runs a full game simulation given user inputs.
     *
     * <p>Steps: create Bet → run Match → evaluate win/loss → update Player balance
     * → add entry to HistoryManager → return GameResult.</p>
     *
     * @param pickedTeam name of the team the user selected
     * @param betType    "Winner Pick" or "Spread Pick"
     * @param amountText raw string from the wager input field
     * @return a {@link GameResult} encapsulating all outcome data
     * @throws IllegalStateException    if loadTeams was never called
     * @throws IllegalArgumentException if amountText is not a positive integer
     *                                  or exceeds the player's balance
     */
    public GameResult simulate(String pickedTeam, String betType, String amountText) {
        if (currentSport == null || currentTeams == null) {
            throw new IllegalStateException("No sport selected. Call loadTeams() first.");
        }

        // --- Parse and validate wager amount ---
        int amount;
        try {
            amount = Integer.parseInt(amountText.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Wager must be a whole number.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Wager must be greater than 0.");
        }
        if (!player.canAfford(amount)) {
            throw new IllegalArgumentException(
                    "Insufficient balance. You have " + player.getBalance() + " points.");
        }

        // --- Build strategy via simple selection ---
        BetStrategy strategy = betType.equals("Spread Pick")
                ? new SpreadBetStrategy()
                : new WinnerBetStrategy();

        // --- Create Bet ---
        Bet bet = new Bet(pickedTeam, amount, strategy);

        // --- Run Match ---
        Team teamA = currentTeams[0];
        Team teamB = currentTeams[1];
        Match match = new Match(currentSport, teamA, teamB);
        match.runGame();

        int scoreA = match.getScoreA();
        int scoreB = match.getScoreB();
        String winner = match.getWinner();

        // --- Evaluate bet ---
        boolean playerWon = bet.isWin(teamA, teamB, scoreA, scoreB);

        // --- Update balance ---
        if (playerWon) {
            player.addPoints(amount);
        } else {
            player.subtractPoints(amount);
        }

        // --- Build history entry ---
        String entry = String.format(
                "[%s] %s %d – %d %s | Winner: %s | Bet: %s on %s for %d pts | %s",
                currentSport.getSportName(),
                teamA.getName(), scoreA, scoreB, teamB.getName(),
                winner,
                bet.getBetTypeName(), pickedTeam, amount,
                playerWon ? "WON +" + amount : "LOST -" + amount
        );
        history.add(entry);

        return new GameResult(
                currentSport.getSportName(),
                teamA.getName(), teamB.getName(),
                scoreA, scoreB,
                winner,
                playerWon,
                playerWon ? amount : -amount,
                bet.getBetTypeName()
        );
    }

    /** Resets the player's balance and clears match history. */
    public void reset() {
        player.reset();
        history.clear();
        currentSport = null;
        currentTeams = null;
    }

    /** @return current player balance */
    public int getBalance() {
        return player.getBalance();
    }

    /** @return formatted history string from HistoryManager */
    public String getHistory() {
        return history.getFormatted();
    }

    /** @return number of games played */
    public int getGameCount() {
        return history.getCount();
    }

    /** @return current teams (may be null before loadTeams is called) */
    public Team[] getCurrentTeams() {
        return currentTeams;
    }

    /** @return current sport (may be null before loadTeams is called) */
    public Sport getCurrentSport() {
        return currentSport;
    }
}

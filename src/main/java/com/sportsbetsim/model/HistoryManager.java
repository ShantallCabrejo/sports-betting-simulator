package com.sportsbetsim.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the match history log.
 * Stores formatted string entries for each completed game.
 */
public class HistoryManager {

    private final ArrayList<String> entries;

    /** Constructs an empty HistoryManager. */
    public HistoryManager() {
        entries = new ArrayList<>();
    }

    /**
     * Appends a new history entry.
     * @param entry formatted summary string for a completed match
     */
    public void add(String entry) {
        entries.add(entry);
    }

    /**
     * Returns all history entries joined by newlines, numbered.
     * @return formatted history string
     */
    public String getFormatted() {
        if (entries.isEmpty()) return "No match history yet.";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < entries.size(); i++) {
            sb.append("Game ").append(i + 1).append(": ").append(entries.get(i));
            if (i < entries.size() - 1) sb.append("\n\n");
        }
        return sb.toString();
    }

    /** @return total number of entries */
    public int getCount() {
        return entries.size();
    }

    /** Clears all history entries. */
    public void clear() {
        entries.clear();
    }

    /**
     * Returns an unmodifiable view of the entries list.
     * @return list of history entry strings
     */
    public List<String> getEntries() {
        return List.copyOf(entries);
    }
}

package com.sportsbetsim;

import com.sportsbetsim.model.HistoryManager;
import com.sportsbetsim.model.Team;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Automated test cases for HistoryManager and Team.
 */
class HistoryAndTeamTest {

    // ── HistoryManager ────────────────────────────────────────────────────────

    // TC-39: Empty history returns placeholder string
    @Test
    void testEmptyHistory() {
        HistoryManager hm = new HistoryManager();
        assertEquals("No match history yet.", hm.getFormatted());
    }

    // TC-40: getCount returns 0 when empty
    @Test
    void testEmptyCount() {
        HistoryManager hm = new HistoryManager();
        assertEquals(0, hm.getCount());
    }

    // TC-41: add increases count
    @Test
    void testAddIncreasesCount() {
        HistoryManager hm = new HistoryManager();
        hm.add("Game 1 entry");
        assertEquals(1, hm.getCount());
    }

    // TC-42: getFormatted contains numbered entries
    @Test
    void testFormattedContainsGameNumber() {
        HistoryManager hm = new HistoryManager();
        hm.add("Lakers 110 - 90 Warriors");
        assertTrue(hm.getFormatted().contains("Game 1:"));
    }

    // TC-43: clear resets count to 0
    @Test
    void testClearResetsCount() {
        HistoryManager hm = new HistoryManager();
        hm.add("entry");
        hm.add("entry2");
        hm.clear();
        assertEquals(0, hm.getCount());
    }

    // TC-44: clear makes formatted return placeholder
    @Test
    void testClearMakesFormattedPlaceholder() {
        HistoryManager hm = new HistoryManager();
        hm.add("something");
        hm.clear();
        assertEquals("No match history yet.", hm.getFormatted());
    }

    // TC-45: Multiple entries are all included in formatted output
    @Test
    void testMultipleEntriesInFormatted() {
        HistoryManager hm = new HistoryManager();
        hm.add("Entry A");
        hm.add("Entry B");
        String formatted = hm.getFormatted();
        assertTrue(formatted.contains("Entry A"));
        assertTrue(formatted.contains("Entry B"));
    }

    // ── Team ──────────────────────────────────────────────────────────────────

    // TC-46: Team getName returns correct name
    @Test
    void testTeamGetName() {
        Team t = new Team("Lakers", 85);
        assertEquals("Lakers", t.getName());
    }

    // TC-47: Team getRating returns correct rating
    @Test
    void testTeamGetRating() {
        Team t = new Team("Lakers", 85);
        assertEquals(85, t.getRating());
    }

    // TC-48: Team toString contains name and rating
    @Test
    void testTeamToString() {
        Team t = new Team("Bulls", 70);
        String s = t.toString();
        assertTrue(s.contains("Bulls"));
        assertTrue(s.contains("70"));
    }
}

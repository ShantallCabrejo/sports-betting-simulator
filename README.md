# 🏆 SportsBetSim

A JavaFX sports simulation and betting game for CS 151 (Object-Oriented Analysis and Design).

The user starts with **1,000 fake points**, picks a sport, sees two randomly matched real professional teams with skill ratings, places a bet, and simulates the game. Points are won or lost based on the outcome. Match history is tracked throughout the session.

---

## Requirements

Before anything else, make sure you have these two things installed:

| Tool | Version | Download |
|------|---------|----------|
| Java JDK | 21 | https://adoptium.net/temurin/releases/?version=21 |
| Apache Maven | 3.9.x (stable) | https://maven.apache.org/download.cgi |

> ⚠️ **Do not use Maven 4.0.x** — it is a preview/RC version and not stable. Download **3.9.15** (the "Binary zip archive").

---

## Installation

### 1. Install Java 21

1. Go to https://adoptium.net/temurin/releases/?version=21
2. Select **Windows**, **x64**, **JDK**, download the `.msi` installer
3. Run the installer — it sets up your PATH automatically
4. Verify in a terminal:
   ```
   java -version
   ```
   You should see `openjdk version "21..."`

---

### 2. Install Maven 3.9.x

1. Go to https://maven.apache.org/download.cgi
2. Under **Apache Maven 3.9.15**, download the **Binary zip archive** (`apache-maven-3.9.15-bin.zip`)
3. Unzip it into:
   ```
   C:\Program Files\Maven\
   ```
   You should end up with this path existing:
   ```
   C:\Program Files\Maven\apache-maven-3.9.15\bin
   ```

4. Add Maven to your system PATH:
   - Press **Windows key**, search **"environment variables"**
   - Click **"Edit the system environment variables"**
   - Click **"Environment Variables..."**
   - Under **System variables**, click **Path** → **Edit** → **New**
   - Paste:
     ```
     C:\Program Files\Maven\apache-maven-3.9.15\bin
     ```
   - Click **OK** on all three windows

5. **Close and reopen** your terminal, then verify:
   ```
   mvn -version
   ```
   You should see Maven version info.

---

### 3. Open the Project in VS Code

1. Unzip `Assignment6-BetSimDevs.zip` anywhere (e.g. your Desktop)
2. Open **VS Code**
3. Go to **File → Open Folder** and select the `SportsBetSim` folder
4. Install the **Extension Pack for Java** by Microsoft if prompted (or search for it in the Extensions tab)
5. Wait for VS Code to finish loading the Java project (watch the bottom status bar)

---

### 4. Switch Terminal to Command Prompt (Recommended)

PowerShell sometimes doesn't pick up PATH changes correctly. To avoid issues:

1. Press `Ctrl+Shift+P` in VS Code
2. Type **"Select Default Profile"** and click **Terminal: Select Default Profile**
3. Select **Command Prompt**
4. Open a new terminal with **Ctrl + `**

---

## Running the App

In the VS Code terminal, make sure you are inside the `SportsBetSim` folder (you should see `pom.xml` if you run `dir`), then run:

```
mvn javafx:run
```

> ⚠️ The **first time** you run this, Maven will download JavaFX and JUnit dependencies from the internet (~50–100 MB). This only happens once. Just wait for it to finish — the app window will open automatically.

---

## Running the Tests

```
mvn test
```

All **48 automated JUnit 5 tests** should pass. You will see:

```
BUILD SUCCESS
Tests run: 48, Failures: 0, Errors: 0
```

Test results are also saved to `target/surefire-reports/`.

---

## Project Structure

```
SportsBetSim/
├── pom.xml                          ← Maven build file
├── README.md                        ← This file
├── SportsBetSim_Report.docx         ← Final project report
└── src/
    ├── main/java/com/sportsbetsim/
    │   ├── controller/
    │   │   └── GameController.java  ← All game logic lives here
    │   ├── factory/
    │   │   └── SportFactory.java    ← Factory Pattern: creates Sport objects
    │   ├── model/
    │   │   ├── Basketball.java      ← NBA teams + score simulation
    │   │   ├── Bet.java             ← Holds bet data, delegates to BetStrategy
    │   │   ├── Football.java        ← NFL teams + score simulation
    │   │   ├── GameResult.java      ← Immutable outcome data object
    │   │   ├── HistoryManager.java  ← Tracks match history log
    │   │   ├── Match.java           ← Runs a single game simulation
    │   │   ├── Player.java          ← Tracks fake-point balance
    │   │   ├── Soccer.java          ← MLS teams + score simulation
    │   │   ├── Sport.java           ← Abstract base class for all sports
    │   │   └── Team.java            ← Name + skill rating data object
    │   ├── strategy/
    │   │   ├── BetStrategy.java     ← Strategy Pattern interface
    │   │   ├── WinnerBetStrategy.java  ← Win by picking the outright winner
    │   │   └── SpreadBetStrategy.java  ← Win by covering the point spread
    │   └── view/
    │       └── SportsPredictionApp.java  ← JavaFX UI entry point
    └── test/java/com/sportsbetsim/
        ├── PlayerTest.java          ← 10 tests for Player
        ├── BetStrategyTest.java     ← 12 tests for both bet strategies
        ├── GameControllerTest.java  ← 16 tests for GameController
        └── HistoryAndTeamTest.java  ← 10 tests for HistoryManager and Team
```

---

## How to Play

1. **Select a sport** — Basketball, Football, or Soccer
2. **Click "Load Matchup"** — two random real teams appear with skill ratings
3. **Pick a team** using the radio buttons
4. **Choose a bet type:**
   - **Winner Pick** — simply pick which team wins outright
   - **Spread Pick** — the favored team must win by more than the spread; the underdog must win or lose by less than the spread. The spread is shown on screen.
5. **Enter a wager amount** (must be a positive whole number, cannot exceed your balance)
6. **Click "Simulate Game"** — the result appears and your balance updates
7. Repeat as many times as you like. Use **Reset** to start over with 1,000 points.

---

## Design Patterns Used

| Pattern | Where |
|---------|-------|
| **Strategy** | `BetStrategy` interface + `WinnerBetStrategy` / `SpreadBetStrategy` — swappable bet evaluation logic |
| **Factory** | `SportFactory.createSport()` — creates the correct Sport subclass from a name string |

---

## Troubleshooting

**`mvn` is not recognized**
- Maven is not on your PATH. Re-do the Maven installation steps, make sure you clicked OK on all three Environment Variables windows, and restart VS Code completely.

**`java` is not recognized**
- JDK 21 is not installed or not on PATH. Re-run the JDK installer.

**`BUILD FAILURE` on `mvn javafx:run`**
- Make sure your terminal is inside the `SportsBetSim` folder (where `pom.xml` is). Run `dir` and confirm `pom.xml` is listed.

**App window doesn't open**
- The first run downloads dependencies and can take a minute. Wait for the terminal to stop printing download progress before assuming something is wrong.

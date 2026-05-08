package com.sportsbetsim.view;

import com.sportsbetsim.controller.GameController;
import com.sportsbetsim.model.GameResult;
import com.sportsbetsim.model.Team;
import com.sportsbetsim.strategy.SpreadBetStrategy;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

/**
 * Main JavaFX entry point for SportsBetSim.
 *
 * <p>Design Principle: Separation of Concerns — this class is responsible only
 * for rendering the UI and handling user events. All business logic is delegated
 * to {@link GameController}.</p>
 *
 * <p>Design Principle: Single Responsibility — SportsPredictionApp handles only
 * view/input concerns.</p>
 */
public class SportsPredictionApp extends Application {

    // ── Controller ────────────────────────────────────────────────────────────
    private final GameController controller = new GameController();

    // ── UI Controls ───────────────────────────────────────────────────────────
    private ComboBox<String> sportBox;
    private ComboBox<String> betTypeBox;
    private RadioButton btnA;
    private RadioButton btnB;
    private ToggleGroup teamGroup;
    private TextField amountField;

    // Display labels
    private Label balanceLabel;
    private Label teamALabel;
    private Label teamBLabel;
    private Label teamARatingLabel;
    private Label teamBRatingLabel;
    private Label spreadLabel;
    private Label resultLabel;
    private Label resultDetailLabel;
    private TextArea historyArea;

    // Sections that should only appear after sport is selected
    private VBox betSection;
    private VBox resultSection;

    // ── Entry Point ───────────────────────────────────────────────────────────
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("SportsBetSim — Sports Prediction Game");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Header
        root.setTop(buildHeader());

        // Center: main game panel
        ScrollPane centerScroll = new ScrollPane(buildCenterPanel());
        centerScroll.setFitToWidth(true);
        centerScroll.setStyle("-fx-background: #1a1a2e; -fx-background-color: #1a1a2e;");
        root.setCenter(centerScroll);

        // Right: history panel
        root.setRight(buildHistoryPanel());

        Scene scene = new Scene(root, 1050, 720);
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    // ── Header ────────────────────────────────────────────────────────────────
    private HBox buildHeader() {
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #16213e; -fx-border-color: #e94560; "
                + "-fx-border-width: 0 0 3 0;");
        header.setPadding(new Insets(14, 20, 14, 20));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(16);

        Label title = new Label("🏆 SportsBetSim");
        title.setStyle("-fx-text-fill: #e94560; -fx-font-size: 24px; "
                + "-fx-font-weight: bold; -fx-font-family: 'Segoe UI';");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        balanceLabel = new Label("Balance: 1000 pts");
        balanceLabel.setStyle("-fx-text-fill: #a8ff78; -fx-font-size: 16px; "
                + "-fx-font-weight: bold; -fx-font-family: 'Segoe UI';");

        Button resetBtn = new Button("🔄 Reset");
        resetBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; "
                + "-fx-font-size: 13px; -fx-font-weight: bold; -fx-cursor: hand; "
                + "-fx-background-radius: 6; -fx-padding: 6 14 6 14;");
        resetBtn.setOnAction(e -> onReset());

        header.getChildren().addAll(title, spacer, balanceLabel, resetBtn);
        return header;
    }

    // ── Center Panel ─────────────────────────────────────────────────────────
    private VBox buildCenterPanel() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(24, 24, 24, 24));

        // Step 1: Sport selection
        panel.getChildren().add(buildSportSection());

        // Step 2: Matchup display + bet (hidden until sport chosen)
        betSection = buildBetSection();
        betSection.setVisible(false);
        betSection.setManaged(false);
        panel.getChildren().add(betSection);

        // Step 3: Result display (hidden until simulated)
        resultSection = buildResultSection();
        resultSection.setVisible(false);
        resultSection.setManaged(false);
        panel.getChildren().add(resultSection);

        return panel;
    }

    // ── Sport Selection Section ───────────────────────────────────────────────
    private VBox buildSportSection() {
        VBox section = new VBox(12);
        section.setStyle("-fx-background-color: #16213e; -fx-background-radius: 10; "
                + "-fx-border-color: #0f3460; -fx-border-radius: 10; -fx-border-width: 1;");
        section.setPadding(new Insets(18));

        Label heading = styledHeading("Step 1 — Choose a Sport");
        Label sub = styledSub("Pick a sport to load two random teams.");

        sportBox = new ComboBox<>();
        sportBox.getItems().addAll("Basketball", "Football", "Soccer");
        sportBox.setPromptText("Select sport...");
        sportBox.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; "
                + "-fx-font-size: 14px; -fx-pref-width: 200; -fx-cursor: hand;");

        Button loadBtn = new Button("Load Matchup →");
        styleButton(loadBtn, "#e94560");
        loadBtn.setOnAction(e -> onSportPicked());

        HBox row = new HBox(12, sportBox, loadBtn);
        row.setAlignment(Pos.CENTER_LEFT);

        section.getChildren().addAll(heading, sub, row);
        return section;
    }

    // ── Bet Section ───────────────────────────────────────────────────────────
    private VBox buildBetSection() {
        VBox section = new VBox(16);
        section.setStyle("-fx-background-color: #16213e; -fx-background-radius: 10; "
                + "-fx-border-color: #0f3460; -fx-border-radius: 10; -fx-border-width: 1;");
        section.setPadding(new Insets(18));

        Label heading = styledHeading("Step 2 — Pick Your Team & Place Your Bet");

        // ── Matchup display
        GridPane matchup = new GridPane();
        matchup.setHgap(20);
        matchup.setVgap(8);
        matchup.setAlignment(Pos.CENTER);

        teamALabel = new Label("Team A");
        teamALabel.setStyle("-fx-text-fill: #e2e2e2; -fx-font-size: 15px; "
                + "-fx-font-weight: bold; -fx-font-family: 'Segoe UI';");
        teamARatingLabel = new Label("Rating: --");
        teamARatingLabel.setStyle("-fx-text-fill: #a8ff78; -fx-font-size: 13px;");

        Label vs = new Label("VS");
        vs.setStyle("-fx-text-fill: #e94560; -fx-font-size: 22px; -fx-font-weight: bold;");

        teamBLabel = new Label("Team B");
        teamBLabel.setStyle("-fx-text-fill: #e2e2e2; -fx-font-size: 15px; "
                + "-fx-font-weight: bold; -fx-font-family: 'Segoe UI';");
        teamBRatingLabel = new Label("Rating: --");
        teamBRatingLabel.setStyle("-fx-text-fill: #a8ff78; -fx-font-size: 13px;");

        matchup.add(new VBox(4, teamALabel, teamARatingLabel), 0, 0);
        matchup.add(vs, 1, 0);
        matchup.add(new VBox(4, teamBLabel, teamBRatingLabel), 2, 0);

        // Spread info
        spreadLabel = new Label();
        spreadLabel.setStyle("-fx-text-fill: #f5a623; -fx-font-size: 12px; "
                + "-fx-font-style: italic;");

        // ── Team radio buttons
        teamGroup = new ToggleGroup();
        btnA = new RadioButton("Pick Team A");
        btnA.setToggleGroup(teamGroup);
        btnA.setStyle("-fx-text-fill: #e2e2e2; -fx-font-size: 14px;");
        btnB = new RadioButton("Pick Team B");
        btnB.setToggleGroup(teamGroup);
        btnB.setStyle("-fx-text-fill: #e2e2e2; -fx-font-size: 14px;");

        HBox pickRow = new HBox(24, btnA, btnB);
        pickRow.setAlignment(Pos.CENTER_LEFT);

        // ── Bet type
        Label betTypeLabel = styledSub("Bet Type:");
        betTypeBox = new ComboBox<>();
        betTypeBox.getItems().addAll("Winner Pick", "Spread Pick");
        betTypeBox.setValue("Winner Pick");
        betTypeBox.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; "
                + "-fx-font-size: 13px; -fx-pref-width: 170; -fx-cursor: hand;");
        betTypeBox.setOnAction(e -> updateSpreadLabel());

        HBox betTypeRow = new HBox(10, betTypeLabel, betTypeBox);
        betTypeRow.setAlignment(Pos.CENTER_LEFT);

        // ── Wager amount
        Label amountLabel = styledSub("Wager (pts):");
        amountField = new TextField();
        amountField.setPromptText("e.g. 100");
        amountField.setMaxWidth(120);
        amountField.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; "
                + "-fx-font-size: 14px; -fx-prompt-text-fill: #888;");

        HBox amountRow = new HBox(10, amountLabel, amountField);
        amountRow.setAlignment(Pos.CENTER_LEFT);

        // ── Simulate button
        Button simBtn = new Button("▶  Simulate Game");
        styleButton(simBtn, "#0f3460");
        simBtn.setStyle(simBtn.getStyle() + " -fx-border-color: #e94560; "
                + "-fx-border-width: 2; -fx-border-radius: 6;");
        simBtn.setOnAction(e -> onSimulate());

        section.getChildren().addAll(
                heading, matchup, spreadLabel,
                new Separator(), pickRow, betTypeRow, amountRow, simBtn
        );
        return section;
    }

    // ── Result Section ────────────────────────────────────────────────────────
    private VBox buildResultSection() {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: #16213e; -fx-background-radius: 10; "
                + "-fx-border-color: #0f3460; -fx-border-radius: 10; -fx-border-width: 1;");
        section.setPadding(new Insets(18));

        Label heading = styledHeading("Game Result");
        resultLabel = new Label();
        resultLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        resultDetailLabel = new Label();
        resultDetailLabel.setStyle("-fx-text-fill: #c0c0c0; -fx-font-size: 14px; "
                + "-fx-wrap-text: true;");

        section.getChildren().addAll(heading, resultLabel, resultDetailLabel);
        return section;
    }

    // ── History Panel ─────────────────────────────────────────────────────────
    private VBox buildHistoryPanel() {
        VBox panel = new VBox(10);
        panel.setPrefWidth(290);
        panel.setStyle("-fx-background-color: #0f3460; -fx-border-color: #e94560; "
                + "-fx-border-width: 0 0 0 2;");
        panel.setPadding(new Insets(18, 14, 18, 14));

        Label heading = new Label("📋 Match History");
        heading.setStyle("-fx-text-fill: #e94560; -fx-font-size: 15px; "
                + "-fx-font-weight: bold; -fx-font-family: 'Segoe UI';");

        historyArea = new TextArea("No match history yet.");
        historyArea.setEditable(false);
        historyArea.setWrapText(true);
        historyArea.setStyle("-fx-background-color: #1a1a2e; -fx-text-fill: #c0c0c0; "
                + "-fx-font-size: 12px; -fx-control-inner-background: #1a1a2e;");
        VBox.setVgrow(historyArea, Priority.ALWAYS);

        panel.getChildren().addAll(heading, historyArea);
        return panel;
    }

    // ── Event Handlers ────────────────────────────────────────────────────────

    /**
     * Handles sport selection: loads teams via controller and reveals the bet section.
     */
    private void onSportPicked() {
        String sport = sportBox.getValue();
        if (sport == null) {
            showError("Please select a sport first.");
            return;
        }
        Team[] teams = controller.loadTeams(sport);

        // Update team display labels
        teamALabel.setText(teams[0].getName());
        teamARatingLabel.setText("Rating: " + teams[0].getRating());
        teamBLabel.setText(teams[1].getName());
        teamBRatingLabel.setText("Rating: " + teams[1].getRating());

        // Update radio button labels
        btnA.setText("Pick: " + teams[0].getName());
        btnB.setText("Pick: " + teams[1].getName());
        teamGroup.selectToggle(null);
        amountField.clear();

        // Update spread label
        updateSpreadLabel();

        // Show bet section
        betSection.setVisible(true);
        betSection.setManaged(true);
        resultSection.setVisible(false);
        resultSection.setManaged(false);
    }

    /**
     * Handles bet simulation: validates inputs, calls controller.simulate(), displays result.
     */
    private void onSimulate() {
        // Validate team pick
        if (teamGroup.getSelectedToggle() == null) {
            showError("Please pick a team to bet on.");
            return;
        }
        Team[] teams = controller.getCurrentTeams();
        String pickedTeam = (teamGroup.getSelectedToggle() == btnA)
                ? teams[0].getName()
                : teams[1].getName();

        String betType = betTypeBox.getValue();
        String amountText = amountField.getText();

        try {
            GameResult result = controller.simulate(pickedTeam, betType, amountText);
            displayResult(result);
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    /**
     * Handles the reset button: resets controller state and refreshes the UI.
     */
    private void onReset() {
        controller.reset();
        updateBalanceLabel();
        historyArea.setText("No match history yet.");
        betSection.setVisible(false);
        betSection.setManaged(false);
        resultSection.setVisible(false);
        resultSection.setManaged(false);
        sportBox.setValue(null);
        amountField.clear();
    }

    // ── UI Helpers ────────────────────────────────────────────────────────────

    /** Displays the game result and updates balance + history. */
    private void displayResult(GameResult result) {
        if (result.isPlayerWon()) {
            resultLabel.setText("✅ YOU WON! +" + result.getPointsDelta() + " pts");
            resultLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; "
                    + "-fx-text-fill: #a8ff78;");
        } else {
            resultLabel.setText("❌ YOU LOST! " + result.getPointsDelta() + " pts");
            resultLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; "
                    + "-fx-text-fill: #e94560;");
        }

        String detail = String.format(
                "%s %d – %d %s%nWinner: %s%nBet Type: %s | Picked: %s",
                result.getTeamA(), result.getScoreA(),
                result.getScoreB(), result.getTeamB(),
                result.getWinner(),
                result.getBetTypeName(),
                result.getTeamA().equals(result.getWinner()) && result.isPlayerWon()
                        ? result.getTeamA() : result.getTeamB()
        );
        resultDetailLabel.setText(detail);

        resultSection.setVisible(true);
        resultSection.setManaged(true);

        updateBalanceLabel();
        historyArea.setText(controller.getHistory());
    }

    /** Keeps the balance label in sync with the controller. */
    private void updateBalanceLabel() {
        int bal = controller.getBalance();
        balanceLabel.setText("Balance: " + bal + " pts");
        if (bal < 100) {
            balanceLabel.setStyle("-fx-text-fill: #e94560; -fx-font-size: 16px; "
                    + "-fx-font-weight: bold;");
        } else {
            balanceLabel.setStyle("-fx-text-fill: #a8ff78; -fx-font-size: 16px; "
                    + "-fx-font-weight: bold;");
        }
    }

    /** Updates the spread info label when the bet type or teams change. */
    private void updateSpreadLabel() {
        Team[] teams = controller.getCurrentTeams();
        if (teams == null) return;
        if ("Spread Pick".equals(betTypeBox.getValue())) {
            int spread = SpreadBetStrategy.calculateSpread(teams[0], teams[1]);
            spreadLabel.setText("Spread: " + spread + " pts | "
                    + (teams[0].getRating() >= teams[1].getRating()
                        ? teams[0].getName() + " favored by " + spread
                        : teams[1].getName() + " favored by " + spread));
        } else {
            spreadLabel.setText("Pick the team you think will win outright.");
        }
    }

    /** Shows an error alert dialog with the given message. */
    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /** Returns a styled heading label. */
    private Label styledHeading(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: #e2e2e2; -fx-font-size: 16px; "
                + "-fx-font-weight: bold; -fx-font-family: 'Segoe UI';");
        return lbl;
    }

    /** Returns a styled sub-label. */
    private Label styledSub(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: #a0a0a0; -fx-font-size: 13px;");
        return lbl;
    }

    /** Applies a consistent button style. */
    private void styleButton(Button btn, String bgColor) {
        btn.setStyle("-fx-background-color: " + bgColor + "; -fx-text-fill: white; "
                + "-fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand; "
                + "-fx-background-radius: 6; -fx-padding: 8 18 8 18;");
    }
}

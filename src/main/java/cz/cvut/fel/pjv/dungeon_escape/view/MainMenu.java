package cz.cvut.fel.pjv.dungeon_escape.view;

import cz.cvut.fel.pjv.dungeon_escape.controller.GameController;
import cz.cvut.fel.pjv.dungeon_escape.model.Game;
import cz.cvut.fel.pjv.dungeon_escape.model.GameState;
import cz.cvut.fel.pjv.dungeon_escape.model.save_load.SaveLoad;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Handles the game's main menu UI and navigation.
 * Manages game state transitions between menu and gameplay.
 */
public class MainMenu {
  private  Scene menuScene;
  private  Scene gameScene;
  private final Stage primaryStage;
  private Game game;
  private final GameController controller;
  private boolean loggingEnabled = true;
  /**
   * Creates the main menu with game references.
   * @param primaryStage The main application window
   * @param game The game instance to control
   * @param controller The game controller for state management
   */
  public MainMenu(Stage primaryStage, Game game, GameController controller) {
    this.primaryStage = primaryStage;
    this.game = game;
    this.controller = controller;
    createMenu();
  }

  /**
   * Sets the gameplay scene reference for later use.
   * @param gameScene The active game scene to switch to
   */
  public void setGameScene(Scene gameScene) {
    this.gameScene = gameScene;
  }

  /**
   * Builds the menu UI with all buttons and styling.
   */
  private void createMenu() {
    // Title label
    Label title = new Label("Dungeon Escape");
    title.setStyle("-fx-font-size: 48px; -fx-text-fill: white;");

    // Menu buttons
    Button newGameButton = createMenuButton("New Game", this::startNewGame);
    Button continueGameButton = createMenuButton("Continue Game", this::continueGame);
    Button exitButton = createMenuButton("Exit", Platform::exit);
    continueGameButton.setDisable(controller.getState() == GameState.GAME_OVER);

    // Logger toggle button
    Button loggerToggleButton = new Button("Logging: ON");
    loggerToggleButton.setStyle("-fx-font-size: 24px; -fx-min-width: 200px;");
    loggerToggleButton.setOnAction(e -> toggleLogging(loggerToggleButton));

    // Layout configuration
    VBox menuBox = new VBox(title, newGameButton, loggerToggleButton, continueGameButton, exitButton);
    menuBox.setAlignment(Pos.CENTER);
    menuBox.setSpacing(10);

    StackPane root = new StackPane();
    root.setStyle("-fx-background-color: #222;");
    root.getChildren().add(menuBox);
    menuScene = new Scene(root, 1024, 800);
  }

  /**
   * Helper to create styled menu buttons.
   * @param text Button display text
   * @param action Click handler
   * @return Configured Button instance
   */
  private Button createMenuButton(String text, Runnable action) {
    Button btn = new Button(text);
    btn.setStyle("-fx-font-size: 24px; -fx-min-width: 200px;");
    btn.setOnAction(e -> action.run());
    return btn;
  }

  /**
   * Starts a new game from level 1.
   * Resets all game state before beginning.
   */
  private void startNewGame() {
    game.reloadLevel("level1.json");
    controller.setState(GameState.RUNNING);
    primaryStage.setScene(gameScene);
  }

  /**
   * Attempts to continue saved game.
   * Only works if valid save exists.
   */
  private void continueGame() {
    SaveLoad saveLoad = new SaveLoad(game);
    if (saveLoad.hasSavedGame() && saveLoad.loadGame()) {
      controller.setState(GameState.RUNNING);
      primaryStage.setScene(gameScene);
    }
  }

  /**
   * Displays the menu scene and pauses the game.
   */
  public void show() {
    primaryStage.setScene(menuScene);
    controller.setState(GameState.PAUSED);
  }

  /**
   * Toggles debug logging on/off.
   * Updates button text to reflect current state.
   * @param button The logging toggle button
   */
  private void toggleLogging(Button button) {
    loggingEnabled = !loggingEnabled;

    Logger rootLogger = Logger.getLogger("");
    Level level = loggingEnabled ? Level.INFO : Level.OFF;

    // Update all handlers
    for (Handler handler : rootLogger.getHandlers()) {
      handler.setLevel(level);
    }

    button.setText("Logging: " + (loggingEnabled ? "ON" : "OFF"));
  }
}

package cz.cvut.fel.pjv.dungeon_escape.view;

import cz.cvut.fel.pjv.dungeon_escape.controller.GameController;
import cz.cvut.fel.pjv.dungeon_escape.controller.InputHandler;
import cz.cvut.fel.pjv.dungeon_escape.model.Game;
import cz.cvut.fel.pjv.dungeon_escape.model.GameState;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainMenu {
  private  Scene menuScene;
  private  Scene gameScene;
  private final Stage primaryStage;
  private Game game;
  private final GameController controller;

  public MainMenu(Stage primaryStage, Game game, GameController controller) {
    this.primaryStage = primaryStage;
    this.game = game;
    this.controller = controller;
    createMenu();
  }
  public void setGameScene(Scene gameScene) {
    this.gameScene = gameScene;
  }
  private void createMenu() {
    // Create elements in menu
    Label title = new Label("Dungeon Escape");
    title.setStyle("-fx-font-size: 48px; -fx-text-fill: white;");

    Button newGameButton = createNewButton("New Game", this::startNewGame);
    Button continueGameButton = createNewButton("Continue Game", this::continueGame);
    Button exitButton = createNewButton("Exit", Platform::exit);
    continueGameButton.setDisable(controller.getState() == GameState.GAME_OVER);

    VBox menuBox = new VBox(title, newGameButton, continueGameButton, exitButton);
    menuBox.setAlignment(Pos.CENTER);

    StackPane root = new StackPane();
    root.setStyle("-fx-background-color: #222;");
    root.getChildren().add(menuBox);
    menuScene = new Scene(root, 1024, 800);

  }



  private Button createNewButton(String text, Runnable action) {
    Button btn = new Button(text);
    btn.setStyle("-fx-font-size: 24px; -fx-min-width: 200px;");
    btn.setOnAction(e -> action.run());
    return btn;
  }

  private void startNewGame() {
    game.reset();
    game = new Game();
    controller.setState(GameState.RUNNING);
    primaryStage.setScene(gameScene);
  }

  private void continueGame() {
    if(controller.loadGame()){
      controller.setState(GameState.RUNNING);
      primaryStage.setScene(gameScene);
    }
  }

  public void show() {
    primaryStage.setScene(menuScene);
    controller.setState(GameState.PAUSED);
  }

}

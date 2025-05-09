package cz.cvut.fel.pjv.dungeon_escape.view;

import cz.cvut.fel.pjv.dungeon_escape.controller.GameController;
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
  private final Scene mainMenuScene;
  private final Stage primaryStage;
  private final Scene gameScene;
  private final Game game;
  private final GameController controller;

  public MainMenu(Stage primaryStage, Scene gameScene, Game game, GameController controller) {
    this.primaryStage = primaryStage;
    this.gameScene = gameScene;
    this.game = game;
    this.controller = controller;

    // Create elements in menu
    Label title = new Label("Dungeon Escape");
    title.setFont(Font.font("Arial", FontWeight.BOLD, 40));
    title.setTextFill(Color.WHEAT);

    Button playGameButton = new Button("Continue Game");
    playGameButton.setFont(Font.font(18));
    playGameButton.setPrefWidth(200);

    Button newGameButton = new Button("New Game");
    newGameButton.setFont(Font.font(18));
    newGameButton.setPrefWidth(200);

    Button exitButton = new Button("Exit");
    exitButton.setFont(Font.font(18));
    exitButton.setPrefWidth(200);

    // action hadnler
    playGameButton.setOnAction(event -> {
      if (controller.getState() == GameState.PAUSED) {
        controller.setState(GameState.RUNNING);
        primaryStage.setScene(gameScene);
      }
    });

    newGameButton.setOnAction(event -> {
      game.reset(); // Метод для сброса состояния игры
      controller.setState(GameState.RUNNING);
      primaryStage.setScene(gameScene);
    });

    exitButton.setOnAction(event -> Platform.exit());

    // Init box
    VBox menuLayout = new VBox(20);
    menuLayout.setAlignment(Pos.CENTER);
    menuLayout.getChildren().addAll(
      title,
      playGameButton,
      newGameButton,
      exitButton
    );

    // Background menu
    StackPane root = new StackPane();
    ImageView background = new ImageView(new Image("backround.jpg"));
    root.getChildren().addAll(background, menuLayout);

    mainMenuScene = new Scene(root, 1024, 780);

    // Styles
    String buttonStyle = "-fx-background-color: #3a3a3a; -fx-text-fill: white; -fx-border-color: #5a5a5a;";
    playGameButton.setStyle(buttonStyle);
    newGameButton.setStyle(buttonStyle);
    exitButton.setStyle(buttonStyle);
  }

  public void show() {
    primaryStage.setScene(mainMenuScene);
  }

  public Scene getScene() {
    return mainMenuScene;
  }
}

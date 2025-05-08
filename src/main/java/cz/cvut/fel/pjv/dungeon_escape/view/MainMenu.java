package cz.cvut.fel.pjv.dungeon_escape.view;

import cz.cvut.fel.pjv.dungeon_escape.model.Game;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu {
  private final Scene mainMenuScene;
  private final Stage mainMenuStage;
  private final Scene gameScene;
  private final Game game;

  public MainMenu(Stage mainMenuStage, Scene gameScene, Game game) {
    this.mainMenuStage = mainMenuStage;
    this.gameScene = gameScene;
    this.game = game;

    Button playGameButton = new Button("Play Game");
    playGameButton.setOnAction(event -> {
      mainMenuStage.setScene(gameScene);
    });
    Button newGameButton = new Button("New Game");
    Button exitButton = new Button("Exit");


    VBox root = new VBox();
    root.setSpacing(10);
    root.setAlignment(Pos.CENTER);
    mainMenuScene = new Scene(root, 1024, 780);

  }
  public void show(){
    mainMenuStage.setScene(mainMenuScene);
  }

}

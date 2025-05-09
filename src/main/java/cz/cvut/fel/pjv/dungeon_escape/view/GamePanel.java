package cz.cvut.fel.pjv.dungeon_escape.view;

import cz.cvut.fel.pjv.dungeon_escape.controller.GameController;
import cz.cvut.fel.pjv.dungeon_escape.controller.InputHandler;
import cz.cvut.fel.pjv.dungeon_escape.model.DrawableItem;
import cz.cvut.fel.pjv.dungeon_escape.model.Game;
import cz.cvut.fel.pjv.dungeon_escape.model.GameState;
import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.EnumMap;
import java.util.Map;


public class GamePanel extends Application {
  Map<ImageId, Image> gameImages = new EnumMap<>(ImageId.class);

  @Override
  public void start(Stage stage){
    loadImages();

    double sceneWidth = ImageId.BGR.getWidth();
    double sceneHeight = ImageId.BGR.getHeight();
//    Init canvas
    Canvas canvas = new Canvas(sceneWidth, sceneHeight);
    StackPane root = new StackPane(canvas);
    Scene scene = new Scene(root, sceneWidth, sceneHeight);

// Init model and controller
    Game game = new Game();
    GameController controller = new GameController(game);
    InputHandler inputHandler = new InputHandler(scene, controller);
    controller.setInputHandler(inputHandler);

    MainMenu mainMenu = new MainMenu(stage, scene, game, controller);

    scene.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ESCAPE) {
        controller.setState(GameState.PAUSED);
        mainMenu.show();
      }
    });
    startGameLoop(canvas, game,controller);

    stage.setTitle("Dungeon Escape");
    stage.setScene(scene);
    stage.show();

  }
  private void loadImages() {
    for (ImageId imgId : ImageId.values()) {
      Image image = new Image(imgId.getFileName());
      imgId.setWidth(image.getWidth());
      imgId.setHeight(image.getHeight());
      gameImages.put(imgId, image);
    }
  }

  private void drawItems(Canvas canvas, Game game) {
    GraphicsContext gc = canvas.getGraphicsContext2D();

    for (DrawableItem di : game.getItemsToDraw())
      gc.drawImage(gameImages.get(di.imageId()), di.x(), di.y());

  }


  private void startGameLoop(Canvas canvas, Game game, GameController controller) {
    AnimationTimer gameLoop = new AnimationTimer() {
      @Override
      public void handle(long now) {
        controller.update();
        drawItems(canvas, game);
      }
    };
    gameLoop.start();
  }

}

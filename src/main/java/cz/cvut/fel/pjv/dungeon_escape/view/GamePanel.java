package cz.cvut.fel.pjv.dungeon_escape.view;

import cz.cvut.fel.pjv.dungeon_escape.controller.InputHandler;
import cz.cvut.fel.pjv.dungeon_escape.model.DrawableItem;
import cz.cvut.fel.pjv.dungeon_escape.model.Game;
import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.EnumMap;
import java.util.Map;


public class GamePanel extends Application {
  Map<ImageId, Image> gameImages = new EnumMap<>(ImageId.class);

  private InputHandler inputHandler;


  @Override
  public void start(Stage stage){
    loadImages();

    double sceneWidth = ImageId.BGR.getWidth();
    double sceneHeight = ImageId.BGR.getHeight();

    Game game = new Game();
    Canvas canvas = new Canvas(sceneWidth, sceneHeight);
    startGameLoop(canvas, game);

    StackPane root = new StackPane(canvas);
    Scene scene = new Scene(root, sceneWidth, sceneHeight);
    inputHandler = new InputHandler(scene,game);

    MainMenu mainMenu = new MainMenu(stage, scene, game);


//    player = new Player(ImageId., 10,10,10);

    stage.setTitle("Preview");
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
//    gc.clearRect(0, 0, bgetWidth(), getHeight());

    for (DrawableItem di : game.getItemsToDraw())
      gc.drawImage(gameImages.get(di.imageId()), di.x(), di.y());

//    gc.fillRect(player.getX(), player.getY(), 30, 30);

  }


  private void startGameLoop(Canvas canvas, Game game) {
    AnimationTimer gameLoop = new AnimationTimer() {
      @Override
      public void handle(long now) {
        game.update();
        drawItems(canvas, game);
      }
    };
    gameLoop.start();
  }

  public static void main(String[] args) {
    launch();
  }
}

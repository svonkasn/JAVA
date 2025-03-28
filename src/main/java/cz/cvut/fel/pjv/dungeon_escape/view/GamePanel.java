package cz.cvut.fel.pjv.dungeon_escape.view;

import cz.cvut.fel.pjv.dungeon_escape.controller.InputHandler;
import cz.cvut.fel.pjv.dungeon_escape.model.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;



public class GamePanel extends Application {
  private final Image backgroundImage = new Image("backround.jpg");

  private final double width = backgroundImage.getWidth();
  private final double height = backgroundImage.getHeight();

  private final Canvas canvas = new Canvas(width, height);
  private GraphicsContext gc;
  private InputHandler inputHandler;
  private Player player;


  private double playerX = 100; // Pozice hráče
  private double playerY = 100;
  private double speed = 2.0; // Rychlost pohybu


  @Override
  public void start(Stage stage){
    gc = canvas.getGraphicsContext2D();
    StackPane root = new StackPane(canvas);

    player = new Player(10,10,10);
//    root.setOnKeyPressed(inputHandler::keyPressed);
//    root.setOnKeyReleased(inputHandler::keyReleased);


    startGameLoop();

    Scene scene = new Scene(root, width, height);
    inputHandler = new InputHandler(scene);

    stage.setTitle("Preview");
    stage.setScene(scene);
    stage.show();
    startGameLoop();

  }

  private void draw() {
    gc.clearRect(0, 0, width, height);
    gc.drawImage(backgroundImage, 0, 0);
    gc.fillRect(playerX, playerY, 30, 30);
  }
  private void update() {
    player.move(
      inputHandler.isUpPressed(),
      inputHandler.isDownPressed(),
      inputHandler.isLeftPressed(),
      inputHandler.isRightPressed()
    );
//    if (inputHandler.isUpPressed()) {
//      System.out.println("Up");
//      //
//    }
//    if (inputHandler.isDownPressed()) {
//      System.out.println("Down");
//      //
//    }
//    if (inputHandler.isLeftPressed()) {
//      System.out.println("Left");
//      //
//    }
//    if (inputHandler.isRightPressed()) {
//      System.out.println("Right");
//    }
//    player.move(dx, dy);
//    playerX += speed;
//    if (playerX > width) playerX = 0;
  }

  private void startGameLoop() {
    AnimationTimer gameLoop = new AnimationTimer() {
      @Override
      public void handle(long now) {
        update();
        draw();
      }
    };
    gameLoop.start();
  }

  public static void main(String[] args) {
    launch();
  }
}

package cz.cvut.fel.pjv.dungeon_escape;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class HelloApplication extends Application {
  private final Image backgroundImage = new Image("backround.jpg");

  private final double width = backgroundImage.getWidth();
  private final double height = backgroundImage.getHeight();

  private final Canvas canvas = new Canvas(width, height);

  @Override
  public void start(Stage stage){
    StackPane root = new StackPane(canvas);

    drawBackround();

    Scene scene = new Scene(root, width, height);
    stage.setTitle("Preview");
    stage.setScene(scene);
    stage.show();
  }
  private void drawBackround(){
    GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.drawImage(backgroundImage, 0,0);
  }

  public static void main(String[] args) {
    launch();
  }
}

package cz.cvut.fel.pjv.dungeon_escape.view;

import cz.cvut.fel.pjv.dungeon_escape.controller.GameController;
import cz.cvut.fel.pjv.dungeon_escape.controller.InputHandler;
import cz.cvut.fel.pjv.dungeon_escape.model.DrawableItem;
import cz.cvut.fel.pjv.dungeon_escape.model.Game;
import cz.cvut.fel.pjv.dungeon_escape.model.GameState;
import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Enemy;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Monster;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;


public class GamePanel extends Application {
  private static final Logger logger = Logger.getLogger(Game.class.getName());

  Map<ImageId, Image> gameImages = new EnumMap<>(ImageId.class);
  private MainMenu mainMenu;
  private GameController controller;
  private Game game;

  @Override
  public void start(Stage stage){
    logger.info("Game started");
    loadImages();
// Init model and controller
    game = new Game();
    controller = new GameController(game);

    createGameScene();

    mainMenu = new MainMenu(stage,game, controller);
    mainMenu.show();

    Scene gameScene = createGameScene();

    // Set inputHandler
    InputHandler inputHandler = new InputHandler(gameScene);
    controller.setInputHandler(inputHandler);

    mainMenu.setGameScene(gameScene);
    stage.setTitle("Dungeon Escape");
    stage.show();

  }

  private void drawMonsterAttackEffect(GraphicsContext gc, Enemy monster) {
    if (!monster.isAttacking()) return;

    long attackDuration = System.currentTimeMillis() - monster.getAttackStartTime();
    double progress = Math.min(1.0, attackDuration / 1000.0); // normalize time attack (0-1)
    double alpha = 1.0 - progress * 0.8; //

    // X, Y center of monster
    double centerX = monster.getX() + ImageId.MONSTER.getWidth() / 2;
    double centerY = monster.getY() + ImageId.MONSTER.getHeight() / 2;

    // Triangle
    gc.setFill(new Color(1, 0, 0, alpha));

    if (monster.getAttackDirection() > 0) {
      // to the Right attack
      gc.fillPolygon(
        new double[]{centerX + 30, centerX + 50, centerX + 30},
        new double[]{centerY - 10, centerY, centerY + 10},
        3
      );
    } else {
      // to the Left
      gc.fillPolygon(
        new double[]{centerX - 30, centerX - 50, centerX - 30},
        new double[]{centerY - 10, centerY, centerY + 10},
        3
      );
    }

    // effect "wave" (gradually expanding)
    if (attackDuration < 500) {
      double waveSize = attackDuration / 500.0 * 40;
      gc.setStroke(new Color(1, 0.5, 0.5, alpha * 0.7));
      gc.setLineWidth(2);
      gc.strokeOval(
        centerX - waveSize/2,
        centerY - waveSize/2,
        waveSize,
        waveSize
      );
    }
  }
  private Scene createGameScene() {
    double sceneWidth = ImageId.BGR.getWidth();
    double sceneHeight = ImageId.BGR.getHeight();

    Canvas canvas = new Canvas(sceneWidth, sceneHeight);
    StackPane root = new StackPane(canvas);
    Scene gameScene = new Scene(root, sceneWidth, sceneHeight);

    // ESC input
    gameScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
      if (event.getCode() == KeyCode.ESCAPE) {
        if (controller.getState() == GameState.RUNNING) {
          controller.setState(GameState.PAUSED);
          controller.saveGame();
          mainMenu.show();
        }
        event.consume(); // Stop
      }
    });

    startGameLoop(canvas, game, controller);
    return gameScene;
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

    Enemy monster = game.getMonster();
    if (monster != null && monster.isAttacking()) {
      drawMonsterAttackEffect(gc, monster);
    }
  }

  private void drawHealth(GraphicsContext gc) {
    double maxHealth = 10;
    double currentHealth = game.getPlayer().getHealth();
    double barWidth = 200;
    double barHeight = 20;
    double x = 380;
    double y = 20;

    // draw box
    gc.setStroke(Color.BLACK);
    gc.strokeRect(x, y, barWidth, barHeight);

    // current health
    double healthRatio = currentHealth / maxHealth;
    gc.setFill(Color.LIGHTGREEN);
    if (healthRatio < 0.3)
      gc.setFill(Color.RED);
    gc.fillRect(x, y, barWidth * healthRatio, barHeight);
  }


  private void startGameLoop(Canvas canvas, Game game, GameController controller) {
    AnimationTimer gameLoop = new AnimationTimer() {
      @Override
      public void handle(long now) {
        if (controller.getState() == GameState.RUNNING) {
          controller.update();
          drawItems(canvas, game);
          drawHealth(canvas.getGraphicsContext2D());
        }
        if(controller.getState() == GameState.GAME_OVER){
          mainMenu.show();
        }
      }
    };
    gameLoop.start();
  }

}

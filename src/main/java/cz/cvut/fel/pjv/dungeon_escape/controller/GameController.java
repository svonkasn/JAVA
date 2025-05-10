package cz.cvut.fel.pjv.dungeon_escape.controller;

import cz.cvut.fel.pjv.dungeon_escape.model.Game;
import cz.cvut.fel.pjv.dungeon_escape.model.GameItem;
import cz.cvut.fel.pjv.dungeon_escape.model.GameState;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Player;
import javafx.geometry.BoundingBox;


public class GameController {
  private Game game;
  private InputHandler inputHandler;
  private GameState state = GameState.RUNNING;

  public GameController(Game game) {
    this.game = game;
  }

  public void setInputHandler(InputHandler inputHandler) {
    this.inputHandler = inputHandler;
  }

  public GameState getState() {
    return state;
  }
  public void setState(GameState gameState) {
    this.state = gameState;
    game.setGameState(state);
  }

  public void update() {
    if(state == GameState.RUNNING  && inputHandler != null) {
      handleInput();
      game.updatePhysics();
      handleInteractions();
    }
  }
  private void handleInput() {
    game.movePlayer(
      inputHandler.isUpPressed(),
      inputHandler.isDownPressed(),
      inputHandler.isLeftPressed(),
      inputHandler.isRightPressed(),
      inputHandler.isJumpPressed()
    );
  }
  private void handleInteractions(){
    Player player = game.getPlayer();
    BoundingBox playerBounds = player.getBoundingBox();
    game.checkLevelBounds();

//    Collision ... levels
    for (GameItem object : game.getCollidableObjects()) {
      checkObjectCollision(player, object);
    }

    if (!game.isKeyTaken() && playerBounds.intersects(game.getKey().getBoundingBox())) {
      player.addInventory(game.getKey());
      game.setKeyTaken(true);
      System.out.println("Taken key");
    }
    if (playerBounds.intersects(game.getDoor().getBoundingBox())) {
      if (game.getDoor().tryOpen(player)) {
        if (player.getInventory().hasKey()) {
          player.getInventory().useKey();
          player.removeInventory(game.getKey());
          System.out.println("Door opened");
          // TODO: game.nextLevel()
        }
      }
    }
  }

  private void checkObjectCollision(Player player, GameItem object) {
    BoundingBox playerBounds = player.getBoundingBox();
    BoundingBox objectBounds = object.getBoundingBox();

    if (playerBounds.intersects(objectBounds)) {
      // sides overlaping
      double overlapLeft = playerBounds.getMaxX() - objectBounds.getMinX();
      double overlapRight = objectBounds.getMaxX() - playerBounds.getMinX();
      double overlapTop = playerBounds.getMaxY() - objectBounds.getMinY();
      double overlapBottom = objectBounds.getMaxY() - playerBounds.getMinY();
//      System.out.println("Left " + overlapLeft + " Right" + overlapRight);
//      System.out.println("Top " + overlapTop + " Bottom" + overlapBottom);

      // magic math
      double minOverlap = Math.min(Math.min(overlapLeft, overlapRight),
        Math.min(overlapTop, overlapBottom));

      // act depending in side collision
      if (minOverlap == overlapTop) {
        // top
        player.setY((int)(objectBounds.getMinY() - playerBounds.getHeight()));
        player.setOnGround(true);
      } else if (minOverlap == overlapBottom) {
        // down
        player.setY((int)objectBounds.getMaxY());
        player.setSpeed(0);
      } else if (minOverlap == overlapLeft) {
        // left
        player.setX((int)(objectBounds.getMinX() - playerBounds.getWidth()));
      } else if (minOverlap == overlapRight) {
        // right
        player.setX((int)objectBounds.getMaxX());
      }
    }
  }



}

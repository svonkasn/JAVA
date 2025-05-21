package cz.cvut.fel.pjv.dungeon_escape.controller;

import cz.cvut.fel.pjv.dungeon_escape.model.*;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Player;
import cz.cvut.fel.pjv.dungeon_escape.model.environment.Plant;
import javafx.geometry.BoundingBox;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.Iterator;
import java.util.logging.Logger;


public class GameController {
  private static final Logger logger = Logger.getLogger(Game.class.getName());
  private Game game;
  private InputHandler inputHandler;
  private GameState state = GameState.RUNNING;
  private boolean wasInventoryToggled = false;

  private static final String SAVE_FILE = "savegame.json";

  public GameController(Game game) {
    this.game = game;
  }
  public void setInputHandler(InputHandler inputHandler) {
    this.inputHandler = inputHandler;
  }
  public void update() {
    if (inputHandler.shouldToggleInventory() && !wasInventoryToggled) {
      game.toggleInventory();
      wasInventoryToggled = true;
    } else if (!inputHandler.shouldToggleInventory()) {
      wasInventoryToggled = false;
    }

    if(state == GameState.RUNNING  && inputHandler != null) {
      handleInput();
      game.updatePhysics();
      handleInteractions();
    }
//    System.out.println(game.getPlayer());
    if(inputHandler.isAttack() && game.getPlayer().getInventory().hasWeapon()) {
      game.damageEnemies();
      logger.info("Attacking by player");
    }
  }
  private void handleInput() {
    game.movePlayer(
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

//    check health. should not be there
    if(player.getHealth() == 0 && state == GameState.RUNNING){
      setState(GameState.GAME_OVER);
    }

    checkCollectibles(game);
    if (playerBounds.intersects(game.getDoor().getBoundingBox())) {
      if (game.getDoor().tryOpen(player)) {
        if (player.getInventory().hasKey()) {
          player.getInventory().useKey();
          player.removeInventory(game.getKey());
          logger.info("Door opened");
          game.reloadLevel("level2.json");
        }
      }
    }
//    Crafting
    if(game.isNearCraftingPlant() && inputHandler.isCraftingKeyPressed()){
      game.craftPotion();
    }
  }
  private void checkCollectibles(Game game) {
    Iterator<InventoryItem> iterator = game.getItemList().iterator();
    while (iterator.hasNext()) {
      InventoryItem item = iterator.next();
      if (item.getBoundingBox().intersects(game.getPlayer().getBoundingBox())
        && item.canBeCollected()
        && !game.getPlayer().getInventory().isFull()) {

        item.onCollect(game);
        iterator.remove();
        logger.info("Collected item: " + item.getClass().getSimpleName());
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
        player.setSpeed(0);
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
  public void setState(GameState gameState) {
    this.state = gameState;
    game.setGameState(state);
  }
  public GameState getState() {
    return state;
  }
}

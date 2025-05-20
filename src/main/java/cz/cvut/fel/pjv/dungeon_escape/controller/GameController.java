package cz.cvut.fel.pjv.dungeon_escape.controller;

import cz.cvut.fel.pjv.dungeon_escape.model.*;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Player;
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
          game.nextLevel();
          // TODO: game.nextLevel()
        }
      }
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
  public void saveGame() {
    Player player = game.getPlayer();
//    System.out.println("player: " + player.getX() +  ", " + player.getY() +  ", " + player.getHealth() );
//    System.out.println(game.isKeyTaken());
    GameStateData gameData = new GameStateData();

    gameData.setPlayerX(player.getX());
    gameData.setPlayerY(player.getY());
    gameData.setHealthPlayer(player.getHealth());
    gameData.setKeyTaken(game.getKey().isCollected());
    try {
      ObjectMapper om = new ObjectMapper();
      om.writeValue(new File(SAVE_FILE), gameData);
      logger.info("Game saved successfully");
    } catch (IOException e) {
      logger.info("Failed to save game:" + e.getMessage());
    }
  }

  public boolean loadGame() {
    try {
      ObjectMapper om = new ObjectMapper();
      GameStateData gameData = om.readValue(new File(SAVE_FILE), GameStateData.class);
      Player player = game.getPlayer();
      player.setX(gameData.getPlayerX());
      player.setY(gameData.getPlayerY());
      player.setHealth(gameData.getHealthPlayer());
      game.getKey().setCollected(gameData.isKeyTaken());
      logger.info("Game loaded successfully");
      return true;

    } catch (IOException e) {
      logger.info("Failed to load game:" + e.getMessage());
      return false;
    }
  }
  public boolean hasSavedGame() {
    return new File(SAVE_FILE).exists();
  }
  public void setState(GameState gameState) {
    this.state = gameState;
    game.setGameState(state);
  }
  public GameState getState() {
    return state;
  }
}

package cz.cvut.fel.pjv.dungeon_escape.controller;

import com.fasterxml.jackson.databind.SerializationFeature;
import cz.cvut.fel.pjv.dungeon_escape.model.Game;
import cz.cvut.fel.pjv.dungeon_escape.model.GameItem;
import cz.cvut.fel.pjv.dungeon_escape.model.GameState;
import cz.cvut.fel.pjv.dungeon_escape.model.GameStateData;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Player;
import javafx.geometry.BoundingBox;
import com.fasterxml.jackson.databind.ObjectMapper;



import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class GameController {
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
  public GameState getState() {
    return state;
  }
  public void setState(GameState gameState) {
    this.state = gameState;
    game.setGameState(state);
  }
  public double getPlayerHealth(){
    Player player = game.getPlayer();
    return player.getHealth();
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
    if (playerBounds.intersects(game.getSlime().getBoundingBox())) {
      game.getSlime().attack(player);
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
    gameData.setKeyTaken(game.isKeyTaken());
    try {
      ObjectMapper om = new ObjectMapper();
      om.writeValue(new File(SAVE_FILE), gameData);
      System.out.println("Game saved successfully");
    } catch (IOException e) {
      System.err.println("Failed to save game: " + e.getLocalizedMessage());

    }
  }
  public boolean loadGame() {
    try {
      ObjectMapper om = new ObjectMapper();
      GameStateData gameData = om.readValue(new File(SAVE_FILE), GameStateData.class);
      Player player = game.getPlayer();
      player.setX(gameData.getPlayerX());
      player.setY(gameData.getPlayerY());
      game.setKeyTaken(gameData.isKeyTaken());
      System.out.println("Game loaded successfully");
      return true;

    } catch (IOException e) {
      System.err.println("Failed to load game: " + e.getMessage());
      return false;
    }
  }
  public boolean hasSavedGame() {
    return new File(SAVE_FILE).exists();
  }

}

package cz.cvut.fel.pjv.dungeon_escape.controller;

import cz.cvut.fel.pjv.dungeon_escape.model.*;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Player;
import javafx.geometry.BoundingBox;
import java.util.Iterator;
import java.util.logging.Logger;


/**
 * The GameController class manages the game logic, input handling, and interactions
 * between game entities. It acts as the intermediary between the model and input systems.
 */
public class GameController {
  private static final Logger logger = Logger.getLogger(Game.class.getName());
  private Game game;
  private InputHandler inputHandler;
  private GameState state = GameState.RUNNING;
  private boolean wasInventoryToggled = false;

  /**
   * Constructs a GameController with the specified game instance.
   *
   * @param game The Game instance to be controlled by this controller
   */
  public GameController(Game game) {
    this.game = game;
  }

  /**
   * Updates the game state by processing input, physics, and interactions.
   * This method should be called every game tick.
   */
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
    if(inputHandler.isAttack() && game.getPlayer().getInventory().hasWeapon()) {
      game.damageEnemies();
      logger.info("Attacking by player");
    }
  }

  /**
   * Handles player input by translating input states to player movement.
   */
  private void handleInput() {
    game.movePlayer(
      inputHandler.isLeftPressed(),
      inputHandler.isRightPressed(),
      inputHandler.isJumpPressed()
    );
  }

  /**
   * Manages all game interactions including:
   * - Player-object collisions
   * - Collectible item pickup
   * - Door interactions
   * - Health checks
   * - Crafting system
   */
  private void handleInteractions(){
    Player player = game.getPlayer();
    BoundingBox playerBounds = player.getBoundingBox();
    game.checkLevelBounds();

    // Collision ... levels
    for (GameItem object : game.getCollidableObjects()) {
      checkObjectCollision(player, object);
    }

    // check health. should not be there
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
    // Crafting
    if(game.isNearCraftingPlant() && inputHandler.isCraftingKeyPressed()){
      game.craftPotion();
    }
  }

  /**
   * Checks for and processes collectible items that intersect with the player.
   *
   * @param game The current game instance containing items to check
   */
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

  /**
   * Handles collision response between the player and a game object.
   * Adjusts player position based on collision side to prevent overlap.
   *
   * @param player The player entity
   * @param object The game object to check collision against
   */
  private void checkObjectCollision(Player player, GameItem object) {
    BoundingBox playerBounds = player.getBoundingBox();
    BoundingBox objectBounds = object.getBoundingBox();

    if (playerBounds.intersects(objectBounds)) {
      // sides overlaping
      double overlapLeft = playerBounds.getMaxX() - objectBounds.getMinX();
      double overlapRight = objectBounds.getMaxX() - playerBounds.getMinX();
      double overlapTop = playerBounds.getMaxY() - objectBounds.getMinY();
      double overlapBottom = objectBounds.getMaxY() - playerBounds.getMinY();

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
  public void setInputHandler(InputHandler inputHandler) {
    this.inputHandler = inputHandler;
  }
  public GameState getState() {
    return state;
  }
}

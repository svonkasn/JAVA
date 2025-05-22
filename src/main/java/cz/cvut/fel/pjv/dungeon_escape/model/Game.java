package cz.cvut.fel.pjv.dungeon_escape.model;

import cz.cvut.fel.pjv.dungeon_escape.model.entities.Enemy;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Monster;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Player;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Slime;
import cz.cvut.fel.pjv.dungeon_escape.model.environment.Door;
import cz.cvut.fel.pjv.dungeon_escape.model.environment.Plant;
import cz.cvut.fel.pjv.dungeon_escape.model.items.Key;
import cz.cvut.fel.pjv.dungeon_escape.model.items.Potion;
import cz.cvut.fel.pjv.dungeon_escape.model.save_load.LevelLoader;
import javafx.geometry.BoundingBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The Game class represents the core game state and logic.
 * It manages all game entities, physics, collisions, and rendering.
 */
public class Game {
  private static final Logger logger = Logger.getLogger(Game.class.getName());

  private static final String FIRST_LEVEL = "level1.json";

  private GameState gameState = GameState.RUNNING;
  private boolean isInventoryOpen = false;
  // Physics constants
  private final double gravity = 0.1;
  // Game objects
  private final GameItem background = new GameItem(ImageId.BGR, 0, 0);
  private Door door;
  private final GameItem inventory = new GameItem(ImageId.INVENTORY, 0, 0);
  private final Player player;
  private Key key;

  // Entity lists
  private final List<Platforms> platforms = new ArrayList<>();
  private final List<Plant> plants = new ArrayList<>();
  public final List<Enemy> enemyList = new ArrayList<>();
  private final List<InventoryItem> itemList = new ArrayList<>();
  private final List<GameItem> collidableObjects = new ArrayList<>();

  /**
   * Initializes a new game with default player and first level.
   */
  public Game() {
    player = new Player(ImageId.PLAYER, 0, 0, 10, gravity);
  }
  /**
   * Loads a game level from JSON file.
   * @param path Path to the level configuration file
   */
  public void loadLevel(String path) {
    try {
      LevelLoader.loadLevel(this, path);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  /**
   * Adds an enemy to the game world and collision system.
   * @param enemy The enemy to add
   */
  public void addEnemies(Enemy enemy){
    enemyList.add(enemy);
    addCollidableObject(enemy);
  }
  /**
   * Adds a plant to the game world.
   * Big plants are added as scenery, others as collectibles.
   * @param plant The plant to add
   */
  public void addPlant(Plant plant) {
    if(plant.getImageId().equals(ImageId.PLANT_BIG)){
      plants.add(plant);
      return;
    }
    plants.add(plant);
    addInventoryItem(plant);// Small plants are collectibles
  }
  /**
   * Adds a platform to the game world and collision system.
   * @param platform The platform to add
   */
  public void addPlatform(Platforms platform) {
    platforms.add(platform);
    addCollidableObject(platform);
  }
  /**
   * Adds an item to the global item list.
   * @param item The inventory item to add
   */
  public void addInventoryItem(InventoryItem item) {
    itemList.add(item);
  }
  /**
   * Adds an object to the collision detection system.
   * @param gameItem The collidable object to add
   */
  public void addCollidableObject(GameItem gameItem){
    collidableObjects.add(gameItem);
  }
  /**
   * Checks if the player is near a crafting plant (PLANT_BIG).
   * The proximity is determined by a 50x50 unit area around the plant.
   *
   * @return true if player is within crafting range of a big plant, false otherwise
   */
  public boolean isNearCraftingPlant() {
    for (Plant plant : plants) {
      if (plant.getImageId().equals(ImageId.PLANT_BIG)) {
        // Calculate horizontal and vertical distances to plant
        double dx = Math.abs(player.getX() - plant.getX());
        double dy = Math.abs(player.getY() - plant.getY());

        // Check if player is within 50 units in both axes
        if (dx < 50 && dy < 50) {
          return true;
        }
      }
    }
    return false;
  }
  /**
   * Attempts to craft a potion if conditions are met:
   * 1. Player is near a crafting plant (PLANT_BIG)
   * 2. Player has at least 2 plant items in inventory
   *
   * Consumes 2 plants from inventory and adds 1 potion
   *
   * @return true if potion was successfully crafted, false otherwise
   */
  public boolean craftPotion() {
    if (!isNearCraftingPlant()) return false;

    // Filter inventory for plant items and take first 2
    List<InventoryItem> inventoryItems = player.getInventory().getItems();
    List<Plant> flowers = inventoryItems.stream()
      .filter(item -> item instanceof Plant)  // Only plants
      .map(item -> (Plant) item)             // Cast to Plant
      .limit(2)                              // Take max 2 plants
      .collect(Collectors.toList());

    if (flowers.size() < 2) return false;  // Not enough plants

    // Remove used plants from inventory
    for (Plant plant : flowers) {
      player.getInventory().removeItm(plant);
    }

    // Create and add new potion
    Potion potion = new Potion(ImageId.POTION, 0, 0); // Position for Inventory
    player.getInventory().addItm(potion);
    logger.info("Crafted potion!");
    return true;
  }
  /**
   * Damages all enemies colliding with player.
   * Removes enemies that are defeated.
   */
  public void damageEnemies() {
    List<Enemy> toRemove = new ArrayList<>();

    for (Enemy enemy : enemyList) {
      if (player.getBoundingBox().intersects(enemy.getBoundingBox())) {
        enemy.takeDamage(0.5);
        if (enemy.isDead()) {
          toRemove.add(enemy);
        }
      }
    }
    for (Enemy enemy : toRemove) {
      getCollidableObjects().remove(enemy);
      enemyList.remove(enemy);
    }
  }
  // Should be in GameController...?
  /**
   * Checks and enforces level boundaries to prevent player from going out of bounds.
   * Handles collisions with:
   * - Bottom (ground level)
   * - Left/right walls
   * - Top ceiling
   * Adjusts player position and physics state accordingly.
   */
  public void checkLevelBounds() {
    BoundingBox playerBounds = player.getBoundingBox();
    BoundingBox backgroundBounds = background.getBoundingBox();

    // Bottom boundary check (prevent falling through floor)
    if (!player.isOnGround()) {
      if (playerBounds.getMaxY() >= backgroundBounds.getMaxY()) {
        player.setY(backgroundBounds.getMaxY() - playerBounds.getHeight());
        player.setOnGround(true);  // Player lands on ground
      }
    }

    // Side boundaries check (left and right walls)
    if (playerBounds.getMinX() <= backgroundBounds.getMinX()) {
      player.setX(backgroundBounds.getMinX());  // Stop at left edge
    } else if (playerBounds.getMaxX() >= backgroundBounds.getMaxX()) {
      player.setX((backgroundBounds.getMaxX() - playerBounds.getWidth()));  // Stop at right edge
    }

    // Top boundary check (ceiling)
    if (playerBounds.getMinY() <= backgroundBounds.getMinY()) {
      player.setY((int)backgroundBounds.getMinY());
      player.setSpeed(0);  // Stop vertical movement when hitting ceiling
    }
  }
  /**
   * Moves the player based on input states.
   * @param left Whether left movement is requested
   * @param right Whether right movement is requested
   * @param jump Whether jump is requested
   */
  public void movePlayer(boolean left, boolean right, boolean jump) {
    player.move(left, right, jump);
  }
  /**
   * Gathers all drawable game items in proper render order:
   * 1. Background
   * 2. Platforms
   * 3. Door
   * 4. Enemies
   * 5. Collectible items
   * 6. Player
   * 7. Inventory (if open)
   *
   * @return Array of DrawableItems in render order
   */
  public DrawableItem[] getItemsToDraw() {
    List<DrawableItem> items = new ArrayList<>();

    // Background first
    items.add(new DrawableItem(background.getImageId(), background.getX(), background.getY()));

    // Platforms
    for (Platforms platform : platforms) {
      items.add(new DrawableItem(platform.getImageId(), platform.getX(), platform.getY()));
    }

    // Door
    if (door != null) {
      items.add(new DrawableItem(door.getImageId(), door.getX(), door.getY()));
    }

    // Active enemies
    for (Enemy enemy : enemyList) {
      if (!enemy.isDead()) {
        items.add(new DrawableItem(enemy.getImageId(), enemy.getX(), enemy.getY()));
      }
    }

    // Collectible items
    for (InventoryItem item : itemList) {
      if (item.canBeCollected()) {
        items.add(new DrawableItem(item.getImageId(), item.getX(), item.getY()));
      }
    }

    // Plants
    for (Plant plant : plants) {
      if (plant.canBeCollected()) {
        items.add(new DrawableItem(plant.getImageId(), plant.getX(), plant.getY()));
      }
    }

    // Player (above most items but below UI)
    items.add(new DrawableItem(player.getImageId(), player.getX(), player.getY()));

    // Inventory UI (if open)
    if (isInventoryOpen) {
      items.add(new DrawableItem(inventory.getImageId(), inventory.getX(), inventory.getY()));
      for (GameItem item : player.getInventory().getItems()) {
        items.add(new DrawableItem(item.getImageId(), item.getX(), item.getY()));
      }
    }

    return items.toArray(new DrawableItem[0]);
  }
  /**
   * Resets all game entities to their initial state.
   * Restores player health and resets positions.
   */
  public void reset() {
  player.reset();
  player.setHealth(10);
//  player.getInventory().reset();
    // Reset all items
    for (InventoryItem item : itemList) {
      item.setCollected(false);
    item.reset();
  }
  // Reset plants
  for (Plant plant : plants) {
    plant.setCollected(false);
    plant.reset();
  }
  // Reset enemy
  for (Enemy enemy : enemyList) {
    enemy.setHealth(10);
    enemy.reset();
  }
}
  /**
   * Reloads a level from file and resets all game entities.
   * Clears all entity lists before loading the new level.
   *
   * @param path Path to the level JSON file
   */
  public void reloadLevel(String path) {
    // Clear all entity lists
    plants.clear();
    enemyList.clear();
    collidableObjects.clear();
    platforms.clear();

    // Load new level and reset state
    loadLevel(path);
    reset();
  }
  public void updatePhysics(){
    if(gameState == GameState.RUNNING){
      player.update();
      for(Enemy enemy: enemyList){
        enemy.update(player);
      }
    }
  }
  public void toggleInventory() {
    isInventoryOpen = !isInventoryOpen;
  }
  public Player getPlayer() {
    return player;
  }
  public List<InventoryItem> getItemList() {
    return itemList;
  }
  public List<GameItem> getCollidableObjects(){
    return collidableObjects;
  }
  public Enemy getMonster() {
    for (Enemy enemy : enemyList) {
      if (enemy instanceof Monster) {
        return enemy;
      }
    }
    return null;
  }
  public Enemy getSlime() {
    for (Enemy enemy : enemyList) {
      if (enemy instanceof Slime) {
        return enemy;
      }
    }
    return null;
  }
  public Door getDoor(){
    return door;
  }
  public void setKey(Key key) {
    this.key = key;
    addInventoryItem(key);
  }
  public Key getKey() {
    return key;
  }
  public List<Enemy> getEnemyList() {
    return enemyList;
  }
  public void setGameState(GameState gameState) {
    this.gameState = gameState;
  }
  public void setDoor(Door door) {
    this.door = door;
  }

}

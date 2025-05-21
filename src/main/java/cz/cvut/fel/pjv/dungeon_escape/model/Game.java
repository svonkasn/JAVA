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

public class Game {
  private static final Logger logger = Logger.getLogger(Game.class.getName());

  private GameState gameState = GameState.RUNNING;
  private boolean isInventoryOpen = false;

  private final double gravity = 0.1;
  private final GameItem background = new GameItem(ImageId.BGR, 0, 0);
  private Door door;
  private final GameItem inventory = new GameItem(ImageId.INVENTORY, 0, 0);
  private final Player player;
  private Key key;

  private final List<Platforms> platforms = new ArrayList<>();
  private final List<Plant> plants = new ArrayList<>();
  public final List<Enemy> enemyList = new ArrayList<>();
  private final List<InventoryItem> itemList = new ArrayList<>();
  private final List<GameItem> collidableObjects = new ArrayList<>();

  public Game() {
    player = new Player(ImageId.PLAYER, 0, 0, 10, gravity);
    loadLevel("level1.json");
  }

  public void loadLevel(String path) {
    try {
      LevelLoader.loadLevel(this, path);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public void addEnemies(Enemy enemy){
    enemyList.add(enemy);
    addCollidableObject(enemy);
  }
  public void addPlant(Plant plant) {
    if(plant.getImageId().equals(ImageId.PLANT_BIG)){
      plants.add(plant);
      return;
    }
    plants.add(plant);
    addInventoryItem(plant);
  }
  public void addPlatform(Platforms platform) {
    platforms.add(platform);
    addCollidableObject(platform);
  }
  public void addInventoryItem(InventoryItem item) {
    itemList.add(item);
  }
  public void addCollidableObject(GameItem gameItem){
    collidableObjects.add(gameItem);
  }
  public boolean isNearCraftingPlant() {
    for (Plant plant : plants) {
      if (plant.getImageId().equals(ImageId.PLANT_BIG)) {
        double dx = Math.abs(player.getX() - plant.getX());
        double dy = Math.abs(player.getY() - plant.getY());
        if (dx < 50 && dy < 50) {
          return true;
        }
      }
    }
    return false;
  }
  public boolean craftPotion() {
    if (!isNearCraftingPlant()) return false;

    List<InventoryItem> inventoryItems = player.getInventory().getItems();
    List<Plant> flowers = inventoryItems.stream()
      .filter(item -> item instanceof Plant)
      .map(item -> (Plant) item)
      .limit(2)
      .collect(Collectors.toList());

    if (flowers.size() < 2) return false;

    // Delete plants from inventory
    for (Plant plant : flowers) {
      player.getInventory().removeItm(plant);
    }

    // Add potion
    Potion potion = new Potion(ImageId.POTION, 0, 0); // Position for Inventory
    player.getInventory().addItm(potion);
    logger.info("Crafted potion!");
    return true;
  }
  public void updatePhysics(){
    if(gameState == GameState.RUNNING){
      player.update();
      for(Enemy enemy: enemyList){
        enemy.update(player);
      }
    }
  }
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
  public void checkLevelBounds(){
    BoundingBox playerBounds = player.getBoundingBox();
    BoundingBox backgroundBounds = background.getBoundingBox();
//    System.out.println(backgroundBounds.getHeight() + " " + backgroundBounds.getWidth());
    // check down
    if (!player.isOnGround()) {
      if (playerBounds.getMaxY() >= backgroundBounds.getMaxY()) {
        player.setY(backgroundBounds.getMaxY() - playerBounds.getHeight());
        player.setOnGround(true);
      }
    }

    // check wall
    if (playerBounds.getMinX() <= backgroundBounds.getMinX()) {
      player.setX(backgroundBounds.getMinX());
    } else if (playerBounds.getMaxX() >= backgroundBounds.getMaxX()) {
      player.setX((backgroundBounds.getMaxX() - playerBounds.getWidth()));
    }

    // check top
    if (playerBounds.getMinY() <= backgroundBounds.getMinY()) {
      player.setY((int)backgroundBounds.getMinY());
      player.setSpeed(0);
    }
  }
  public void movePlayer(boolean left, boolean right, boolean jump) {
    player.move(left, right, jump);
  }
  public DrawableItem[] getItemsToDraw() {
    List<DrawableItem> items = new ArrayList<>();
    items.add(new DrawableItem(background.getImageId(), background.getX(), background.getY()));
    for (Platforms platform : platforms) {
      items.add(new DrawableItem(platform.getImageId(), platform.getX(), platform.getY()));
    }
    if (door != null) {
      items.add(new DrawableItem(door.getImageId(), door.getX(), door.getY()));
    }
    for (Enemy enemy : enemyList) {
      if (!enemy.isDead()) {
        items.add(new DrawableItem(enemy.getImageId(), enemy.getX(), enemy.getY()));
      }
    }
    for (InventoryItem item : itemList) {
      if (item.canBeCollected()) {
        items.add(new DrawableItem(item.getImageId(), item.getX(), item.getY()));
      }
    }
    for (Plant plant : plants) {
      if (plant.canBeCollected()) {
        items.add(new DrawableItem(plant.getImageId(), plant.getX(), plant.getY()));
      }
    }
    items.add(new DrawableItem(player.getImageId(), player.getX(), player.getY()));

    if (isInventoryOpen) {
      items.add(new DrawableItem(inventory.getImageId(), inventory.getX(), inventory.getY()));
      for (GameItem item : player.getInventory().getItems()) {
        items.add(new DrawableItem(item.getImageId(), item.getX(), item.getY()));
      }
    }
    return items.toArray(new DrawableItem[0]);
  }
  public void reset() {
  player.reset();
  player.setHealth(10);
//  player.getInventory().reset();
    // Reset all items
    for (InventoryItem item : itemList) {
//      System.out.println("Reset item: " + item.getImageId() + " at " + item.getX() + "," + item.getY());
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
  public void reloadLevel(String path) {
//    itemList.clear();
    plants.clear();
    enemyList.clear();
    collidableObjects.clear();
    platforms.clear();
//    player.getInventory().reset();
    loadLevel(path);
    reset();
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

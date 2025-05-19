package cz.cvut.fel.pjv.dungeon_escape.model;

import cz.cvut.fel.pjv.dungeon_escape.model.entities.Enemy;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Monster;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Player;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Slime;
import cz.cvut.fel.pjv.dungeon_escape.model.environment.Door;
import cz.cvut.fel.pjv.dungeon_escape.model.environment.Plant;
import cz.cvut.fel.pjv.dungeon_escape.model.items.Key;
import cz.cvut.fel.pjv.dungeon_escape.model.items.Weapon;
import javafx.geometry.BoundingBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Game {
  private static final Logger logger = Logger.getLogger(Game.class.getName());

  private List<GameItem> collidableObjects = new ArrayList<>();
  List<InventoryItem> itemList = new ArrayList<>();
  List<Enemy> enemyList = new ArrayList<>();


  private final double gravity = 0.1;

  private final GameItem backround;
  private final Player player;
  private final Platforms platform;
  private final Platforms platform2;
  private final Platforms ground;
  private final GameItem inventory;
  private final Key key;
  private final Door door;
  private Enemy slime;
  private Enemy monster;
  private Plant plant1;
  private Plant plant2;
  private Plant plant3;
  private Weapon weapon;

  private GameState gameState = GameState.RUNNING;
  private boolean isInventoryOpen = false;

  public Game() {
    backround = new GameItem(ImageId.BGR, 0, 0);
    platform = new Platforms(ImageId.PLATFORM,0, 0, -25, 350);
    platform2 = new Platforms(ImageId.PLATFORM,0, 0, 630, 170);
    ground = new Platforms(ImageId.GROUND,0, 0, 0, 690);
    inventory = new GameItem(ImageId.INVENTORY, 0,0);
    key = new Key(ImageId.KEY, 900,650,"key1");
    door = new Door(ImageId.DOOR, 900, 65);
    slime = new Slime(ImageId.SLIME, 800, 650 );
    monster = new Monster(ImageId.MONSTER, 100, 600);

    plant1 = new Plant(ImageId.PLANT_BLUE, 30, 650);
    plant2 = new Plant(ImageId.PLANT_GREEN, 10, 320);
    plant3 = new Plant(ImageId.PLANT_BIG, 780, 620);

    weapon = new Weapon(ImageId.WEAPON, 100, 100);

    addCollidableObject(platform2);
    addCollidableObject(platform);
    addCollidableObject(ground);

    addInventoryItem(key);
    addInventoryItem(plant1);
    addInventoryItem(plant2);
    addInventoryItem(weapon);

    addEnemies(slime);
    addEnemies(monster);


    player = new Player(ImageId.PLAYER,0, 0, 10, gravity);
    reset();
  }
  public void addEnemies(Enemy enemy){
    enemyList.add(enemy);
    addCollidableObject(enemy);
  }

  public void addInventoryItem(InventoryItem item) {
    itemList.add(item);
  }
  public void addCollidableObject(GameItem gameItem){
    collidableObjects.add(gameItem);
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
    BoundingBox backgroundBounds = backround.getBoundingBox();
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
    List<DrawableItem> items = new ArrayList<>(Arrays.asList(
      new DrawableItem(backround.getImageId(), backround.getX(), backround.getY()),
      new DrawableItem(platform.getImageId(), platform.getX(), platform.getY()),
      new DrawableItem(platform2.getImageId(), platform2.getX(), platform2.getY()),
      new DrawableItem(door.getImageId(), door.getX(), door.getY()),
      new DrawableItem(ground.getImageId(), ground.getX(), ground.getY()),
      new DrawableItem(player.getImageId(), player.getX(), player.getY()),
      new DrawableItem(plant3.imageId, plant3.getX(), plant3.getY())
      ));
    if(isInventoryOpen){
      logger.info("Inventory open");
      items.add(new DrawableItem(inventory.getImageId(), inventory.getX(), inventory.getY()));
      for (GameItem item : player.getInventory().getItems()) {
        items.add(new DrawableItem(item.getImageId(), item.getX(), item.getY()));
      }
    }

    for (InventoryItem item : itemList) {
      if (item.canBeCollected()) {
        items.add(new DrawableItem(item.getImageId(), item.getX(), item.getY()));
      }
    }
    for(Enemy enemy: enemyList){
      if(!enemy.isDead()){
        logger.info(enemy + " health " + enemy.getHealth());
        items.add(new DrawableItem(enemy.getImageId(), enemy.getX(), enemy.getY()));
      }
    }

    return items.toArray(new DrawableItem[0]);
  }
  public void reset() {
    player.reset();
    player.setHealth(10);
    key.setX(900);
    key.setY(650);
    key.setCollected(false);
    plant1.setCollected(false);
    plant2.setCollected(false);
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
  public Enemy getSlime() {
    return slime;
  }
  public Door getDoor(){
    return door;
  }
  public Key getKey(){
    return key;
  }
  public void setGameState(GameState gameState) {
    this.gameState = gameState;
  }
  public void toggleInventory() {
    isInventoryOpen = !isInventoryOpen;
  }
  public Enemy getMonster() {
    return monster;
  }
}

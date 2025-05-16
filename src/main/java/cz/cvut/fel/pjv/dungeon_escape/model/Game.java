package cz.cvut.fel.pjv.dungeon_escape.model;

import cz.cvut.fel.pjv.dungeon_escape.model.entities.Enemy;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Monster;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Player;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Slime;
import cz.cvut.fel.pjv.dungeon_escape.model.environment.Door;
import cz.cvut.fel.pjv.dungeon_escape.model.environment.Plants;
import cz.cvut.fel.pjv.dungeon_escape.model.items.Key;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
  private List<GameItem> collidableObjects = new ArrayList<>();
  private final double gravity = 0.1;

  private final GameItem backround;
  private final Player player;
  private final Platforms platform;
  private final Platforms platform2;
  private final Platforms ground;
  private final GameItem inventory;
  private final Key key;
  private final Door door;
  private final Slime slime;
  private final Enemy monster;
  private final Plants plant1;
  private final Plants plant2;
  private final Plants plant3;

  private GameState gameState = GameState.RUNNING;
  private boolean isInventoryOpen = false;
  private boolean isTakenKey = false;

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

    plant1 = new Plants(ImageId.PLANT_BLUE, 30, 650);
    plant2 = new Plants(ImageId.PLANT_GREEN, 10, 320);
    plant3 = new Plants(ImageId.PLANT_BIG, 780, 620);

    addCollidableObject(platform2);
    addCollidableObject(platform);
    addCollidableObject(ground);
    addCollidableObject(slime);
    addCollidableObject(monster);

    player = new Player(ImageId.PLAYER,0, 0, 10, gravity);
    reset();
  }


  public void addCollidableObject(GameItem gameItem){
    collidableObjects.add(gameItem);
  }
  public List<GameItem> getCollidableObjects(){
    return collidableObjects;
  }
  public boolean isKeyTaken(){
    return isTakenKey;
  }
  public void updatePhysics(){
    if(gameState == GameState.RUNNING){
      player.update();
      monster.update(player);
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
      new DrawableItem(monster.imageId, monster.getX(), monster.getY()),
      new DrawableItem(plant1.imageId, plant1.getX(), plant1.getY()),
      new DrawableItem(plant2.imageId, plant2.getX(), plant2.getY()),
      new DrawableItem(plant3.imageId, plant3.getX(), plant3.getY()),
      new DrawableItem(slime.getImageId(), slime.getX(), slime.getY())
      ));
    if(isInventoryOpen){
      System.out.println("Inventory open");
      items.add(new DrawableItem(inventory.getImageId(), inventory.getX(), inventory.getY()));
      for (GameItem item : player.getInventory().getItems()) {
        items.add(new DrawableItem(item.getImageId(), item.getX(), item.getY()));
      }
    }

    if (!isTakenKey) {
      items.add(new DrawableItem(key.getImageId(), key.getX(), key.getY()));
    }

    return items.toArray(new DrawableItem[0]);
  }
  public void reset() {
    player.reset();
    player.setHealth(10);
    isTakenKey = false;
    key.setX(900);
    key.setY(650);
  }

  public Player getPlayer() {
    return player;
  }
  public Slime getSlime() {
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
  public void setKeyTaken(boolean taken){
    isTakenKey = taken;
  }

  public void toggleInventory() {
    isInventoryOpen = !isInventoryOpen;
  }

  public Enemy getMonster() {
    return monster;
  }
}

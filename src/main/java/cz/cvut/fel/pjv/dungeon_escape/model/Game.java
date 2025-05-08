package cz.cvut.fel.pjv.dungeon_escape.model;

import cz.cvut.fel.pjv.dungeon_escape.model.entities.Player;
import cz.cvut.fel.pjv.dungeon_escape.model.environment.Door;
import cz.cvut.fel.pjv.dungeon_escape.model.items.Key;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

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

  private boolean isTakenKey = false;

  public Game() {
    backround = new GameItem(ImageId.BGR, 0, 0);
    platform = new Platforms(ImageId.PLATFORM,0, 0, -25, 350);
    platform2 = new Platforms(ImageId.PLATFORM,0, 0, 630, 170);
    ground = new Platforms(ImageId.GROUND,0, 0, 0, 690);
    inventory = new GameItem(ImageId.INVENTORY, 0,0);
    key = new Key(ImageId.KEY, 900,650,"key1");
    door = new Door(ImageId.DOOR, 920, 50);

    addCollidableObject(platform2);
    addCollidableObject(platform);
    addCollidableObject(ground);

    player = new Player(ImageId.PLAYER,0, 0, 0, gravity);
  }
  public void update(){
      player.update();
      handleCollisions();

  }
  public void addCollidableObject(GameItem gameItem){
    collidableObjects.add(gameItem);
  }
  private void checkLevelBounds(Bounds playerBounds){
    BoundingBox backgroundBounds = backround.getBoundingBox();
//    System.out.println(backgroundBounds.getHeight() + " " + backgroundBounds.getWidth());

    // check down
    if (playerBounds.getMaxY() >= backgroundBounds.getMaxY()) {
      System.out.println(playerBounds.getMaxY() + " " + backgroundBounds.getMaxY());
      player.setY((backgroundBounds.getMaxY() - playerBounds.getHeight()));
      player.setOnGround(true);
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
  private void handleCollisions() {
    BoundingBox playerBounds = player.getBoundingBox();
    // check background
    checkLevelBounds(playerBounds);

    if(!isTakenKey && playerBounds.intersects(key.getBoundingBox())){
      player.addInventory(key);
      isTakenKey = true;
      System.out.println("Taken key");
    }

    // check with others objects
    for (GameItem object : collidableObjects) {
      if (object != player) {
        checkObjectCollision(playerBounds, object.getBoundingBox());
      }
    }
  }
  private void checkObjectCollision(BoundingBox playerBounds, BoundingBox objectBounds) {
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
  public void movePlayer(boolean up, boolean down, boolean left, boolean right, boolean jump) {
    player.move(up, down, left, right, jump);
  }
  public DrawableItem[] getItemsToDraw() {
    List<DrawableItem> items = new ArrayList<>(Arrays.asList(
      new DrawableItem(backround.getImageId(), backround.getX(), backround.getY()),
      new DrawableItem(inventory.getImageId(), inventory.getX(), inventory.getY()),
      new DrawableItem(platform.getImageId(), platform.getX(), platform.getY()),
      new DrawableItem(platform2.getImageId(), platform2.getX(), platform2.getY()),
      new DrawableItem(door.getImageId(), door.getX(), door.getY()),
      new DrawableItem(ground.getImageId(), ground.getX(), ground.getY()),
      new DrawableItem(player.getImageId(), player.getX(), player.getY())
    ));

    for (GameItem item : player.getInventory().getItems()) {
      items.add(new DrawableItem(item.getImageId(), item.getX(), item.getY()));
    }
    if (!isTakenKey) {
      items.add(new DrawableItem(key.getImageId(), key.getX(), key.getY()));
    }

    return items.toArray(new DrawableItem[0]);
  }

}

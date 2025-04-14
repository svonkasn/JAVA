package cz.cvut.fel.pjv.dungeon_escape.model;

import cz.cvut.fel.pjv.dungeon_escape.model.entities.Player;
import javafx.application.Platform;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Game {
  private List<GameItem> collidableObjects = new ArrayList<>();
  private final double gravity = 0.1;

  private final GameItem backround;
  private final Player player;
  private final Platforms platform;

  public Game() {
    backround = new GameItem(ImageId.BGR, 0, 0);
    player = new Player(ImageId.PLAYER,0, 0, 0, gravity);
    platform = new Platforms(ImageId.PLATFORM,100, 500, 200, 20);
    addCollidableObject(platform);
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

    // check down
    if (playerBounds.getMaxY() >= backgroundBounds.getMaxY()) {
      player.setY((int)(backgroundBounds.getMaxY() - playerBounds.getHeight()));
      player.setOnGround(true);
    }

    // check wall
    if (playerBounds.getMinX() <= backgroundBounds.getMinX()) {
      player.setX((int)backgroundBounds.getMinX());
    } else if (playerBounds.getMaxX() >= backgroundBounds.getMaxX()) {
      player.setX((int)(backgroundBounds.getMaxX() - playerBounds.getWidth()));
    }

    // check top
    if (playerBounds.getMinY() <= backgroundBounds.getMinY()) {
      player.setY((int)backgroundBounds.getMinY());
      player.setSpeed(0);
    }
  }

  private void handleCollisions() {
    BoundingBox playerBounds = player.getBoundingBox();
    // check backgound
    checkLevelBounds(playerBounds);

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
        // lwft
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
    return new DrawableItem[]{
      new DrawableItem(backround.getImageId(), backround.getX(), backround.getY()),
      new DrawableItem(player.getImageId(), player.getX(), player.getY()),
      new DrawableItem(platform.getImageId(), platform.getX(), platform.getY())
    };
  }

}

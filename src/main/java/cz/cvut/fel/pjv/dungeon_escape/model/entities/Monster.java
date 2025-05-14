package cz.cvut.fel.pjv.dungeon_escape.model.entities;

import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;
import javafx.scene.image.Image;

public class Monster extends Enemy {
  private double patrolMinX;
  private double patrolMaxX;

  private double speed = 1.0;
  private boolean movingRight = true;

  private boolean isAggro = false;
  private long aggroStartTime = 0;
  private double visionRange = 150;
  private final long aggroDuration = 3_000_000_000L;

  public Monster(ImageId imageId, double x, double y) {
    super(imageId, x, y);
    this.patrolMinX = x - 100;
    this.patrolMaxX = x + 800;
  }

  @Override
  public void update(Player player){
    double playerX = player.getX();
    double distanceToPlayer = Math.abs(playerX - getX());
    patrol();

    if (distanceToPlayer <= 30) {
      attack(player);
    }
  }
  private void patrol() {
    if (movingRight) {
      setX(getX() + speed);
      if (getX() >= patrolMaxX) {
        movingRight = false;
      }
    } else {
      setX(getX() - speed);
      if (getX() <= patrolMinX) {
        movingRight = true;
      }
    }
  }
}

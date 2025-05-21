package cz.cvut.fel.pjv.dungeon_escape.model.entities;

import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;
import javafx.scene.image.Image;

public class Monster extends Enemy {
  private double patrolMinX;
  private double patrolMaxX;

  private double speed = 1.0;
  private boolean movingRight = true;

  public Monster(ImageId imageId, double x, double y) {
    super(imageId, x, y);
    this.patrolMinX = x - 100;
    this.patrolMaxX = x + 800;
  }

  @Override
  public void update(Player player){
    double playerX = player.getX();
    double playerY = player.getY();
    double distanceX = Math.abs(playerX - getX());
    double distanceY = Math.abs(playerY - getY());

    if (distanceX <= 120 && distanceY <= 70) {
      attack(player);
      attackDirection = (playerX > getX()) ? 1 : -1;
    } else
      patrol();

    if (isAttacking && System.currentTimeMillis() - attackStartTime > 1000) {
      isAttacking = false;
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

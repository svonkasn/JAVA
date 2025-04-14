package cz.cvut.fel.pjv.dungeon_escape.model.entities;


import cz.cvut.fel.pjv.dungeon_escape.model.Game;
import cz.cvut.fel.pjv.dungeon_escape.model.GameItem;
import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;

public class Player extends GameItem {
  private final double gravity;
  private int health;
  private double speed;
  private boolean isOnGround;


  public Player(ImageId imageId, int x, int y, int health, double gravity) {
    super(imageId, x, y);
    this.gravity = gravity;
    this.health = health;
    this.speed = 0;
    this.isOnGround = false;
  }
  public void update() {
    if(!isOnGround) {
      speed += gravity;
      y += speed;
    }else{
      speed = 0;
    }
  }
  public void move(boolean up, boolean down, boolean left, boolean right, boolean jump) {
    double moveSpeed = 2.0;
    if (left) x -= moveSpeed;
    if (right) x += moveSpeed;

    if (up) y -= moveSpeed;
    if (down) y += moveSpeed;

    if (jump && isOnGround) {
      speed = -10;
      isOnGround = false;
    }
  }
  public void setSpeed(double speed) {
    this.speed = speed;
  }

  public double getSpeed() {
    return speed;
  }

  public void setOnGround(boolean onGround) {
    isOnGround = onGround;
  }

  public int getHealth() {
    return health;
  }

  public void heal(int heal) {
    health += heal;
  }

  public void takeDamage(int damage) {
    health -= damage;
  }
}

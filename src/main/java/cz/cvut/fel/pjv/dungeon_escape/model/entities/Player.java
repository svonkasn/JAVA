package cz.cvut.fel.pjv.dungeon_escape.model.entities;

import cz.cvut.fel.pjv.dungeon_escape.model.GameItem;
import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;
import cz.cvut.fel.pjv.dungeon_escape.model.Inventory;

public class Player extends GameItem {
  private final double gravity;
  private int health;
  private double speed;
  private boolean isOnGround;

  private Inventory inventory;


  public Player(ImageId imageId, int x, int y, int health, double gravity) {
    super(imageId, x, y);
    this.gravity = gravity;
    this.health = health;
    this.speed = 0;
    this.isOnGround = false;
    this.inventory = new Inventory();
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
    double moveSpeed = 8.0;
    if (left) x -= moveSpeed;
    if (right) x += moveSpeed;

    if (up) y -= moveSpeed;
    if (down) y += moveSpeed;

    if (jump && isOnGround) {
      speed = -8.5;
      isOnGround = false;
    }
    if(isOnGround) {
      isOnGround = false;
    }
  }
  public Inventory getInventory() {
    return inventory;
  }

  public void addInventory(GameItem item) {
    inventory.addItm(item);
  }
  public void removeInventory(GameItem item) {inventory.removeItm(item);}
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

  public void resetPosition() {
    setX(0);
    setY(0);
  }
}

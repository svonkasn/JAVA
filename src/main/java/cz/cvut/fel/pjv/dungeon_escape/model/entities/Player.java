package cz.cvut.fel.pjv.dungeon_escape.model.entities;

import cz.cvut.fel.pjv.dungeon_escape.model.GameItem;
import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;
import cz.cvut.fel.pjv.dungeon_escape.model.Inventory;

public class Player extends GameItem {
  private final double gravity;
  private double health;
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
  public void move(boolean left, boolean right, boolean jump) {
    double moveSpeed = 4.0;
    if (left) x -= moveSpeed;
    if (right) x += moveSpeed;

    if (jump && isOnGround) {
      speed = -8.5;
      isOnGround = false;
    }
    if(isOnGround) {
      isOnGround = false;
    }
  }
  public void removeInventory(GameItem item) {inventory.removeItm(item);}
  public boolean isOnGround() {
    return isOnGround;
  }
  public void heal(int heal) {
    health += heal;
  }
  public void takeDamage(int damage) {
    health -= damage;
  }
  public void reset() {
    this.x = 0;
    this.y = 0;
    this.speed = 0;
    this.isOnGround = false;
    this.inventory = new Inventory();
  }
  public Inventory getInventory() {
    return inventory;
  }
  public double getHealth() {
    return health;
  }
  public void getDamage(double damage) {
    health -= damage;
  }
  public void setOnGround(boolean onGround) {
    isOnGround = onGround;
  }
  public void setSpeed(double speed) {
    this.speed = speed;
  }
  public void setHealth(double health) {
    this.health = health;
  }
}

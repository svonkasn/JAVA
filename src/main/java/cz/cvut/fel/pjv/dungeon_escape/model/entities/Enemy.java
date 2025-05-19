package cz.cvut.fel.pjv.dungeon_escape.model.entities;

import cz.cvut.fel.pjv.dungeon_escape.model.GameItem;
import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;


public class Enemy extends GameItem {
  private double health = 10;
  private boolean dead = false;
  protected long lastAttackTime = 0;
  protected final long attackCooldown = 1_000_000_000;
  protected boolean isAttacking = false;
  protected long attackStartTime = 0;
  protected int attackDirection = 1;

  public Enemy(ImageId imageId, double x, double y) {
    super(imageId, x, y);
  }

  public boolean canAttack() {
    return System.nanoTime() - lastAttackTime >= attackCooldown;
  }
  public void attack(Player player) {
    if (canAttack()) {
      player.getDamage(1);
      lastAttackTime = System.nanoTime();
      isAttacking = true;
      attackStartTime = System.currentTimeMillis();
    }

  }
  public void takeDamage(double damage) {
     health -= damage;
     if (health < 0) {
       setDead(true);
     }
  }
  public void update(Player player) {}
  public boolean isAttacking() {
    return isAttacking;
  }
  public long getAttackStartTime() {
    return attackStartTime;
  }
  public int getAttackDirection() {
    return attackDirection;
  }

  public double getHealth() {
    return health;
  }
  public void setHealth(double health) {
    this.health = health;
  }
  public boolean isDead() {
    return dead;
  }
  public void setDead(boolean dead) {
    this.dead = dead;
  }
}

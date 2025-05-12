package cz.cvut.fel.pjv.dungeon_escape.model.entities;

import cz.cvut.fel.pjv.dungeon_escape.model.GameItem;
import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;

public class Enemy extends GameItem {
  private double health;
  private long lastAttackTime = 0;
  protected final long attackCooldown = 1_000_000_000;
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
    }
  }

  public void takeDamage(double damage) {
     health -= damage;
  }
}

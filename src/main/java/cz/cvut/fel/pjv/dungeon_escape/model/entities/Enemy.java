package cz.cvut.fel.pjv.dungeon_escape.model.entities;

import cz.cvut.fel.pjv.dungeon_escape.model.GameItem;
import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;

import java.util.logging.Logger;


/**
 * The Enemy class represents a base enemy entity in the game.
 * It provides common enemy functionality including health management,
 * attack mechanics, and position reset capabilities.
 */
public class Enemy extends GameItem {
  private static final Logger logger = Logger.getLogger(Enemy.class.getName());

  private double health = 10;
  private boolean dead = false;
  protected long lastAttackTime = 0;
  protected final long attackCooldown = 1_000_000_000; // 1 second in nanoseconds
  protected boolean isAttacking = false;
  protected long attackStartTime = 0;
  protected int attackDirection = 1;

  private final double initX;
  private final double initY;

  /**
   * Constructs a new Enemy at the specified position.
   *
   * @param imageId The ImageId to use for this enemy's visual representation
   * @param x The initial x-coordinate of the enemy
   * @param y The initial y-coordinate of the enemy
   */
  public Enemy(ImageId imageId, double x, double y) {
    super(imageId, x, y);
    initX = x;
    initY = y;
  }

  /**
   * Checks if the enemy is ready to attack again based on the attack cooldown.
   *
   * @return true if the attack cooldown period has elapsed, false otherwise
   */
  public boolean canAttack() {
    return System.nanoTime() - lastAttackTime >= attackCooldown;
  }

  /**
   * Attacks the specified player if the attack cooldown has elapsed.
   * Also manages attack state tracking for animation/behavior purposes.
   *
   * @param player The player to attack
   */
  public void attack(Player player) {
    if (canAttack()) {
      player.getDamage(1);
      lastAttackTime = System.nanoTime();
      if (!isAttacking) {
        isAttacking = true;
        attackStartTime = System.currentTimeMillis();
      }
    }
  }

  /**
   * Applies damage to the enemy and handles death state if health reaches zero.
   *
   * @param damage The amount of damage to apply
   */
  public void takeDamage(double damage) {
    health -= damage;
    logger.info("Enemy took " + damage + " damage. Remaining health: " + health);
    if (health < 0) {
      setDead(true);
      logger.warning("Enemy is dead.");
    }
  }

  /**
   * Updates the enemy's state. This base implementation is empty and should be
   * overridden by subclasses to implement specific enemy behaviors.
   *
   * @param player The player reference for AI targeting or other interactions
   */
  public void update(Player player) {}

  /**
   * Resets the enemy to its initial position and state.
   * Useful for level restarts or enemy respawns.
   */
  public void reset() {
    setX(initX);
    setY(initY);
  }

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

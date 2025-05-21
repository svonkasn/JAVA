package cz.cvut.fel.pjv.dungeon_escape.model.entities;

import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;
import javafx.scene.image.Image;
/**
 * The Monster class represents a specific type of Enemy that patrols an area
 * and attacks the player when in range. It extends the base Enemy class with
 * patrolling behavior and player detection capabilities.
 */
public class Monster extends Enemy {
  private double patrolMinX;  // Left boundary of patrol area
  private double patrolMaxX;  // Right boundary of patrol area
  private double speed = 1.0; // Movement speed in pixels per update
  private boolean movingRight = true; // Current movement direction

  /**
   * Constructs a Monster with specified image and position, setting up its patrol area.
   * The patrol area is automatically calculated as 100 units left and 800 units right
   * from the initial position.
   *
   * @param imageId The visual representation of the monster
   * @param x       Initial x-coordinate position
   * @param y       Initial y-coordinate position
   */
  public Monster(ImageId imageId, double x, double y) {
    super(imageId, x, y);
    this.patrolMinX = x - 100;
    this.patrolMaxX = x + 800;
  }

  /**
   * Updates the monster's state each game tick, handling both patrolling and attacking behaviors.
   * The monster will:
   * - Attack the player if within 120x70 unit detection range
   * - Patrol between boundaries when player is not detected
   * - Manage attack state and cooldowns
   *
   * @param player The player character used for detection and attacking
   */
  @Override
  public void update(Player player) {
    double playerX = player.getX();
    double playerY = player.getY();
    double distanceX = Math.abs(playerX - getX());
    double distanceY = Math.abs(playerY - getY());

    // Attack if player is within detection range
    if (distanceX <= 120 && distanceY <= 70) {
      attack(player);
      attackDirection = (playerX > getX()) ? 1 : -1; // Face player when attacking
    } else {
      patrol(); // Continue patrolling if player not detected
    }

    // Reset attacking state after 1 second
    if (isAttacking && System.currentTimeMillis() - attackStartTime > 1000) {
      isAttacking = false;
    }
  }

  /**
   * Handles the monster's patrolling behavior between the defined boundaries.
   * The monster moves back and forth between patrolMinX and patrolMaxX,
   * changing direction when reaching either boundary.
   */
  private void patrol() {
    if (movingRight) {
      setX(getX() + speed);
      if (getX() >= patrolMaxX) {
        movingRight = false; // Change direction at right boundary
      }
    } else {
      setX(getX() - speed);
      if (getX() <= patrolMinX) {
        movingRight = true; // Change direction at left boundary
      }
    }
  }
}

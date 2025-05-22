package cz.cvut.fel.pjv.dungeon_escape.model.entities;

import cz.cvut.fel.pjv.dungeon_escape.model.GameItem;
import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;
import cz.cvut.fel.pjv.dungeon_escape.model.Inventory;
import cz.cvut.fel.pjv.dungeon_escape.model.InventoryItem;

/**
 * The Player class represents the main character controlled by the user.
 * It handles player movement, physics, health management, and inventory.
 */
public class Player extends GameItem {
  private final double gravity;      // The gravitational force affecting the player
  private double health;            // Current health points
  private double speed;             // Current vertical speed (for jumping/falling)
  private boolean isOnGround;       // Flag indicating if player is on solid ground
  private Inventory inventory;      // Player's item inventory

  /**
   * Constructs a new Player with specified attributes.
   *
   * @param imageId The visual representation of the player
   * @param x       Initial x-coordinate position
   * @param y       Initial y-coordinate position
   * @param health  Starting health points
   * @param gravity Gravitational force to apply when airborne
   */
  public Player(ImageId imageId, int x, int y, double health, double gravity) {
    super(imageId, x, y);
    this.gravity = gravity;
    this.health = health;
    this.speed = 0;
    this.isOnGround = false;
    this.inventory = new Inventory();
  }

  /**
   * Updates the player's physics state each game tick.
   * Applies gravity when airborne and resets vertical speed when grounded.
   */
  public void update() {
    if(!isOnGround) {
      speed += gravity;  // Apply gravity when not on ground
      y += speed;        // Update vertical position
    } else {
      speed = 0;         // Reset vertical speed when grounded
    }
  }

  /**
   * Handles player movement based on input states.
   *
   * @param left  Whether left movement is requested
   * @param right Whether right movement is requested
   * @param jump  Whether jump is requested
   */
  public void move(boolean left, boolean right, boolean jump) {
    double moveSpeed = 4.0;  // Horizontal movement speed

    // Horizontal movement
    if (left) x -= moveSpeed;
    if (right) x += moveSpeed;

    // Jump handling
    if (jump && isOnGround) {
      speed = -8.5;        // Set upward jump velocity
      isOnGround = false;  // Player is no longer grounded
    }

    // Reset ground state (will be recalculated in collision detection)
    if(isOnGround) {
      isOnGround = false;
    }
  }

  /**
   * Removes an item from the player's inventory.
   *
   * @param item The inventory item to remove
   */
  public void removeInventory(InventoryItem item) {
    inventory.removeItm(item);
  }

  /**
   * Restores health points to the player.
   *
   * @param heal The amount of health to restore
   */
  public void heal(int heal) {
    health = Math.min(health + heal, 10);
  }

  /**
   * Applies damage to the player.
   *
   * @param damage The amount of damage to take
   */
  public void takeDamage(int damage) {
    health -= damage;
  }

  /**
   * Resets the player to initial state (position, speed, inventory).
   * Used when restarting levels or after player death.
   */
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
  public boolean isOnGround() {
    return isOnGround;
  }
}

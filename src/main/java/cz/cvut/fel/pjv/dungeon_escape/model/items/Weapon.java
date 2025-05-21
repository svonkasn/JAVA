package cz.cvut.fel.pjv.dungeon_escape.model.items;

import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;
import cz.cvut.fel.pjv.dungeon_escape.model.InventoryItem;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Enemy;

/**
 * The Weapon class represents an attack item that can damage enemies.
 * When used, it applies a fixed amount of damage to targeted enemies.
 */
public class Weapon extends InventoryItem {
  private final int damage = 1;  // Fixed damage amount dealt by this weapon

  /**
   * Constructs a Weapon item at the specified position.
   *
   * @param imageId The visual representation of the weapon
   * @param x       The x-coordinate position in the game world
   * @param y       The y-coordinate position in the game world
   */
  public Weapon(ImageId imageId, double x, double y) {
    super(imageId, x, y);
  }

  /**
   * Attacks an enemy with this weapon, applying damage.
   *
   * @param enemy The enemy to attack
   */
  public void attack(Enemy enemy) {
    enemy.takeDamage(damage);
  }
}

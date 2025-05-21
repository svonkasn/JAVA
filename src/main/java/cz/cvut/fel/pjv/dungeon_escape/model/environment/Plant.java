package cz.cvut.fel.pjv.dungeon_escape.model.environment;

import cz.cvut.fel.pjv.dungeon_escape.model.*;

/**
 * The Plant class represents a collectible plant item in the game world.
 * Plants can be collected by the player and potentially used for crafting.
 * Extends InventoryItem to support collection and inventory management.
 */
public class Plant extends InventoryItem {
  /**
   * Constructs a Plant item at the specified position.
   *
   * @param imageId The visual representation of the plant
   * @param x       The x-coordinate position in the game world
   * @param y       The y-coordinate position in the game world
   */
  public Plant(ImageId imageId, double x, double y) {
    super(imageId, x, y);
  }
}

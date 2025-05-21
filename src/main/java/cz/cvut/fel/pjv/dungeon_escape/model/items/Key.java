package cz.cvut.fel.pjv.dungeon_escape.model.items;

import cz.cvut.fel.pjv.dungeon_escape.model.*;
import cz.cvut.fel.pjv.dungeon_escape.model.environment.Door;

/**
 * The Key class represents a door key item that can unlock specific doors.
 * Each key is associated with a particular door through a unique identifier.
 */
public class Key extends InventoryItem {
  private final String doorKey;  // Unique identifier matching the door this key opens

  /**
   * Constructs a Key item with its associated door identifier.
   *
   * @param imageId The visual representation of the key
   * @param x       The x-coordinate position in the game world
   * @param y       The y-coordinate position in the game world
   * @param doorKey The unique identifier of the door this key opens
   */
  public Key(ImageId imageId, double x, double y, String doorKey) {
    super(imageId, x, y);
    this.doorKey = doorKey;
  }

  /**
   * Checks if this key matches a specific door.
   *
   * @param door The door to check compatibility with
   * @return true if this key can open the specified door, false otherwise
   */
  public boolean fitsDoorKey(Door door) {
    return this.doorKey.equals(door.getDoorKey());
  }
}

package cz.cvut.fel.pjv.dungeon_escape.model.environment;

import cz.cvut.fel.pjv.dungeon_escape.model.GameItem;
import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Player;

/**
 * The Door class represents a door game object that can be locked or unlocked.
 * Players can interact with doors to potentially progress to new areas.
 */
public class Door extends GameItem {
  private boolean isLocked;  // Flag indicating if the door is currently locked

  /**
   * Constructs a Door at the specified position.
   *
   * @param imageId The visual representation of the door
   * @param x       The x-coordinate position of the door
   * @param y       The y-coordinate position of the door
   */
  public Door(ImageId imageId, double x, double y) {
    super(imageId, x, y);
  }

  /**
   * Checks if the door is currently locked.
   *
   * @return true if the door is locked, false otherwise
   */
  public boolean unlock() {
    return isLocked;
  }

  /**
   * Attempts to open the door. The door will open if:
   * - It is already unlocked, or
   * - The player has a key in their inventory
   *
   * @param player The player attempting to open the door
   * @return true if the door was successfully opened, false otherwise
   */
  public boolean tryOpen(Player player) {
    if (!isLocked) return true;  // Door is already unlocked

    if (player.getInventory().hasKey()) {
      isLocked = false;  // Unlock the door if player has a key
      return true;
    }
    return false;  // Door remains locked
  }

  /**
   * Gets the unique identifier for this door based on its image file.
   * This can be used to match doors with their corresponding keys.
   *
   * @return The filename of the door's image as its identifier
   */
  public String getDoorKey() {
    return imageId.getFileName();
  }
}

package cz.cvut.fel.pjv.dungeon_escape.model;

/**
 * Base class for all collectible game items that can be stored in inventory.
 * Tracks collection state and handles reset functionality.
 */
public class InventoryItem extends GameItem {
  private boolean collected = false;      // Whether item has been picked up
  private final double initialX;         // Original spawn X position
  private final double initialY;         // Original spawn Y position

  /**
   * Creates a collectible item at specified position.
   * @param imageId Visual appearance of the item
   * @param x       Initial world X coordinate
   * @param y       Initial world Y coordinate
   */
  public InventoryItem(ImageId imageId, double x, double y) {
    super(imageId, x, y);
    this.initialX = x;
    this.initialY = y;
  }

  /**
   * Checks if item is available for collection.
   * @return True if item hasn't been collected yet
   */
  public boolean canBeCollected() {
    return !isCollected();
  }

  /**
   * Handles collection logic - adds item to player's inventory.
   * @param game Current game instance
   */
  public void onCollect(Game game) {
    game.getPlayer().getInventory().addItm(this);
    collected = true;
  }

  // Basic accessors
  public boolean isCollected() { return collected; }
  public void setCollected(boolean collected) { this.collected = collected; }

  /**
   * Resets item to initial state:
   * - Marks as uncollected
   * - Returns to original position
   */
  public void reset() {
    collected = false;
    setX(initialX);
    setY(initialY);
  }
}

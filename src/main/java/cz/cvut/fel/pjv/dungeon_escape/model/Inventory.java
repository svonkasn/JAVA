package cz.cvut.fel.pjv.dungeon_escape.model;


import cz.cvut.fel.pjv.dungeon_escape.model.items.Key;
import cz.cvut.fel.pjv.dungeon_escape.model.items.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manages player inventory with limited capacity and item positioning.
 */
public class Inventory {
  // Inventory configuration
  private final int capacity = 5;  // Max items player can carry
  private List<InventoryItem> items = new ArrayList<>();

  // Visual layout settings
  private float slotX = 0;      // Base X position for first slot
  private float slotY = 0;      // Base Y position for all slots
  private float slotSize = 40;  // Size of each inventory slot

  /**
   * Adds item if inventory has space.
   * Automatically updates item positions.
   */
  public void addItm(InventoryItem item) {
    if (items.size() < capacity) {
      items.add(item);
    }
    updateItemPosition();
  }

  /**
   * Removes specified item if present.
   * Updates positions of remaining items.
   */
  public void removeItm(InventoryItem item) {
    if (items.size() > 0) {
      items.remove(item);
    }
    updateItemPosition();
  }

  /** Returns current list of inventory items */
  public List<InventoryItem> getItems() {
    return items;
  }

  /**
   * Uses/consumes a key if available.
   * @return true if key was found and used
   */
  public boolean useKey() {
    Optional<InventoryItem> key = items.stream()
      .filter(item -> item instanceof Key)
      .findFirst();

    if (key.isPresent()) {
      items.remove(key.get());
      updateItemPosition();
      return true;
    }
    return false;
  }

  /** Checks if inventory contains any key */
  public boolean hasKey() {
    return items.stream().anyMatch(item -> item instanceof Key);
  }

  /** Checks if inventory contains any weapon */
  public boolean hasWeapon() {
    return items.stream().anyMatch(item -> item instanceof Weapon);
  }

  /**
   * Uses specified item if present.
   * Currently just removes it (TODO: implement usage effects).
   */
  public void useItem(InventoryItem item) {
    if (items.contains(item)) {
      items.remove(item);
      updateItemPosition();
    }
  }

  /** Checks if inventory is full */
  public boolean isFull() {
    return items.size() >= capacity;
  }

  /**
   * Updates visual positions of all items in inventory.
   * Arranges them horizontally with spacing.
   */
  private void updateItemPosition() {
    for (int i = 0; i < items.size(); i++) {
      items.get(i).setX(slotX + i * (slotSize + 40));  // 40px spacing
      items.get(i).setY(slotY + 5);  // Small vertical offset
    }
  }

  /** Clears all items from inventory */
  public void reset() {
    items.clear();
  }
}

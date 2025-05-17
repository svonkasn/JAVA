package cz.cvut.fel.pjv.dungeon_escape.model.items;

import cz.cvut.fel.pjv.dungeon_escape.model.*;
import cz.cvut.fel.pjv.dungeon_escape.model.environment.Door;


public class Key extends InventoryItem {
  private final String doorKey;
  private boolean keyTaken = false;

  public Key(ImageId imageId, double x, double y, String doorKey) {
    super(imageId, x, y);
    this.doorKey = doorKey;
  }

  public boolean fitsDoorKey(Door door){
    return this.doorKey.equals(door.getDoorKey());
  }

  @Override
  public boolean canBeCollected(Game game) {
    return !keyTaken;
  }

  @Override
  public void onCollect(Game game) {
    game.getPlayer().getInventory().addItm(this);
    keyTaken = true;
  }

  public boolean isKeyTaken() {
    return keyTaken;
  }

  public void setKeyTaken(boolean keyTaken) {
    this.keyTaken = keyTaken;
  }
}

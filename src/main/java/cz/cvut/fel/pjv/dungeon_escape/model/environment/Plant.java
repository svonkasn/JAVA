package cz.cvut.fel.pjv.dungeon_escape.model.environment;

import cz.cvut.fel.pjv.dungeon_escape.model.*;

public class Plant extends InventoryItem {
  private boolean isCollected = false;
  public Plant(ImageId imageId, double x, double y) {
    super(imageId, x, y);
  }

  @Override
  public boolean canBeCollected(Game game) {
    return !isCollected();
  }

  @Override
  public void onCollect(Game game) {
    game.getPlayer().getInventory().addItm(this);
    isCollected = true;
  }

  public boolean isCollected() {
    return isCollected;
  }
}

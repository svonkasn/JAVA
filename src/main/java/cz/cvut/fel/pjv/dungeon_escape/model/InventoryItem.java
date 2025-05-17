package cz.cvut.fel.pjv.dungeon_escape.model;

public class InventoryItem extends GameItem {

  public InventoryItem(ImageId imageId, double x, double y) {
    super(imageId, x, y);
  }

  public boolean canBeCollected(Game game) {
    return false;
  }

  public void onCollect(Game game){}
}

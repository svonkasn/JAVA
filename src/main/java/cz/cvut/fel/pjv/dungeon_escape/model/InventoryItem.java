package cz.cvut.fel.pjv.dungeon_escape.model;

public class InventoryItem extends GameItem {
  private boolean collected = false;
  public InventoryItem(ImageId imageId, double x, double y) {
    super(imageId, x, y);
  }

  public boolean canBeCollected() {
    return !isCollected();
  }

  public void onCollect(Game game){
    game.getPlayer().getInventory().addItm(this);
    collected = true;
  }

  public boolean isCollected() {
    return collected;
  }

  public void setCollected(boolean collected) {
    this.collected = collected;
  }
}

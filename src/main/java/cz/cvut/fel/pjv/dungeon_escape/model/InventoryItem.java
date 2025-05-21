package cz.cvut.fel.pjv.dungeon_escape.model;

public class InventoryItem extends GameItem {
  private boolean collected = false;
  private final double initialX;
  private final double initialY;
  public InventoryItem(ImageId imageId, double x, double y) {
    super(imageId, x, y);
    this.initialX = x;
    this.initialY = y;
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
  public void reset() {
    collected = false;
    setX(initialX);
    setY(initialY);
  }
}

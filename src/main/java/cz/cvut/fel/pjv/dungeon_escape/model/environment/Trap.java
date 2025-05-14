package cz.cvut.fel.pjv.dungeon_escape.model.environment;

import cz.cvut.fel.pjv.dungeon_escape.model.GameItem;
import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Player;

public class Trap extends GameItem {
  private int damage;
  private boolean isActive;

  public Trap(ImageId imageId, double x, double y) {
    super(imageId, x, y);
    this.isActive = true;
    this.damage = 1;
  }
  public void setActive(boolean active) {
    this.isActive = active;
  }
  public void trigger(Player player) {
    if(isActive){
      player.takeDamage(damage);
      isActive = false;
    }
  }
  public void reset(){
    isActive = true;
  }

}

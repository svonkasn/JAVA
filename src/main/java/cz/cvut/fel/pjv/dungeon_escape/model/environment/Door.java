package cz.cvut.fel.pjv.dungeon_escape.model.environment;

import cz.cvut.fel.pjv.dungeon_escape.model.GameItem;
import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Player;

public class Door extends GameItem {
  private boolean isLocked;

  public Door(ImageId imageId, double x, double y) {
    super(imageId, x, y);
  }
  public boolean unlock(){
    /* TODO
    if i have key in my invenotry i can open the door
    * */

    return isLocked;
  }

  public boolean tryOpen(Player player){
      if (!isLocked) return true;

      if(player.getInventory().hasKey()){
        isLocked = false;
        return true;
      }
    return false;
  }

  public String getDoorKey() {
    return imageId.getFileName();
  }
}

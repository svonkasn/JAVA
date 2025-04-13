package cz.cvut.fel.pjv.dungeon_escape.model.environment;

import cz.cvut.fel.pjv.dungeon_escape.model.GameItem;
import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;

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

  public void useKey(){
    /* TODO
    *   ????
    * */
  }
}

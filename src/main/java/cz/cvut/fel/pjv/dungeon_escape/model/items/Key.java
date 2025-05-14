package cz.cvut.fel.pjv.dungeon_escape.model.items;

import cz.cvut.fel.pjv.dungeon_escape.model.GameItem;
import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;
import cz.cvut.fel.pjv.dungeon_escape.model.environment.Door;

public class Key extends GameItem {
  private final String doorKey;

  public Key(ImageId imageId, double x, double y, String doorKey) {
    super(imageId, x, y);
    this.doorKey = doorKey;
  }
  public boolean fitsDoorKey(Door door){
    return this.doorKey.equals(door.getDoorKey());
  }
}

package cz.cvut.fel.pjv.dungeon_escape.model.entities;

import cz.cvut.fel.pjv.dungeon_escape.model.GameItem;
import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;

public class Enemy extends GameItem {

  public Enemy(ImageId imageId, double x, double y) {
    super(imageId, x, y);
  }
}

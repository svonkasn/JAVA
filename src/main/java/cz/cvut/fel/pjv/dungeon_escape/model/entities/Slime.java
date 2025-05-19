package cz.cvut.fel.pjv.dungeon_escape.model.entities;

import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;
import javafx.geometry.BoundingBox;

public class Slime extends Enemy {

  public Slime(ImageId imageId, double x, double y) {
    super(imageId, x, y);
  }

  @Override
  public void update(Player player) {
    BoundingBox playerBounds = player.getBoundingBox();
    if(playerBounds.intersects(getBoundingBox())){
      attack(player);
    }
  }
}

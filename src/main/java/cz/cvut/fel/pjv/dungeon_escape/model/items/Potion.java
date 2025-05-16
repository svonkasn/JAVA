package cz.cvut.fel.pjv.dungeon_escape.model.items;

import cz.cvut.fel.pjv.dungeon_escape.model.GameItem;
import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Player;

public class Potion extends GameItem {
  private final int heal = 2;
  public Potion(ImageId imageId, double x, double y) {
    super(imageId, x, y);
  }

  public void use(Player player){
    if (player.getHealth() < 10) {
      player.heal(heal);
    }
  }
}

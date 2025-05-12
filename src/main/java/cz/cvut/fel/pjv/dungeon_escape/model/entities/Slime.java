package cz.cvut.fel.pjv.dungeon_escape.model.entities;

import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;

public class Slime extends Enemy {

  public Slime(ImageId imageId, double x, double y) {
    super(imageId, x, y);
  }
  @Override
  public void attack(Player player) {
    super.attack(player);
  }
  @Override
  public void takeDamage(double damage) {
    // TODO
  }
}

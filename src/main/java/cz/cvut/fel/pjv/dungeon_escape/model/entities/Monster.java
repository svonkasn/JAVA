package cz.cvut.fel.pjv.dungeon_escape.model.entities;

import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;

public class Monster extends Enemy {

  public Monster(ImageId imageId, double x, double y) {
    super(imageId, x, y);
  }
  @Override
  public void attack(){
    // TODO
  }
  @Override
  public void takeDamage(int damage) {
    // TODO
  }
}

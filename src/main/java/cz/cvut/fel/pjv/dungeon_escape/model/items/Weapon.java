package cz.cvut.fel.pjv.dungeon_escape.model.items;

import cz.cvut.fel.pjv.dungeon_escape.model.GameItem;
import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Enemy;

public class Weapon extends GameItem {

  private final int damage = 1;
  public Weapon(ImageId imageId, double x, double y) {
    super(imageId, x, y);
  }

  public void attack(Enemy enemy){
    enemy.takeDamage(damage);
  }

}

package cz.cvut.fel.pjv.dungeon_escape.model.entities;

import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;
import javafx.geometry.BoundingBox;
/**
 * The Slime class represents a simple enemy that damages the player on contact.
 * Unlike the Monster class, it doesn't actively pursue the player but instead
 * acts as a stationary hazard that damages upon collision.
 */
public class Slime extends Enemy {

  /**
   * Constructs a Slime enemy at the specified position.
   *
   * @param imageId The visual representation of the slime
   * @param x       The initial x-coordinate position
   * @param y       The initial y-coordinate position
   */
  public Slime(ImageId imageId, double x, double y) {
    super(imageId, x, y);
  }

  /**
   * Updates the slime's state each game tick.
   * Checks for collision with the player and attacks if contact occurs.
   *
   * @param player The player character to check for collisions with
   */
  @Override
  public void update(Player player) {
    BoundingBox playerBounds = player.getBoundingBox();
    if (playerBounds.intersects(getBoundingBox())) {
      attack(player);
    }
  }
}

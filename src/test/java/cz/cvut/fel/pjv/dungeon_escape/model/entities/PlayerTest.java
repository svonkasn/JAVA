package cz.cvut.fel.pjv.dungeon_escape.model.entities;

import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

  private Player player;

  @BeforeEach
  void setUp() {
    // Create player with health 10 and gravitation 0.5
    player = new Player(ImageId.PLAYER, 0, 0, 10.0, 0.5);
  }

  @Test
  void update_AppliesGravity_WhenNotOnGround() {
    player.update();
    assertTrue(player.getY() > 0, "Player has to be drop down by gravity");
  }

  @Test
  void move_Right_IncreasesX() {
    double initialX = player.getX();
    player.move(false, true, false);
    assertTrue(player.getX() > initialX, "Player has to be move to the right");
  }

  @Test
  void takeDamage_DecreasesHealth() {
    player.takeDamage(2);
    assertEquals(8, player.getHealth(), "Decreased health 2");
  }

  @Test
  void heal_IncreasesHealth() {
    player.takeDamage(3);
    player.heal(2);
    assertEquals(9, player.getHealth(), "Increased health 3");
  }
}

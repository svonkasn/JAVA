package cz.cvut.fel.pjv.dungeon_escape.model.entities;

import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class PlayerTest {

  private Player player;

  @BeforeEach
  void setUp() {
    // Create a player at position (0,0) with 10 health and gravity of 0.5
    player = new Player(ImageId.PLAYER, 0, 0, 10.0, 0.5);
  }

  @Test
  void update_AppliesGravity_WhenNotOnGround() {
    // By default, the player is not on the ground
    // Calling update should apply gravity and change Y position
    player.update();
    assertTrue(player.getY() > 0, "Player fall due to gravity");
  }

  @Test
  void move_Right_IncreasesX() {
    // Moving right increase the X
    double initialX = player.getX();
    player.move(false, true, false); // moveRight = true
    assertTrue(player.getX() > initialX, "Player move to the right");
  }

  @Test
  void takeDamage_DecreasesHealth() {
    // Take 2 damage reduce health from 10 to 8
    player.takeDamage(2);
    assertEquals(8, player.getHealth(), "Player's health decrease by 2");
  }

  @Test
  void heal_IncreasesHealth() {
    // After taking 3 damage, healing for 2 should bring health from 7 to 9
    player.takeDamage(3);
    player.heal(2);
    assertEquals(9, player.getHealth(), "Player's health increase by 2 after healing");
  }
}

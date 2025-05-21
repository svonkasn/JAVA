package cz.cvut.fel.pjv.dungeon_escape.model.entities;

import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class MonsterTest {

  private Monster monster;
  private Player player;

  @BeforeEach
  void setUp() {
    // Initialize a monster at (100, 100)
    monster = new Monster(ImageId.MONSTER, 100, 100);
    // Initialize a player far away from the monster
    player = new Player(ImageId.PLAYER, 300, 300, 10, 0.5);
  }

  @Test
  void monsterPatrolsRightInitially() {
    double initialX = monster.getX();

    // Player is far, so the monster patrol (move right initially)
    monster.update(player);

    assertTrue(monster.getX() > initialX, "Monster should move to the right when the player is far away.");
  }

  @Test
  void monsterSwitchesDirectionAtPatrolMax() {
    // Move monster to the edge of its patrol range
    monster.setX(100 + 800); // near patrolMaxX boundary

    monster.update(player); // still patrolling
    double xAfterFirstUpdate = monster.getX();
    monster.update(player); // On this update it should switch direction and move left

    assertTrue(monster.getX() < xAfterFirstUpdate, "Monster should reverse and move left after reaching patrolMaxX.");
  }

  @Test
  void monsterAttacksPlayerWhenClose() {
    // Place player near monster to trigger attack
    player.setX(110);
    player.setY(110);

    monster.update(player);

    assertTrue(monster.isAttacking(), "Monster starts attacking when player in attack range.");
  }

  @Test
  void monsterStopsAttackingAfterDelay() throws InterruptedException {
    // Place player near the monster
    player.setX(110);
    player.setY(110);

    // Monster starts attack
    monster.update(player);
    assertTrue(monster.isAttacking(), "Monster should be attacking immediately after detecting player.");

    // Wait 1 second to simulate
    Thread.sleep(1100);

    // Update again to check if monster stops attacking
    monster.update(player);

    assertFalse(monster.isAttacking(), "Monster stops attacking after 1 second.");
  }
}

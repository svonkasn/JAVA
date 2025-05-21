package cz.cvut.fel.pjv.dungeon_escape.model;

import cz.cvut.fel.pjv.dungeon_escape.model.entities.Enemy;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Monster;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Player;
import cz.cvut.fel.pjv.dungeon_escape.model.environment.Plant;
import cz.cvut.fel.pjv.dungeon_escape.model.items.Potion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class GameIntegrationTest {

  private Player player;
  private Game game;

  @BeforeEach
  public void setup() {
    player = new Player(ImageId.PLAYER, 100, 100, 10, 0.5);
    game = new Game();
  }

  @Test
  public void playerAttacksEnemy() {
    Enemy enemy = new Enemy(ImageId.MONSTER, 100, 100);
    double initialHealth = enemy.getHealth();
    enemy.takeDamage(3);
    assertTrue(enemy.getHealth() < initialHealth);
  }

  @Test
  public void enemyAttacksPlayer() {
    Enemy enemy = new Enemy(ImageId.MONSTER, 100, 100);
    double initialHealth = player.getHealth();
    enemy.attack(player);
    assertTrue(player.getHealth() < initialHealth);
  }

  @Test
  public void monsterPatrols() {
    Monster monster = new Monster(ImageId.MONSTER, 300, 100);
    double initialX = monster.getX();

    // Player is far, so monster patrol
    for (int i = 0; i < 1000; i++) {
      monster.update(new Player(ImageId.PLAYER, 1000, 1000, 10, 0.5));
    }
    assertNotEquals(initialX, monster.getX());
  }

  @Test
  public void playerJumpOnlyFromGround() {
    player.setOnGround(true);
    player.move(false, false, true); // Try jumping
    assertFalse(player.isOnGround()); // Should leave the ground
  }

  @Test
  public void playerAffectedByGravity() {
    player.setOnGround(false);
    double beforeY = player.getY();
    player.update();
    assertTrue(player.getY() > beforeY);
  }

  @Test
  void craftPotionSuccess() {
    // Add a nearby large crafting plant
    Plant craftingPlant = new Plant(ImageId.PLANT_BIG, 0, 0);
    game.addPlant(craftingPlant);

    // Player is near the plant
    game.getPlayer().setX(10);
    game.getPlayer().setY(10);

    // Add two small plants to the inventory
    Plant plant1 = new Plant(ImageId.PLANT_BLUE, 0, 0);
    Plant plant2 = new Plant(ImageId.PLANT_GREEN, 0, 0);
    game.getPlayer().getInventory().addItm(plant1);
    game.getPlayer().getInventory().addItm(plant2);

    // Try crafting
    boolean result = game.craftPotion();

    assertTrue(result, "Potion should be crafted");
    assertTrue(game.getPlayer().getInventory().getItems().stream()
      .anyMatch(i -> i instanceof Potion), "Potion should be in inventory");
  }

  @Test
  void resetRestoresGameState() {
    // Add enemy and plant to game
    Enemy enemy = new Monster(ImageId.MONSTER, 100, 100);
    Plant plant = new Plant(ImageId.PLANT_GREEN, 200, 200);
    plant.setCollected(true);
    enemy.setHealth(2);
    enemy.setX(1000);
    enemy.setY(1000);

    game.addEnemies(enemy);
    game.addPlant(plant);

    // Ensure enemy and plant were changed
    assertEquals(2, enemy.getHealth(), "Enemy should have 2 HP before reset");
    assertTrue(plant.isCollected(), "Plant has to be marked as collected");

    // Reset the game state
    game.reset();

    // Ensure values have been restored
    assertEquals(10, enemy.getHealth(), "Enemy should have 10 HP after reset");
    assertFalse(plant.isCollected(), "Plant should not be collected after reset");
    assertEquals(100, enemy.getX(), 0.1, "Enemy should return to initial X");
    assertEquals(100, enemy.getY(), 0.1, "Enemy should return to initial Y");
  }

  @Test
  void toggleInventoryAffectsItemsToDraw() {
    int withoutInventory = game.getItemsToDraw().length;

    game.toggleInventory();

    // Add an item to the player's inventory
    Plant plant = new Plant(ImageId.PLANT_GREEN, 0, 0);
    game.getPlayer().getInventory().addItm(plant);

    int withInventory = game.getItemsToDraw().length;

    assertTrue(withInventory > withoutInventory, "More items should be drawn when inventory is open");
  }
}

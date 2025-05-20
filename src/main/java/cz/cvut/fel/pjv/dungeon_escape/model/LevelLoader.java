package cz.cvut.fel.pjv.dungeon_escape.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Enemy;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Monster;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Slime;
import cz.cvut.fel.pjv.dungeon_escape.model.environment.Door;
import cz.cvut.fel.pjv.dungeon_escape.model.environment.Plant;
import cz.cvut.fel.pjv.dungeon_escape.model.items.Key;
import cz.cvut.fel.pjv.dungeon_escape.model.items.Weapon;

import java.io.File;
import java.io.IOException;


public class LevelLoader {
  public static void loadLevel(Game game, String filePath) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = mapper.readTree(new File(filePath));

    game.getCollidableObjects().clear();
    game.getItemList().clear();
    game.getEnemyList().clear();

    // Platforms
    for (JsonNode platform : root.get("platforms")) {
      Platforms platforms = new Platforms(
        ImageId.valueOf(platform.get("imageId").asText()),
        platform.get("x").asInt(),
        platform.get("y").asInt()
      );
      game.addPlatform(platforms);
    }

    // Items
    for (JsonNode item : root.get("items")) {
      String type = item.get("type").asText();
      ImageId imageId = ImageId.valueOf(item.get("imageId").asText());
      int x = item.get("x").asInt();
      int y = item.get("y").asInt();
      InventoryItem object = null;

      switch (type) {
        case "Key":
          object = new Key(imageId, x, y, item.get("id").asText());
          game.setKey((Key) object);
          break;
        case "Weapon":
          object = new Weapon(imageId, x, y);
          break;
      }

      if (object != null) game.addInventoryItem(object);
    }

    // Plants
    for (JsonNode plant : root.get("plants")) {
      Plant plants = new Plant(
        ImageId.valueOf(plant.get("imageId").asText()),
        plant.get("x").asInt(),
        plant.get("y").asInt()
      );

      game.addPlant(plants);
    }

    // Enemies
    for (JsonNode enemy : root.get("enemies")) {
      String type = enemy.get("type").asText();
      ImageId imageId = ImageId.valueOf(enemy.get("imageId").asText());
      int x = enemy.get("x").asInt();
      int y = enemy.get("y").asInt();
      Enemy enemy1 = null;

      switch (type) {
        case "Slime":
          enemy1 = new Slime(imageId, x, y);
          break;
        case "Monster":
          enemy1 = new Monster(imageId, x, y);
          break;
      }

      if (enemy1 != null) game.addEnemies(enemy1);
    }

    // Door
    JsonNode door = root.get("door");
    Door doors = new Door(ImageId.valueOf(door.get("imageId").asText()),
      door.get("x").asInt(),
      door.get("y").asInt());
    game.setDoor(doors);

    // playerStart
    JsonNode playerStart = root.get("playerStart");
    game.getPlayer().setX(playerStart.get("x").asInt());
    game.getPlayer().setY(playerStart.get("y").asInt());
  }
}

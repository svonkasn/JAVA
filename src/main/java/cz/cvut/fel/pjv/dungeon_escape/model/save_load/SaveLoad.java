package cz.cvut.fel.pjv.dungeon_escape.model.save_load;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.pjv.dungeon_escape.model.Game;
import cz.cvut.fel.pjv.dungeon_escape.model.GameStateData;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Player;
import cz.cvut.fel.pjv.dungeon_escape.view.GamePanel;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class SaveLoad {
  private static final Logger logger = Logger.getLogger(GamePanel.class.getName());
  private final String saveFile;
  Game game;
  public SaveLoad(Game game) {
    this(game, "savegame.json"); // default
  }
  public SaveLoad(Game game, String saveFile) {
    this.game = game;
    this.saveFile = saveFile;
  }
  public void saveGame() {
    Player player = game.getPlayer();
    GameStateData gameData = new GameStateData();

    gameData.setPlayerX(player.getX());
    gameData.setPlayerY(player.getY());
    gameData.setHealthPlayer(player.getHealth());
    gameData.setKeyTaken(game.getKey().isCollected());
    try {
      ObjectMapper om = new ObjectMapper();
      om.writeValue(new File(saveFile), gameData);
      logger.info("Game saved successfully");
    } catch (IOException e) {
      logger.info("Failed to save game:" + e.getMessage());
    }
  }
  public boolean loadGame() {
    try {
      ObjectMapper om = new ObjectMapper();
      GameStateData gameData = om.readValue(new File(saveFile), GameStateData.class);
      Player player = game.getPlayer();
      player.setX(gameData.getPlayerX());
      player.setY(gameData.getPlayerY());
      player.setHealth(gameData.getHealthPlayer());
      game.getKey().setCollected(gameData.isKeyTaken());
      logger.info("Game loaded successfully");
      return true;

    } catch (IOException e) {
      logger.info("Failed to load game:" + e.getMessage());
      return false;
    }
  }
  public boolean hasSavedGame() {
    return new File(saveFile).exists();
  }
}

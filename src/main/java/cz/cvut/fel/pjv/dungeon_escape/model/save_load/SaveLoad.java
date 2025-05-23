package cz.cvut.fel.pjv.dungeon_escape.model.save_load;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.pjv.dungeon_escape.model.Game;
import cz.cvut.fel.pjv.dungeon_escape.model.GameStateData;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Player;
import cz.cvut.fel.pjv.dungeon_escape.model.items.Key;
import cz.cvut.fel.pjv.dungeon_escape.view.GamePanel;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
/**
 * The SaveLoad class handles saving and loading game state to/from JSON files.
 * It manages persistent game data including player position, health, and key collection status.
 */
public class SaveLoad {
  private static final Logger logger = Logger.getLogger(GamePanel.class.getName());
  private final String saveFile;  // Path to the save game file
  private Game game;              // Reference to the current game instance

  /**
   * Constructs a SaveLoad handler with default save file location.
   *
   * @param game The Game instance to save/load
   */
  public SaveLoad(Game game) {
    this(game, "savegame.json"); // Default save file
  }

  /**
   * Constructs a SaveLoad handler with custom save file location.
   *
   * @param game The Game instance to save/load
   * @param saveFile Custom path for the save game file
   */
  public SaveLoad(Game game, String saveFile) {
    this.game = game;
    this.saveFile = saveFile;
  }

  /**
   * Saves the current game state to a JSON file.
   * Persists:
   * - Player position (X/Y coordinates)
   * - Player health
   * - Key collection status
   * Logs success/failure through the game logger.
   */
  public void saveGame() {
    Player player = game.getPlayer();
    GameStateData gameData = new GameStateData();

    // Set game state data
    gameData.setPlayerX(player.getX());
    gameData.setPlayerY(player.getY());
    gameData.setHealthPlayer(player.getHealth());
    gameData.setKeyTaken(game.getKey().isCollected());
    gameData.setWeaponTaken(game.getWeapon().isCollected());

  try {
      ObjectMapper om = new ObjectMapper();
      om.writeValue(new File(saveFile), gameData);
      logger.info("Game saved successfully");
    } catch (IOException e) {
      logger.info("Failed to save game:" + e.getMessage());
    }
  }

  /**
   * Loads game state from the save file.
   * Restores:
   * - Player position
   * - Player health
   * - Key collection status
   *
   * @return true if loading succeeded, false otherwise
   */
  public boolean loadGame() {
    try {
      ObjectMapper om = new ObjectMapper();
      GameStateData gameData = om.readValue(new File(saveFile), GameStateData.class);

      // Restore game state
      Player player = game.getPlayer();
      player.setX(gameData.getPlayerX());
      player.setY(gameData.getPlayerY());
      player.setHealth(gameData.getHealthPlayer());
      game.getKey().setCollected(gameData.isKeyTaken());
      game.getWeapon().setCollected(gameData.isWeaponTaken());

//      Key key = game.getKey();
//      key.setCollected(gameData.isKeyTaken());
//      if (key.isCollected()) {
//        player.getInventory().addItm(key);
//      }

      logger.info("Game loaded successfully");
      return true;
    } catch (IOException e) {
      logger.info("Failed to load game:" + e.getMessage());
      return false;
    }
  }

  /**
   * Checks if a saved game file exists.
   *
   * @return true if a save file exists, false otherwise
   */
  public boolean hasSavedGame() {
    return new File(saveFile).exists();
  }
}

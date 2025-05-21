package cz.cvut.fel.pjv.dungeon_escape.model.save_load;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.pjv.dungeon_escape.model.Game;
import cz.cvut.fel.pjv.dungeon_escape.model.GameStateData;
import cz.cvut.fel.pjv.dungeon_escape.model.entities.Player;
import cz.cvut.fel.pjv.dungeon_escape.model.items.Key;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.logging.Logger;
@ExtendWith(MockitoExtension.class)
class SaveLoadTest {
  // Temporary directory for test files (automatically cleaned up after test)
  @TempDir
  File tempDir;

  private SaveLoad saveLoad;
  private File saveFile;

  // Mocked game components
  @Mock(lenient = true)
  private Game game;
  @Mock(lenient = true)
  private Player player;
  @Mock(lenient = true)
  private Key key;
  @Mock(lenient = true)
  private Logger logger;

  private final String saveFilePath = "savegame.json";

  @BeforeEach
  void setUp() {
    // Create a new file in the temporary directory
    saveFile = new File(tempDir, "savegame.json");

    // Mock game behavior to return mocked player and key
    when(game.getPlayer()).thenReturn(player);
    when(game.getKey()).thenReturn(key);

    // Initialize SaveLoad with mocked game and temp file path
    saveLoad = new SaveLoad(game, saveFile.getAbsolutePath());
  }

  @AfterEach
  void tearDown() {
    // Clean up save file if it was created
    File file = new File(saveFilePath);
    if (file.exists()) {
      file.delete();
    }
  }

  @Test
  void saveGameCreatesFile() {
    // Simulate player's state to save
    when(player.getX()).thenReturn(100.0);
    when(player.getY()).thenReturn(200.0);
    when(player.getHealth()).thenReturn(5.0);
    when(key.isCollected()).thenReturn(true);

    // Attempt to save game state
    saveLoad.saveGame();

    // Verify that the file was actually created
    assertTrue(saveFile.exists(), "Save file should be created after saving the game state.");
  }

  @Test
  void testHasSavedGameReturnsFalseIfMissing() {
    // Save file does not exist yet
    assertFalse(saveLoad.hasSavedGame(), "Should return false when no save file exists.");
  }

  @Test
  void loadGameRestoresState() throws Exception {
    // Simulate game state and write it as JSON to the save file
    GameStateData data = new GameStateData();
    data.setPlayerX(123);
    data.setPlayerY(456);
    data.setHealthPlayer(10);
    data.setKeyTaken(true);

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(saveFile, data);

    // Load the game state from the file
    boolean result = saveLoad.loadGame();

    // Verify state was loaded and applied to player/key objects
    assertTrue(result, "Loading game should succeed if valid save file exists.");
    verify(player).setX(123);
    verify(player).setY(456);
    verify(player).setHealth(10);
    verify(key).setCollected(true);
  }
}

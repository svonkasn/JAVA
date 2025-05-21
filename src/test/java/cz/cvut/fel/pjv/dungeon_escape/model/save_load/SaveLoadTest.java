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
  @TempDir
  File tempDir;

  private SaveLoad saveLoad;
  private File saveFile;

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
    saveFile = new File(tempDir, "savegame.json");

    when(game.getPlayer()).thenReturn(player);
    when(game.getKey()).thenReturn(key);

    saveLoad = new SaveLoad(game, saveFile.getAbsolutePath());
  }

  @AfterEach
  void tearDown() {
    File file = new File(saveFilePath);
    if (file.exists()) {
      file.delete();
    }
  }

  @Test
  void testSaveGameCreatesFile() {
    when(player.getX()).thenReturn(100.0);
    when(player.getY()).thenReturn(200.0);
    when(player.getHealth()).thenReturn(5.0);
    when(key.isCollected()).thenReturn(true);

    saveLoad.saveGame();

    assertTrue(saveFile.exists(), "Save file should be created");
  }
  @Test
  void testHasSavedGameReturnsFalseIfMissing() {
    assertFalse(saveLoad.hasSavedGame());
  }

  @Test
  void testLoadGameRestoresState() throws Exception {
    GameStateData data = new GameStateData();
    data.setPlayerX(123);
    data.setPlayerY(456);
    data.setHealthPlayer(10);
    data.setKeyTaken(true);

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(saveFile, data);

    boolean result = saveLoad.loadGame();

    assertTrue(result);
    verify(player).setX(123);
    verify(player).setY(456);
    verify(player).setHealth(10);
    verify(key).setCollected(true);
  }
}

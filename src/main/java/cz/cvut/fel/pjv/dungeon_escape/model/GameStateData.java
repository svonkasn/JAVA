package cz.cvut.fel.pjv.dungeon_escape.model;
/**
 * Contains serializable game state data for saving/loading functionality.
 * Stores critical player and game progress information that needs to persist between sessions.
 */
public class GameStateData {
  private double playerX;
  private double playerY;
  private double healthPlayer;
  private boolean keyTaken;
  private boolean plantsTaken;
  private boolean weaponTaken;
  private boolean potionTaken;

  /**
   * Constructs an empty GameStateData object.
   * Fields should be set via setter methods before use.
   */
  public GameStateData() {}

  public double getHealthPlayer() {
    return healthPlayer;
  }

  public void setHealthPlayer(double healthPlayer) {
    this.healthPlayer = healthPlayer;
  }

  public double getPlayerX() {
    return playerX;
  }

  public double getPlayerY() {
    return playerY;
  }

  public boolean isKeyTaken() {
    return keyTaken;
  }

  public void setPlayerX(double playerX) {
    this.playerX = playerX;
  }

  public void setPlayerY(double playerY) {
    this.playerY = playerY;
  }

  public void setKeyTaken(boolean keyTaken) {
    this.keyTaken = keyTaken;
  }

  public void setPlantsTaken(boolean plantsTaken) {
    this.plantsTaken = plantsTaken;
  }

  public void setWeaponTaken(boolean weaponTaken) {
    this.weaponTaken = weaponTaken;
  }

  public void setPotionTaken(boolean potionTaken) {
    this.potionTaken = potionTaken;
  }

}



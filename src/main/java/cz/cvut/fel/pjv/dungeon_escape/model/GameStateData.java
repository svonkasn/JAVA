package cz.cvut.fel.pjv.dungeon_escape.model;




public class GameStateData {
  private double playerX;
  private double playerY;
  private double healthPlayer;
  private boolean keyTaken;

  public GameStateData(){}

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
}



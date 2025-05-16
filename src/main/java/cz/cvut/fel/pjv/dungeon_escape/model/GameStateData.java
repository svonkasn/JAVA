package cz.cvut.fel.pjv.dungeon_escape.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameStateData {
  private double playerX;
  private double playerY;
  private double healthPlayer;
  private boolean keyTaken;

  @JsonCreator
  public GameStateData(){
//    @JsonProperty("playerX") double playerX,
//    @JsonProperty("playerY") double playerY,
//    @JsonProperty("playerHealth") double healthPlayer,
//    @JsonProperty("keyTaken") boolean keyTaken) {
//    this.playerX = playerX;
//    this.playerY = playerY;
//    this.keyTaken = keyTaken;

  }

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



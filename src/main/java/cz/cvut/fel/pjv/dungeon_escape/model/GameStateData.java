package cz.cvut.fel.pjv.dungeon_escape.model;

import cz.cvut.fel.pjv.dungeon_escape.model.entities.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameStateData implements Serializable {
  private double playerX;
  private double playerY;
  private boolean keyTaken;
//  private List inventroyItems;

  public GameStateData(Player player, Game game) {
    this.playerX = player.getX();
    this.playerY = player.getY();
    this.keyTaken = game.isKeyTaken();
//    this.inventroyItems = new ArrayList<>(player.getInventory().getItems());
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
}

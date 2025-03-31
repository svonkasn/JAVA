package cz.cvut.fel.pjv.dungeon_escape.controller;
import cz.cvut.fel.pjv.dungeon_escape.model.Game;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputHandler {
  private Game game;

  public InputHandler(Scene scene, Game game) {
    this.game = game;

    scene.setOnKeyPressed(event -> handleInput(event, true));
    scene.setOnKeyReleased(event -> handleInput(event, false));
  }

  private void handleInput(KeyEvent event, boolean isPressed) {
    boolean up = event.getCode() == KeyCode.W;
    boolean down = event.getCode() == KeyCode.S;
    boolean left = event.getCode() == KeyCode.A;
    boolean right = event.getCode() == KeyCode.D;

    game.movePlayer(up, down, left, right);
  }

}

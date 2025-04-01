package cz.cvut.fel.pjv.dungeon_escape.controller;
import cz.cvut.fel.pjv.dungeon_escape.model.Game;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputHandler {
  private Game game;
  private boolean up, down, left, right, jump;


  public InputHandler(Scene scene, Game game) {
    this.game = game;

    scene.setOnKeyPressed(event -> handleInput(event, true));
    scene.setOnKeyReleased(event -> handleInput(event, false));
  }

  private void handleInput(KeyEvent event, boolean isPressed) {
    switch (event.getCode()) {
      case W -> up = isPressed;
      case S -> down = isPressed;
      case A -> left = isPressed;
      case D -> right = isPressed;
      case SPACE -> jump = isPressed;
    }

    game.movePlayer(up, down, left, right, jump);
  }

}

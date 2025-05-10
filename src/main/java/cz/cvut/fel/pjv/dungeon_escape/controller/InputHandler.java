package cz.cvut.fel.pjv.dungeon_escape.controller;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

public class InputHandler {
  private boolean up, down, left, right, jump;


  public InputHandler(Scene scene) {
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
  }
  public boolean isUpPressed() { return up; }
  public boolean isDownPressed() { return down; }
  public boolean isLeftPressed() { return left; }
  public boolean isRightPressed() { return right; }
  public boolean isJumpPressed() { return jump; }

}

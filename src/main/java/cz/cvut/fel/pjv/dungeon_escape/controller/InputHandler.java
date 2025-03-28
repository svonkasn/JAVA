package cz.cvut.fel.pjv.dungeon_escape.controller;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class InputHandler {
  private boolean upPressed = false;
  private boolean downPressed = false;
  private boolean leftPressed = false;
  private boolean rightPressed = false;

  public InputHandler(Scene scene) {
    scene.setOnKeyPressed(event -> {
      KeyCode code = event.getCode();
      switch (code) {
        case W -> upPressed = true; // Вверх
        case S -> downPressed = true; // Вниз
        case A -> leftPressed = true; // Влево
        case D -> rightPressed = true; // Вправо
        default -> {}
      }
    });

    scene.setOnKeyReleased(event -> {
      KeyCode code = event.getCode();
      switch (code) {
        case W -> upPressed = false;
        case S -> downPressed = false;
        case A -> leftPressed = false;
        case D -> rightPressed = false;
        default -> {}
      }
    });
  }

  public boolean isUpPressed() {
    return upPressed;
  }

  public boolean isDownPressed() {
    return downPressed;
  }

  public boolean isLeftPressed() {
    return leftPressed;
  }

  public boolean isRightPressed() {
    return rightPressed;
  }
}

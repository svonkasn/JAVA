package cz.cvut.fel.pjv.dungeon_escape.controller;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputHandler {
  private boolean left, right, jump, inventoryKeyPressed, attack;
  private boolean inventoryToggleProcessed;



  public InputHandler(Scene scene) {
    scene.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.Q && !inventoryToggleProcessed) {
        inventoryKeyPressed = true;
        inventoryToggleProcessed = true;
      }
      handleInput(event, true);
    });

    scene.setOnKeyReleased(event -> {
      if (event.getCode() == KeyCode.Q) {
        inventoryKeyPressed = false;
        inventoryToggleProcessed = false;
      }
      handleInput(event, false);
    });
//    scene.setOnKeyPressed(event -> handleInput(event, true));
//    scene.setOnKeyReleased(event -> handleInput(event, false));

  }

  private void handleInput(KeyEvent event, boolean isPressed) {
    switch (event.getCode()) {
      case A -> left = isPressed;
      case D -> right = isPressed;
      case SPACE -> jump = isPressed;
      case ENTER -> attack = isPressed;
    }
  }

  public boolean isLeftPressed() { return left; }
  public boolean isRightPressed() { return right; }
  public boolean isJumpPressed() { return jump; }
  public boolean shouldToggleInventory() {
    return inventoryKeyPressed;
  }
  public boolean isAttack() {
    return attack;
  }
}

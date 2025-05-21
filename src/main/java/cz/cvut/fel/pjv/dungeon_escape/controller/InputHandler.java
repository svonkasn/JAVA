package cz.cvut.fel.pjv.dungeon_escape.controller;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * The InputHandler class manages keyboard input for the game.
 * It tracks the state of relevant keys and provides methods to check their current state.
 */
public class InputHandler {
  private boolean left, right, jump, inventoryKeyPressed, attack, craftingKeyPressed;
  private boolean inventoryToggleProcessed;

  /**
   * Constructs an InputHandler and sets up key event listeners for the specified scene.
   *
   * @param scene The JavaFX Scene to listen for keyboard events on
   */
  public InputHandler(Scene scene) {
    // Set up key pressed event handler with special handling for inventory toggle
    scene.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.Q && !inventoryToggleProcessed) {
        inventoryKeyPressed = true;
        inventoryToggleProcessed = true;
      }
      handleInput(event, true);
    });

    // Set up key released event handler with special handling for inventory toggle
    scene.setOnKeyReleased(event -> {
      if (event.getCode() == KeyCode.Q) {
        inventoryKeyPressed = false;
        inventoryToggleProcessed = false;
      }
      handleInput(event, false);
    });
  }

  /**
   * Processes a key event and updates the corresponding input state.
   *
   * @param event The KeyEvent to process
   * @param isPressed Whether the key is being pressed (true) or released (false)
   */
  private void handleInput(KeyEvent event, boolean isPressed) {
    switch (event.getCode()) {
      case A -> left = isPressed;
      case D -> right = isPressed;
      case SPACE -> jump = isPressed;
      case ENTER -> attack = isPressed;
      case E -> craftingKeyPressed = isPressed;
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
  public boolean isCraftingKeyPressed() {
    return craftingKeyPressed;
  }
}

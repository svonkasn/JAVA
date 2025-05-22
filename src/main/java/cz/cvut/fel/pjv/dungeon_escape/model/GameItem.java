package cz.cvut.fel.pjv.dungeon_escape.model;


import javafx.geometry.BoundingBox;

/**
 * The GameItem class represents a basic game object with position and visual representation.
 * It serves as the base class for all interactive objects in the game world.
 */
public class GameItem {
  /**
   * The visual representation of this game item
   */
  protected final ImageId imageId;

  /**
   * The x-coordinate position in the game world
   */
  protected double x;

  /**
   * The y-coordinate position in the game world
   */
  protected double y;

  /**
   * Constructs a new GameItem with specified visual representation and position.
   *
   * @param imageId The ImageId defining the visual appearance of this item
   * @param x       The initial x-coordinate position in the game world
   * @param y       The initial y-coordinate position in the game world
   */
  public GameItem(ImageId imageId, double x, double y) {
    this.imageId = imageId;
    this.x = x;
    this.y = y;
  }

  /**
   * Gets the bounding box for collision detection.
   * The bounding box is slightly smaller than the visual representation
   * (20 pixels shorter on the y-axis) to provide better gameplay feel.
   *
   * @return A BoundingBox representing this item's collision area
   */
  public BoundingBox getBoundingBox() {
    double paddingY = 20; // Reduces height for better collision feel

    return new BoundingBox(
      x,
      y,
      imageId.getWidth(),
      imageId.getHeight() - paddingY
    );
  }
  public ImageId getImageId() {
    return imageId;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public void setX(double x) {
    this.x = x;
  }

  public void setY(double y) {
    this.y = y;
  }
}

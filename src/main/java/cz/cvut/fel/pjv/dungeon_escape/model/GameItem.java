package cz.cvut.fel.pjv.dungeon_escape.model;


import javafx.geometry.BoundingBox;

public class GameItem {
  protected final ImageId imageId;
  protected double x;
  protected double y;

  public GameItem(ImageId imageId, double x, double y) {
    this.imageId = imageId;
    this.x = x;
    this.y = y;
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

  public BoundingBox getBoundingBox() {
    double paddingY = 20;

    return new BoundingBox(x, y,
      imageId.getWidth(), imageId.getHeight() - paddingY);
  }
}

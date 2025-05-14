package cz.cvut.fel.pjv.dungeon_escape.model;

public class Platforms extends GameItem{
  public Platforms(ImageId imageId, double x, double y, int width, int height) {
    super(imageId, x, y);
    setX(width);
    setY(height);
 }
}

package cz.cvut.fel.pjv.dungeon_escape.model;

public enum ImageId {
  BGR("back.png"),
  PLAYER("player1.png"),
  PLATFORM("Sprite-0003.png");

  private final String fileName;
  private double width;
  private double height;

  ImageId(String fileName) {
    this.fileName = fileName;
  }

  public String getFileName() {
    return fileName;
  }

  public double getWidth() {
    return width;
  }

  public void setWidth(double width) {
    this.width = width;
  }

  public double getHeight() {
    return height;
  }

  public void setHeight(double height) {
    this.height = height;
  }
}


package cz.cvut.fel.pjv.dungeon_escape.model;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

public enum ImageId {
  BGR("background.png"),
  PLAYER("player2.png"),
  PLATFORM("platrform1.png"),
  GROUND("ground.png"),
  INVENTORY("dialogues2.png"),
  KEY("key.png"),
  DOOR("door.png"),
  SLIME("slime.png"),
  MONSTER("crow.png"),
  PLANT_BLUE("plant1.png"),
  PLANT_GREEN("plant2.png"),
  PLANT_BIG("placeToCraft.png"),
  WEAPON("weapon.png"),
  POTION("potion.png");

  private final String fileName;
  private final Image image;
  private double width;
  private double height;

  ImageId(String fileName) {
    this.fileName = fileName;
    this.image = new Image(getClass().getResourceAsStream("/" + fileName));
  }

  public Image getImage() {
    return image;
  }

  public PixelReader getPixelReader() {
    return image.getPixelReader();
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


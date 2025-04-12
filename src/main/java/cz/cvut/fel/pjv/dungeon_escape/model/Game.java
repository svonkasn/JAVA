package cz.cvut.fel.pjv.dungeon_escape.model;

import cz.cvut.fel.pjv.dungeon_escape.model.entities.Player;

public class Game {
  private final GameItem backround;
  private final Player player;

  public Game() {
    backround = new GameItem(ImageId.BGR, 0, 0);
    player = new Player(ImageId.PLAYER,10, 450, 0);
  }
  public void update(){
    player.update();
  }
  public void movePlayer(boolean up, boolean down, boolean left, boolean right, boolean jump) {
    player.move(up, down, left, right, jump);

  }
  public boolean checkCollision(double nextX, double nextY) {
    double width = ImageId.BGR.getWidth();
    double height = ImageId.BGR.getHeight();
    System.out.println("X: " + nextX + " width: " + backround.getBoundingBox().getMaxX() );
    System.out.println("X:" + player.getBoundingBox().getMaxY() + " height: " + backround.getBoundingBox().getMaxY());

    return nextX <= backround.getBoundingBox().getMaxX()
      && nextX >= backround.getBoundingBox().getMinX()
      && nextY <= backround.getBoundingBox().getMaxY()
      && nextY >= backround.getBoundingBox().getMinY();
  }

  public boolean checkCollision() {
    double width = ImageId.BGR.getWidth();
    double height = ImageId.BGR.getHeight();
    System.out.println(ImageId.BGR.getWidth() + " " + ImageId.BGR.getHeight());
    System.out.println(player.getX());
    System.out.println(player.getY());
    return  player.getX() > width &&
      player.getY() > height;

  }

  public DrawableItem[] getItemsToDraw() {
    return new DrawableItem[]{
      new DrawableItem(backround.getImageId(), backround.getX(), backround.getY()),
      new DrawableItem(player.getImageId(), player.getX(), player.getY())
    };
  }

}

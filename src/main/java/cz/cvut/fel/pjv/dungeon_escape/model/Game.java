package cz.cvut.fel.pjv.dungeon_escape.model;

public class Game {
  private final GameItem backround;
  private final Player player;

  public Game() {
    backround = new GameItem(ImageId.BGR, 0, 0);
    player = new Player(ImageId.PLAYER,10, 10, 0);
  }
  public void update(){
    player.update();
  }
  public void movePlayer(boolean up, boolean down, boolean left, boolean right) {
    player.move(up, down, left, right);
  }

  public DrawableItem[] getItemsToDraw() {
    return new DrawableItem[]{
      new DrawableItem(backround.getImageId(), backround.getX(), backround.getY()),
      new DrawableItem(player.getImageId(), player.getX(), player.getY())
    };
  }

}

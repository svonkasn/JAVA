package cz.cvut.fel.pjv.dungeon_escape.model;

public class Game {
  private final GameItem backround;
//  private final Player player;

  public Game() {
    backround = new GameItem(ImageId.BGR, 0, 0);
//    player = new Player(ImageId.PLAYER,)
  }
  public void update(){

  }
  public DrawableItem[] getItemsToDraw() {
    return new DrawableItem[]{
      new DrawableItem(backround.getImageId(), backround.getX(), backround.getY()),

    };
  }

}

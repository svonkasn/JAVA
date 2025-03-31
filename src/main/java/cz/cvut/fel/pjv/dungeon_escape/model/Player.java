package cz.cvut.fel.pjv.dungeon_escape.model;

public class Player extends GameItem{
    private double speedY;
    private int keys = 0;
    private int health;
    private double speed = 15;


  public Player(ImageId imageId, int x, int y,int health) {
    super(imageId, x, y);
    this.speedY = 0;
    this.health = health;

  }
  public void update(){

  }

  public void move(boolean up, boolean down, boolean left, boolean right) {
    if (up){
      y -= speed;
//      speedY -= speed;
      System.out.println("Up");
    }
    if (down) y += speed;
    if (left) x -= speed;
    if (right) x += speed;
  }

  public void addKey(){
    this.keys++;
  }


  public int getHealth() {
    return health;
  }

}

package cz.cvut.fel.pjv.dungeon_escape.model;

public class Player extends GameItem{
    private final double acceleration = 0.1;
    private double gravity = 0.5;
    private int keys = 0;
    private int health;
    private double speed = 15;


  public Player(ImageId imageId, int x, int y,int health) {
    super(imageId, x, y);
//    this.speedY = 0;
    this.health = health;

  }
  public void update(){

  }

  public void move(boolean up, boolean down, boolean left, boolean right, boolean jump) {
    if (up) y -= speed;
    if (down) y += speed;
    if (left) x -= speed;
    if (right) x += speed;

    if(jump){
      System.out.println("jump");
      y -= speed;
      speed -= acceleration;
    }
  }

  public void addKey(){
    this.keys++;
  }


  public int getHealth() {
    return health;
  }

}

package cz.cvut.fel.pjv.dungeon_escape.model;

public class Player {
    private int x;
    private int y;
    private int keys = 0;
    private int health;
    private double speed = 5;


  public Player(int x, int y, int health) {
    this.x = x;
    this.y = y;
    this.health = health;

  }

  public void move(boolean up, boolean down, boolean left, boolean right) {
    if (up){
      y -= speed;
      System.out.println("Up");
    }
    if (down) y += speed;
    if (left) x -= speed;
    if (right) x += speed;
  }

  public void addKey(){
    this.keys++;
  }
  public int getX() {
    return x;
  }
  public int getY() {
    return y;
  }

  public int getHealth() {
    return health;
  }

}

package cz.cvut.fel.pjv.dungeon_escape.model;

public class Player {
    private int x;
    private int y;
    private int keys = 0;
    private int health;



  public Player(int x, int y, int health) {
    this.x = x;
    this.y = y;
    this.health = health;

  }

  public void move(int dx, int dy){
    this.x += dx;
    this.y += dy;
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

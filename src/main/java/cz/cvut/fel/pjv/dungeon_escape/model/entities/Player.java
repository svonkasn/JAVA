package cz.cvut.fel.pjv.dungeon_escape.model.entities;


import cz.cvut.fel.pjv.dungeon_escape.model.Game;
import cz.cvut.fel.pjv.dungeon_escape.model.GameItem;
import cz.cvut.fel.pjv.dungeon_escape.model.ImageId;

public class Player extends GameItem {
    private final double acceleration = 0.1;
    private double gravity = 0.5;
    private int health;
    private double speed = 15;
  private double velocityY = 0;
  private boolean isJumping = false;


  public Player(ImageId imageId, int x, int y, int health) {
    super(imageId, x, y);
//    this.speedY = 0;
    this.health = health;

  }
  public void update() {

  }
  public void move(boolean up, boolean down, boolean left, boolean right, boolean jump) {

    Game game = new Game();

    if (up) y -= speed;
    if (down) y += speed;
    if (left) x -= speed;
    if (right) x += speed;
    System.out.println(game.checkCollision(x, y));
  }


//  public void addKey(){
//    this.keys++;
//  }


  public int getHealth() {
    return health;
  }

  public void heal(int heal) {
    health += heal;
  }
}

package cz.cvut.fel.pjv.dungeon_escape.controller;

import cz.cvut.fel.pjv.dungeon_escape.model.Player;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputHandler extends KeyAdapter {
//  private Player player;
//  private int dx = 0, dy = 0;
  public boolean upPressed;
  public boolean downPressed;
  public boolean leftPressed;
  public boolean rightPressed;

//  public InputHandler(Player player) {
//    this.player = player;
//  }

  public void keyPressed(KeyEvent e) {
    int code = e.getKeyCode();
    if (code == KeyEvent.VK_W)
      upPressed = true;
    if (code == KeyEvent.VK_S)
      downPressed = true;
    if (code == KeyEvent.VK_D)
      leftPressed = true;
    if(code == KeyEvent.VK_A)
      rightPressed = true;
    }

  public void keyRelease(KeyEvent e) {
    int code = e.getKeyCode();
    if (code == KeyEvent.VK_W)
      upPressed = false;
    if (code == KeyEvent.VK_S)
      downPressed = false;
    if (code == KeyEvent.VK_D)
      leftPressed = false;
    if(code == KeyEvent.VK_A)
      rightPressed = false;
  }


//    switch (e.getKeyCode()) {
//      case KeyEvent.VK_UP:
//        player.move(0, -1);
//        break;
//      case KeyEvent.VK_DOWN:
//        player.move(0, 1);
//        break;
//      case KeyEvent.VK_LEFT:
//        player.move(-1, 0);
//        break;
//    }
  }




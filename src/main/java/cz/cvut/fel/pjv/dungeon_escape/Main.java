package cz.cvut.fel.pjv.dungeon_escape;

import cz.cvut.fel.pjv.dungeon_escape.view.GamePanel;
import javafx.application.Application;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;

public class Main {


  public static void main(String[] args) {
//    try {
//      LogManager.getLogManager().readConfiguration(
//        new FileInputStream("logging.properties"));
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
      Application.launch(GamePanel.class, args);

    }
}

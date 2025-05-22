module cz.cvut.fel.pjv.dungeon_escape {
  requires javafx.controls;
  requires com.fasterxml.jackson.databind;
  requires java.logging;

//  requires javafx.fxml;


//  opens cz.cvut.fel.pjv.dungeon_escape to javafx.fxml;
  exports cz.cvut.fel.pjv.dungeon_escape.model;
  exports cz.cvut.fel.pjv.dungeon_escape.view;
  exports cz.cvut.fel.pjv.dungeon_escape.model.save_load;
}

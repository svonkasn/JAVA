package cz.cvut.fel.pjv.dungeon_escape.model;


import java.util.ArrayList;
import java.util.List;

public class Inventory {

  private final int capacity = 5;
  private List<GameItem> items = new ArrayList<GameItem>();

  public void addItm(GameItem item){
    if( items.size() < capacity ){
      items.add(item);
    }
  }

  public void removeItm(GameItem item){
    if ( items.size() > 0 ){
      items.remove(item);
    }
  }

  public void useItem(){
    /* TODO
    * */
  }
}

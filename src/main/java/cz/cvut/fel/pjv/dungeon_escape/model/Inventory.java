package cz.cvut.fel.pjv.dungeon_escape.model;


import java.util.ArrayList;
import java.util.List;

public class Inventory {

  private final int capacity = 5;
  private List<GameItem> items = new ArrayList<>();
  private float slotX = 0;
  private float slotY = 0;
  private float slotSize = 40;

  public void addItm(GameItem item){
    if( items.size() < capacity ){
      items.add(item);
    }
    updateItemPosition(item);
  }
  private void updateItemPosition(GameItem item){
    for ( int i = 0; i < items.size(); i++ ){
      items.get(i).setX(slotX + i * (slotSize + 5));  // Располагаем предметы в ряд
      items.get(i).setY(slotY);
    }
  }
  public void removeItm(GameItem item){
    if ( items.size() > 0 ){
      items.remove(item);
    }
  }
  public List<GameItem> getItems(){
    return items;
  }

  public void useItem(){
    /* TODO
    * */
  }
}

package cz.cvut.fel.pjv.dungeon_escape.model;


import cz.cvut.fel.pjv.dungeon_escape.model.items.Key;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    updateItemPosition();
  }
  public void removeItm(GameItem item){
    if ( items.size() > 0 ){
      items.remove(item);
    }
    updateItemPosition();
  }
  public List<GameItem> getItems(){
    return items;
  }
  public boolean hasKey(){
    return items.stream().anyMatch(item -> item instanceof Key);
  }
  public boolean useKey() {
    Optional<GameItem> key = items.stream()
      .filter(item -> item instanceof Key)
      .findFirst();

    if (key.isPresent()) {
      items.remove(key.get());
      updateItemPosition();
      return true;
    }
    return false;
  }
  public void useItem(GameItem item){
    if (items.contains(item)) {
      // TODO: Logic of using items
      items.remove(item);
      updateItemPosition();
    }
  }
  public boolean isFull() {
    return items.size() >= capacity;
  }

  private void updateItemPosition(){
    for ( int i = 0; i < items.size(); i++ ){
      items.get(i).setX(slotX + i * (slotSize + 5));  // Располагаем предметы в ряд
      items.get(i).setY(slotY);
    }
  }
}

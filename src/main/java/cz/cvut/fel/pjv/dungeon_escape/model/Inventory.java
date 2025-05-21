package cz.cvut.fel.pjv.dungeon_escape.model;


import cz.cvut.fel.pjv.dungeon_escape.model.items.Key;
import cz.cvut.fel.pjv.dungeon_escape.model.items.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Inventory {

  private final int capacity = 5;
  private List<InventoryItem> items = new ArrayList<>();
  private float slotX = 0;
  private float slotY = 0;
  private float slotSize = 40;

  public void addItm(InventoryItem item){
    if( items.size() < capacity ){
      items.add(item);
    }
    updateItemPosition();
  }
  public void removeItm(InventoryItem item){
    if ( items.size() > 0 ){
      items.remove(item);
    }
    updateItemPosition();
  }
  public List<InventoryItem> getItems(){
    return items;
  }
  public boolean useKey() {
    Optional<InventoryItem> key = items.stream()
      .filter(item -> item instanceof Key)
      .findFirst();

    if (key.isPresent()) {
      items.remove(key.get());
      updateItemPosition();
      return true;
    }
    return false;
  }
  public boolean hasKey(){
    return items.stream().anyMatch(item -> item instanceof Key);
  }
  public boolean hasWeapon(){
    return items.stream().anyMatch(item -> item instanceof Weapon);
  }

  public void useItem(InventoryItem item){
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
      items.get(i).setX(slotX + i * (slotSize + 40));  // items in a row
      items.get(i).setY(slotY);
    }
  }
  public void reset(){
    items.clear();
  }
}

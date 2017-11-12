package edu.cnm.deepdive.eb.flashme.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.HashMap;
import java.util.Map;

// my foreign key would be here

// connects my class to a specific table in my database
@DatabaseTable(tableName = "CARD")
// student class corresponds with STUDENT table
public class Card {

  // generatedId = true, auto increments an id
  @DatabaseField(columnName = "CARD_ID", generatedId = true)
  private int id;

  @DatabaseField(columnName = "FRONT", canBeNull = false)
  private String front;

  @DatabaseField(columnName = "BACK", canBeNull = false)
  private String back;

  @DatabaseField(columnName = "TYPE", canBeNull = false)
  private String type = "Level 1";

  @DatabaseField(columnName = "DECK_ID", canBeNull = false, foreign = true, foreignAutoRefresh = true)
  private Deck deck;

  public int getId() {
    return id;
  }

  public String getFront() {
    return front;
  }

  public void setFront(String front) {
    this.front = front;
  }

  public String getBack() {
    return back;
  }

  public void setBack(String back) {
    this.back = back;
  }

  public Deck getDeck() {
    return deck;
  }

  public void setDeck(Deck deck) {
    this.deck = deck;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    Map<String, Object> map = new HashMap<>();
//    map.put("id", id);
    map.put("front", front);
//    map.put("back", back);
//    map.put("type", type);


    return map.get("front").toString();
  }


}

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


  @DatabaseField(columnName = "CARD", canBeNull = false)
  private String name;



  @DatabaseField(columnName = "DECK_ID", canBeNull = false, foreign = true, foreignAutoRefresh = true)
  private Deck deck;

  public int getId() {
    return id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Deck getDeck() {
    return deck;
  }

  public void setDeck(Deck deck) {
    this.deck = deck;
  }

  @Override
  public String toString() {
    Map<String, Object> map = new HashMap<>();
//    map.put("id", id);
    map.put("name", name);

    return map.toString();
  }


}

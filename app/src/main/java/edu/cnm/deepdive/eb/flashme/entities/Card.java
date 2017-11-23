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
  private int type;

  @DatabaseField(columnName = "IMAGE_ONE", canBeNull = false)
  private String imageOne;

  @DatabaseField(columnName = "IMAGE_TWO", canBeNull = false)
  private String imageTwo;

  @DatabaseField(columnName = "IMAGE_THREE", canBeNull = false)
  private String imageThree;

  @DatabaseField(columnName = "IMAGE_FOUR", canBeNull = false)
  private String imageFour;

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

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getImageOne() {
    return imageOne;
  }

  public void setImageOne(String imageOne) {
    this.imageOne = imageOne;
  }

  public String getImageTwo() {
    return imageTwo;
  }

  public void setImageTwo(String imageTwo) {
    this.imageTwo = imageTwo;
  }

  public String getImageThree() {
    return imageThree;
  }

  public void setImageThree(String imageThree) {
    this.imageThree = imageThree;
  }

  public String getImageFour() {
    return imageFour;
  }

  public void setImageFour(String imageFour) {
    this.imageFour = imageFour;
  }


  @Override
  public String toString() {
    Map<String, Object> map = new HashMap<>();
    map.put("id", id);
    map.put("front", front);
    map.put("back", back);
    map.put("type", type);
//    map.put("deck_id", deck);
        return map.get("front").toString();
//    return map.toString();
  }


}

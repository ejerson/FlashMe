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

  /**
   * Provides access to the Card Id.
   * @return Returns a card id
   */
  public int getId() {
    return id;
  }

  /**
   * Provides access to the Front value of a card.
   * @return Returns the front value of a card.
   */
  public String getFront() {
    return front;
  }

  /**
   * Allow for the mutation of card front value.
   * @param front Passes user created front.
   */
  public void setFront(String front) {
    this.front = front;
  }

  /**
   * Provides access to the back value of a card.
   * @return Returns the back value of a card.
   */
  public String getBack() {
    return back;
  }

  /**
   * Allow for the mutation of back value.
   * @param back Passes user created back.
   */
  public void setBack(String back) {
    this.back = back;
  }

  /**
   * Provides access to the deck value.
   * @return Returns the deck value of a card.
   */
  public Deck getDeck() {
    return deck;
  }

  /**
   * Allow for the mutation of deck value.
   * @param deck Passes user created deck.
   */
  public void setDeck(Deck deck) {
    this.deck = deck;
  }

  /**
   * Provides access to the type value.
   * @return Returns the type value of a card.
   */
  public int getType() {
    return type;
  }

  /**
   * Allow for the mutation of type value.
   * @param type Passes card type.
   */
  public void setType(int type) {
    this.type = type;
  }

  /**
   * Provides access to the imageOne value.
   * @return Returns the imageOne value of a card.
   */
  public String getImageOne() {
    return imageOne;
  }

  /**
   * Allow for the mutation of imageOne value.
   * @param imageOne Passes card imageOne.
   */
  public void setImageOne(String imageOne) {
    this.imageOne = imageOne;
  }

  /**
   * Provides access to the imageTwo value.
   * @return Returns the imageTwo value of a card.
   */
  public String getImageTwo() {
    return imageTwo;
  }

  /**
   * Allow for the mutation of imageTwo value.
   * @param imageTwo Passes card imageTwo.
   */
  public void setImageTwo(String imageTwo) {
    this.imageTwo = imageTwo;
  }

  /**
   * Provides access to the imageThree value.
   * @return Returns the imageThree value of a card.
   */
  public String getImageThree() {
    return imageThree;
  }

  /**
   * Allow for the mutation of imageThree value.
   * @param imageThree Passes card imageThree.
   */
  public void setImageThree(String imageThree) {
    this.imageThree = imageThree;
  }

  /**
   * Provides access to the imageFour value.
   * @return Returns the imageFour value of a card.
   */
  public String getImageFour() {
    return imageFour;
  }

  /**
   * Allow for the mutation of imageFour value.
   * @param imageFour Passes card imageFour.
   */
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
    map.put("deck_id", deck);
        return map.get("front").toString();
//    return map.toString();
  }


}

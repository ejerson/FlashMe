package edu.cnm.deepdive.eb.flashme.entities;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.HashMap;
import java.util.Map;

// connects my class to a specific table in my database
@DatabaseTable(tableName = "DECK")
public class Deck {

  // generatedId = true, auto increments an id
  @DatabaseField(columnName = "DECK_ID", generatedId = true)
  private int id;

  // will get the current time and use it as a timestamp
//  @DatabaseField(columnName = "CREATED", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
//  format = "yyyy-MM-dd HH:mm:ss", canBeNull = false, readOnly = true)
//  private Timestamp created;

  @DatabaseField(columnName = "DECK", canBeNull = false)
  private String name;

  @DatabaseField(columnName = "CARD_POOL", canBeNull = false)
  private int pool;


  @ForeignCollectionField
  public ForeignCollection<Card> cards;

  /**
   * Provides access to the id value of a deck.
   * @return Returns the id of a deck.
   */
  public int getId() {
    return id;
  }

//  public Timestamp getCreated() {
//    return created;
//  }

  /**
   * Provides access to the name value of a deck.
   * @return Returns the name of a deck.
   */
  public String getName() {
    return name;
  }

  /**
   * Allow for the mutation of deck name value.
   * @param name Passes user created name.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Provides access to the pool value of a deck.
   * @return Returns the pool value of a deck.
   */
  public int getPool() {
    return pool;
  }

  /**
   * Allow for the mutation of deck pool value.
   * @param pool Passes pool value.
   */
  public void setPool(int pool) {
    this.pool = pool;
  }

  public ForeignCollection<Card> getCards() {
    return cards;
  }

  @Override
  public String toString() {
    Map<String, Object> map = new HashMap<>();
//    map.put("id", id);
    map.put("name", name);
//    map.put("created", created);
    map.put("pool", pool);

    return map.toString();
  }
}

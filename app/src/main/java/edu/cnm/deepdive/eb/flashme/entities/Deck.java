package edu.cnm.deepdive.eb.flashme.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;


// connects my class to a specific table in my database
@DatabaseTable(tableName = "DECK")
// student class corresponds with STUDENT table
public class Deck {

  // generatedId = true, auto increments an id
  @DatabaseField(columnName = "DECK_ID", generatedId = true)
  private int id;

  // will get the current time and use it as a timestamp
  @DatabaseField(columnName = "CREATED", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
  format = "yyyy-MM-dd HH:mm:ss", canBeNull = false, readOnly = true)
  private Timestamp created;

  @DatabaseField(columnName = "DECK", canBeNull = false)
  private String name;

  public int getId() {
    return id;
  }

  public Timestamp getCreated() {
    return created;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    Map<String, Object> map = new HashMap<>();
    map.put("id", id);
    map.put("name", name);
    map.put("name", created);

    return map.toString();
  }
}

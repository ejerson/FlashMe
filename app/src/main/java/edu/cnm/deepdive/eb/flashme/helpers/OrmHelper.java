package edu.cnm.deepdive.eb.flashme.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import edu.cnm.deepdive.eb.flashme.entities.Card;
import edu.cnm.deepdive.eb.flashme.entities.Deck;
import java.sql.SQLException;

public class OrmHelper extends OrmLiteSqliteOpenHelper{

  private static final String DATABASE_NAME = "deck.db";
  private static final int DATABASE_VERSION = 1;

  //Data access object (Daos are parametarized by entity type <Deck> and the data type of the primary key <Integer>)
  private Dao<Deck, Integer> deckDao = null;
  private Dao<Card, Integer> cardDao = null;

  public OrmHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
    // deck.class is a class object, which gives code access to a world information about the class
    try {
      TableUtils.createTable(connectionSource, Deck.class);
      TableUtils.createTable(connectionSource, Card.class);
//      populateDatabase();

    } catch (SQLException e) {
      // RunTimeException is not a checked exception
      throw new RuntimeException(e);
    }

  }

  @Override
  public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion,
      int newVersion) {

  }



  // Think of an entities type as a class
  @Override
  public void close() {
    // any Dao reference must be set to null to make sure Garbage collection has access to the object
    deckDao = null;
    super.close();
  }

  //Method to handle my Dao
  // synchronized manages multiple threads that are accessing this method, one at a time is the rule
  public synchronized Dao<Deck, Integer> getDeckDao() throws SQLException {
    if(deckDao == null) {
      deckDao = getDao(Deck.class);
    }
    return deckDao;
  }

  public synchronized Dao<Card, Integer> getCardDao() throws SQLException {
    if(cardDao == null) {
      cardDao = getDao(Card.class);
    }
    return cardDao;
  }

  private void populateDatabase() throws SQLException {
//     a lot of deserialization depends on a no parameter constructor
//    Deck deck = new Deck();
//    deck.setName("Fudge Nickleson");
//    getDeckDao().create(deck);
////    data access object?
//
//    Card card = new Card();
//    card.setName("yey");
//
//    card.setDeck(deck);
//    getCardDao().create(card);
//
//    List<Deck> testList = getDeckDao().queryForAll();
//    Assert.assertEquals(testList.size(), 1);
//    Assert.assertEquals(testList.get(0).getCards().size(), 1);
//
  }

  public interface OrmInteraction {

    OrmHelper getHelper();

  }

}

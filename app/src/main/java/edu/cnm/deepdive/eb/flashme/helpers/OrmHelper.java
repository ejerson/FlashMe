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

/**
 * A class that handles OrmLite database related functions.
 */
public class OrmHelper extends OrmLiteSqliteOpenHelper {

  private static final String DATABASE_NAME = "deck.db";
  private static final int DATABASE_VERSION = 1;

  /**
   * Data access object (Daos are parametarized by entity type <deck> and the data type of the
   * primary key <Integer>)
   */
  private Dao<Deck, Integer> deckDao = null;
  /**
   * Data access object (Daos are parametarized by entity type <card> and the data type of the
   * primary key <Integer>)
   */
  private Dao<Card, Integer> cardDao = null;

  public OrmHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
    try {
      TableUtils.createTable(connectionSource, Deck.class);
      TableUtils.createTable(connectionSource, Card.class);
//      populateDatabase();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion,
      int newVersion) {
  }

  @Override
  public void close() {
    // any Dao reference must be set to null to make sure Garbage collection has access to the object
    deckDao = null;
    super.close();
  }

  /**
   * Handles traffic to the deck Dao.
   */
  public synchronized Dao<Deck, Integer> getDeckDao() throws SQLException {
    if (deckDao == null) {
      deckDao = getDao(Deck.class);
    }
    return deckDao;
  }

  /**
   * Handles traffic to the Card Dao.
   */
  public synchronized Dao<Card, Integer> getCardDao() throws SQLException {
    if (cardDao == null) {
      cardDao = getDao(Card.class);
    }
    return cardDao;
  }

  /**
   * Populates the database directly without user input for testing purposes.
   * @throws SQLException
   */
  private void populateDatabase() throws SQLException {
//     a lot of deserialization depends on a no parameter constructor
//    deck deck = new deck();
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
//    List<deck> testList = getDeckDao().queryForAll();
//    Assert.assertEquals(testList.size(), 1);
//    Assert.assertEquals(testList.get(0).getCards().size(), 1);
//
  }

  /**
   * Allows other class/component to receive the getHelper method behaviour.
   */
  public interface OrmInteraction {

    OrmHelper getHelper();

    void releaseHelper();
  }

}

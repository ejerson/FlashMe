package edu.cnm.deepdive.eb.flashme.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.adapters.DeckListRecyclerViewAdapter;
import edu.cnm.deepdive.eb.flashme.entities.Card;
import edu.cnm.deepdive.eb.flashme.entities.Deck;
import edu.cnm.deepdive.eb.flashme.fragments.AddDeckFragment;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import java.sql.SQLException;

/**
 * An activity representing a list of Decks. This activity handles
 * the implementation of the recycler view and contains a method
 * that is connected to a button that allows a user to open the AddCardFragment.
 *
 */
public class DeckListActivity
    extends AppCompatActivity
    implements OrmHelper.OrmInteraction {

  private OrmHelper helper;
  DeckListRecyclerViewAdapter deckListRecyclerViewAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Creates an instance of the helper class, this line forces android to create my database if it doesn't exist already
    getHelper().getWritableDatabase().close();
    setContentView(R.layout.activity_deck_list);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    refreshRecyclerView();
  }

  @Override
  protected void onStart() {
    super.onStart();
    getHelper();


    refreshRecyclerView();
  }

  @Override
  protected void onStop() {
    super.onStop();
    releaseHelper();
  }

  /**
   * Updates the recyclerView when a new deck is added.
   */
  public void refreshRecyclerView() {
    View recyclerView = findViewById(R.id.deck_list);
    assert recyclerView != null;
    setupRecyclerView((RecyclerView) recyclerView);
  }


  /**
   * Creates a new view adapter and passing it deck names.
   * @param recyclerView passes RecyclerView
   */
  private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
    try {
      deckListRecyclerViewAdapter = new DeckListRecyclerViewAdapter(getHelper().getDeckDao().queryForAll());
      recyclerView
          .setAdapter(deckListRecyclerViewAdapter);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }



  @Override
  public OrmHelper getHelper() {
    if (helper == null) {
      helper = OpenHelperManager.getHelper(this, OrmHelper.class);
    }
    return helper;
  }

  @Override
  public synchronized void releaseHelper() {
    if (helper != null) {
      OpenHelperManager.releaseHelper();
      helper = null;
    }
  }

  /**
   * Provides user a way to enter a deck name.
   * @param view passes the container view
   */
  public void showAddDialog(View view) {
    AddDeckFragment dialog = new AddDeckFragment();
    dialog.show(getSupportFragmentManager(), "AddDeckFragment");
  }

  public void deleteDeck(View view) {
    try {
      for (int i = 0; i < deckListRecyclerViewAdapter.getDeckDeletePool().size(); i++) {
        Dao<Card, Integer> cardDao = helper.getCardDao();
        DeleteBuilder<Card, Integer> cardDeleteBuilder = cardDao.deleteBuilder();
        cardDeleteBuilder.where().eq("DECK_ID", deckListRecyclerViewAdapter.getDeckDeletePool().get(i));
        cardDeleteBuilder.delete();

        Dao<Deck, Integer> deckDao = helper.getDeckDao();
        DeleteBuilder<Deck, Integer> deckDeleteBuilder = deckDao.deleteBuilder();
        deckDeleteBuilder.where().eq("DECK_ID", deckListRecyclerViewAdapter.getDeckDeletePool().get(i));
        deckDeleteBuilder.delete();

      }
      refreshRecyclerView();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }


  }

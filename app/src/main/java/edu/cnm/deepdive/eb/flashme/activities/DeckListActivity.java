package edu.cnm.deepdive.eb.flashme.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.adapters.DeckListRecyclerViewAdapter;
import edu.cnm.deepdive.eb.flashme.entities.Card;
import edu.cnm.deepdive.eb.flashme.entities.Deck;
import edu.cnm.deepdive.eb.flashme.fragments.AddDeckFragment;
import edu.cnm.deepdive.eb.flashme.fragments.DeckListFragment;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;

import static edu.cnm.deepdive.eb.flashme.fragments.DeckMemberFragment.DECK_ID;

/**
 * An activity representing a list of Decks. This activity handles
 * the implementation of the recycler view and contains a method
 * that is connected to a button that allows a user to open the AddCardFragment.
 *
 */
public class DeckListActivity
    extends AppCompatActivity
    implements OrmHelper.OrmInteraction {

  private String userId;

  private  List<Deck> Decks = new ArrayList<>();
//  private  List<User> Users = new ArrayList<>();
  private OrmHelper helper = null;

  DeckListRecyclerViewAdapter deckListRecyclerViewAdapter;

  FragmentManager manager = getSupportFragmentManager();
  DeckListFragment fragment = (DeckListFragment) manager.findFragmentById(R.id.fragment_container);

  RecyclerView recyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Creates an instance of the helper class, this line forces android to create my database if it doesn't exist already
    getHelper().getWritableDatabase().close();
    setContentView(R.layout.activity_deck_detail);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);


    if (fragment == null) {
      fragment = new DeckListFragment();
      Bundle args = new Bundle();
      args.putInt(DECK_ID, getIntent().getIntExtra(DECK_ID, 0));
      fragment.setArguments(args);
      manager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }


//    refreshRecyclerView();
  }

  /**
   * Updates the recyclerView when a new deck is added.
   */
  public void refreshRecyclerView() {
    recyclerView = (RecyclerView) findViewById(R.id.deck_list);
    assert recyclerView != null;
    setupRecyclerView(recyclerView);
  }

  public void setupRecyclerView(@NonNull RecyclerView recyclerView) {
    deckListRecyclerViewAdapter = new DeckListRecyclerViewAdapter(Decks);
    recyclerView.setAdapter(deckListRecyclerViewAdapter);
  }

  /**
   * Creates a new view adapter and passing it deck names.
//   * @param recyclerView passes RecyclerView
   */


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

  @Override
  protected void onStart() {
    super.onStart();
    getHelper();
//    refreshRecyclerView();
  }

  @Override
  protected void onStop() {
    super.onStop();
    releaseHelper();
  }


  /**
   * Provides user a way to enter a deck name.
   * @param view passes the container view
   */
  public void showAddDeckDialog(View view) {
    AddDeckFragment dialog = new AddDeckFragment();
    dialog.show(getSupportFragmentManager(), "AddDeckFragment");
  }

  public void deleteDeck() {
    try {
      for (int i = 0; i < deckListRecyclerViewAdapter.getDeckDeletePool().size(); i++) {
        Dao<Card, Integer> cardDao = helper.getCardDao();
        DeleteBuilder<Card, Integer> cardDeleteBuilder = cardDao.deleteBuilder();
        cardDeleteBuilder.where()
            .eq("DECK_ID", deckListRecyclerViewAdapter.getDeckDeletePool().get(i));
        cardDeleteBuilder.delete();
        Dao<Deck, Integer> deckDao = helper.getDeckDao();
        DeleteBuilder<Deck, Integer> deckDeleteBuilder = deckDao.deleteBuilder();
        deckDeleteBuilder.where()
            .eq("DECK_ID", deckListRecyclerViewAdapter.getDeckDeletePool().get(i));
        deckDeleteBuilder.delete();
      }
      refreshRecyclerView();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }


  public List<Deck> getDecks() {
    return Decks;
  }

}


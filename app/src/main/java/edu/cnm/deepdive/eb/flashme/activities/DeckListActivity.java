package edu.cnm.deepdive.eb.flashme.activities;

import static edu.cnm.deepdive.eb.flashme.fragments.DeckMemberFragment.DECK;
import static edu.cnm.deepdive.eb.flashme.fragments.DeckMemberFragment.DECK_ID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.entities.Card;
import edu.cnm.deepdive.eb.flashme.entities.Deck;
import edu.cnm.deepdive.eb.flashme.fragments.AddDeckFragment;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import java.sql.SQLException;
import java.util.List;

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

  // gets invoked after onCreate
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
      recyclerView
          .setAdapter(new DeckItemRecyclerViewAdapter(getHelper().getDeckDao().queryForAll()));
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
   * Manages my the Recycler view for the deck name list.
   */
  public class DeckItemRecyclerViewAdapter
      extends RecyclerView.Adapter<DeckItemRecyclerViewAdapter.ViewHolder> {

    /**
     * Contains a list of individual deck.
     */
    private final List<Deck> Decks;

    /**
     * Constructor for the List<deck> Decks field.
     * @param decks Passes user created decks
     */
    public DeckItemRecyclerViewAdapter(List<Deck> decks) {
      Decks = decks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.deck_list_content, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
      holder.deck = Decks.get(position);
      holder.deckView.setText(holder.deck.getName());
      holder.deckPool.setText(String.valueOf(holder.deck.getPool()));
      holder.deckDeleteView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          try {
            // TODO add a warning fragment to prevent users from accidentally pressing the delete button
            Dao<Card, Integer> cardDao = helper.getCardDao();
            DeleteBuilder<Card, Integer> cardDeleteBuilder = cardDao.deleteBuilder();
            cardDeleteBuilder.where().eq("DECK_ID", holder.deck.getId());
            cardDeleteBuilder.delete();

            Dao<Deck, Integer> deckDao = helper.getDeckDao();
            DeleteBuilder<Deck, Integer> deckDeleteBuilder = deckDao.deleteBuilder();
            deckDeleteBuilder.where().eq("DECK_ID", holder.deck.getId());
            deckDeleteBuilder.delete();

            refreshRecyclerView();
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        }
      });

      /** Passes the value of the deck id onto DeckMemberActivity */
      holder.deckView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Context context = v.getContext();
          Intent intent = new Intent(context, DeckMemberActivity.class);
          intent.putExtra(DECK_ID, holder.deck.getId());
          intent.putExtra(DECK, holder.deck.getName());

          context.startActivity(intent);
        }
      });
    }

    // Adapter connects me to data, its job is to construct
    // a collection of views object to be used by a list/recycler view
    @Override
    public int getItemCount() {
      return Decks.size();
    }

    // construct a representation for a deck in the list
    public class ViewHolder extends RecyclerView.ViewHolder {

      public final View view;
      public final TextView deckView;
      public final ImageButton deckDeleteView;
      public final TextView deckPool;
      public Deck deck;

      public ViewHolder(View view) {
        super(view);
        this.view = view;
        deckView = view.findViewById(R.id.deck_name);
        deckPool = view.findViewById(R.id.deck_pool);
        deckDeleteView = view.findViewById(R.id.delete_deck);
      }

      @Override
      public String toString() {
        return super.toString() + " '" + deckView.getText() + "'";
      }
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

}

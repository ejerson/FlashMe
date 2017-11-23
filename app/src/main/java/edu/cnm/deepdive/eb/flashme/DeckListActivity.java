package edu.cnm.deepdive.eb.flashme;

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
import edu.cnm.deepdive.eb.flashme.entities.Card;
import edu.cnm.deepdive.eb.flashme.entities.Deck;
import edu.cnm.deepdive.eb.flashme.fragments.AddContentFragment;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import java.sql.SQLException;
import java.util.List;

/**
 * An activity representing a list of Decks. */

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

    View recyclerView = findViewById(R.id.deck_list);
    assert recyclerView != null;
    setupRecyclerView((RecyclerView) recyclerView);
  }

  // gets invoked after onCreate
  @Override
  protected void onStart() {
    super.onStart();
    getHelper();
  }

  @Override
  protected void onStop() {
    super.onStop();
    releaseHelper();
  }

  /** Updates my recyclerView when a new Deck is added*/
  public void refreshRecyclerView(){
    View recyclerView = findViewById(R.id.deck_list);
    assert recyclerView != null;
    setupRecyclerView((RecyclerView) recyclerView);
  }

  /** Creates a new view adapter and passing it deck names */
  private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
    try {
      recyclerView.setAdapter(new DeckItemRecyclerViewAdapter(getHelper().getDeckDao().queryForAll()));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  // we manage memory leaks in android by reusing our database references
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

  /** Manages my the Recycler view for the deck name list */
  public class DeckItemRecyclerViewAdapter
      extends RecyclerView.Adapter<DeckItemRecyclerViewAdapter.ViewHolder> {

    /** Contains a list of individual deck */
    private final List<Deck> mDecks;

    /** Constructor for the List<Deck> mDecks field */
    public DeckItemRecyclerViewAdapter(List<Deck> decks) {
      mDecks = decks;
    }

    /** Returns a new view from ViewHolder */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.deck_list_content, parent, false);
      return new ViewHolder(view);
    }

    /** Binds the value ???? */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
      holder.mDeck = mDecks.get(position);
      holder.mDeckView.setText(mDecks.get(position).getName());
      holder.mDeckDeleteView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          try {
          // TODO add a warning fragment to prevent users from accidentally pressing the button
//            showAddDialog(view);
            Dao<Card, Integer> cardDao = helper.getCardDao();
            DeleteBuilder<Card, Integer> cardDeleteBuilder = cardDao.deleteBuilder();
            cardDeleteBuilder.where().eq("DECK_ID", holder.mDeck.getId());
            cardDeleteBuilder.delete();

            Dao<Deck, Integer> deckDao = helper.getDeckDao();
            DeleteBuilder<Deck, Integer> deckDeleteBuilder = deckDao.deleteBuilder();
            deckDeleteBuilder.where().eq("DECK_ID", holder.mDeck.getId());
            deckDeleteBuilder.delete();

            refreshRecyclerView();
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        }
      });
//      holder.mCreatedView.setText(mValues.get(position).getCreated().toString());

      /** Passes the value of the Deck id onto DeckMemberActivity */
      holder.mDeckView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Intent intent = new Intent(context, DeckMemberActivity.class);
            intent.putExtra(DECK_ID, holder.mDeck.getId());
            intent.putExtra(DECK, holder.mDeck.getName());

            context.startActivity(intent);
        }
      });
    }

    // Adapter connects me to data, its job is to construct
    // a collection of views object to be used by a list/recycler view
    @Override
    public int getItemCount() {
      return mDecks.size();
    }

    // construct a representation for a deck in the list
    public class ViewHolder extends RecyclerView.ViewHolder {

      public final View mView;
      public final TextView mDeckView;
      public final ImageButton mDeckDeleteView;
      public Deck mDeck;

      public ViewHolder(View view) {
        super(view);
        mView = view;
        mDeckView = view.findViewById(R.id.deck_name);
        mDeckDeleteView = view.findViewById(R.id.delete_deck);
      }

      @Override
      public String toString() {
        return super.toString() + " '" + mDeckView.getText() + "'";
      }
    }
  }

  public void showAddDialog(View view) {
    AddContentFragment dialog = new AddContentFragment();
    dialog.show(getSupportFragmentManager(), "AddContentFragment");
  }

}

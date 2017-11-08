package edu.cnm.deepdive.eb.flashme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import edu.cnm.deepdive.eb.flashme.fragments.DeckMemberFragment;
import edu.cnm.deepdive.eb.flashme.fragments.DeckMemberFragment.DeckMemberFragmentDaoInteraction;
import edu.cnm.deepdive.eb.flashme.entities.Deck;
import edu.cnm.deepdive.eb.flashme.fragments.AddContentFragment;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import java.sql.SQLException;
import java.util.List;

/**
 * An activity representing a list of Decks. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link DeckMemberActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two vertical panes.
 */
public class DeckListActivity
    extends AppCompatActivity
    implements DeckMemberFragmentDaoInteraction {

  /**
   * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
   */
  private boolean mTwoPane;
  private OrmHelper helper = null;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Creates an instance of the helper class, this line forces android to create my database if it doesn't exist already
    getHelper().getWritableDatabase().close();
    setContentView(R.layout.activity_deck_list);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(getTitle());

//    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//    fab.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//            .setAction("Action", null).show();
//      }
//    });

    View recyclerView = findViewById(R.id.deck_list);
    assert recyclerView != null;
    setupRecyclerView((RecyclerView) recyclerView);

    if (findViewById(R.id.deck_detail_container) != null) {
      // The detail container view will be present only in the
      // large-screen layouts (res/values-w900dp).
      // If this view is present, then the
      // activity should be in two-pane mode.
      mTwoPane = true;
    }
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
    // make sure that my database doesn't consume memory, otherwise it would remain open if I don't
    // release the database if my app is in the background
    releaseHelper();
  }

  public void refreshRecyclerView(){
    View recyclerView = findViewById(R.id.deck_list);
    assert recyclerView != null;
    setupRecyclerView((RecyclerView) recyclerView);
  }

  // creates a new view adapter and passing it items from dummy content
  private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
    try {
      recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(getHelper().getDeckDao(Deck.class).queryForAll()));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  // we manage memory leaks in android by reusing our database references
  public OrmHelper getHelper() {
    if (helper == null) {
      helper = OpenHelperManager.getHelper(this, OrmHelper.class);
    }
    return helper;
  }

  public synchronized void releaseHelper() {
    if (helper != null) {
      OpenHelperManager.releaseHelper();
      helper = null;
    }
  }

  @Override
  public Dao<Deck, Integer> getDaoDeck() throws SQLException {
    return getHelper().getDeckDao(Deck.class);
  }

  public class SimpleItemRecyclerViewAdapter
      extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    // change this so I can populate my list with deck
    private final List<Deck> mValues;



    public SimpleItemRecyclerViewAdapter(List<Deck> items) {
      mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.deck_list_content, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
      holder.mItem = mValues.get(position);
//      holder.mIdView.setId(mValues.get(position).getId());
      holder.mDeckView.setText(mValues.get(position).getName());
//      holder.mCreatedView.setText(mValues.get(position).getCreated().toString());

      holder.mView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putInt(DeckMemberFragment.ARG_ITEM_ID, holder.mItem.getId());
            DeckMemberFragment fragment = new DeckMemberFragment();
            fragment.setArguments(arguments); // this is how I pass arguments into a fragment
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.deck_detail_container, fragment)
                .commit();
          } else {
            Context context = v.getContext();
            Intent intent = new Intent(context, DeckMemberActivity.class);
            intent.putExtra(DeckMemberFragment.ARG_ITEM_ID, holder.mItem.getId());

            context.startActivity(intent);
          }
        }
      });
    }

    // Adapter connects me to data, its job is to construct a collection of views object to be used by
    // a list/recycler view
    @Override
    public int getItemCount() {
      return mValues.size();
    }

    // construct a representation for a student in our list
    public class ViewHolder extends RecyclerView.ViewHolder {

      public final View mView;
//      public final TextView mIdView;
      public final TextView mDeckView;
//      public final TextView mCreatedView;
      public Deck mItem;

      public ViewHolder(View view) {
        super(view);
        mView = view;
//        mIdView = (TextView) view.findViewById(R.id.deck_id);
        mDeckView = (TextView) view.findViewById(R.id.deck_name);
//        mCreatedView = (TextView) view.findViewById(R.id.deck_created);
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

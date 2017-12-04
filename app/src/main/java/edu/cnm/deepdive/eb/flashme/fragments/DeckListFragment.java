package edu.cnm.deepdive.eb.flashme.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.activities.DeckListActivity;
import edu.cnm.deepdive.eb.flashme.adapters.DeckListRecyclerViewAdapter;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import java.sql.SQLException;

/**
 * A fragment representing a single Card List screen. This fragment is contained in the {@link
// * DeckMemberActivity}.
 */
public class DeckListFragment
    extends Fragment
    implements OnClickListener {

  private OrmHelper helper;
  DeckListRecyclerViewAdapter deckListRecyclerViewAdapter;
  Button delete_deck;
//
//  /**
//   * Stores the value of my helper to be used by the queries within this fragment.
//   */

  RecyclerView recyclerView;

  View rootView;
//
//  /**
//   * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon
//   * screen orientation changes).
//   */
  public DeckListFragment() {
  }
//
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    helper = ((OrmHelper.OrmInteraction) getActivity()).getHelper();

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.activity_deck_list, container, false);




//    try {
      recyclerView = rootView.findViewById(R.id.deck_list);

    delete_deck = rootView.findViewById(R.id.button_delete_deck);
    delete_deck.setOnClickListener(this);

//    ((DeckListActivity) getActivity()).refreshRecyclerView();
    refreshRecyclerView();
//    Bundle deleteDeckPool = getActivity().getIntent().getExtras();
//    deleteDeckPool.putStringArrayList(""deckListRecyclerViewAdapter.getDeckDeletePool().toString());

//      deckListRecyclerViewAdapter = new DeckListRecyclerViewAdapter(helper.getDeckDao().queryForAll());
//      recyclerView.setAdapter(deckListRecyclerViewAdapter);
//      RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//      recyclerView.setLayoutManager(layoutManager);
//    } catch (SQLException e) {
//      throw new RuntimeException(e);
//    }
    return rootView;
    }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.button_delete_deck:
        ((DeckListActivity) getActivity()).deleteDeck();
        break;
    }
  }

  /**
   * Updates the recyclerView when a new deck is added.
   */
  public void refreshRecyclerView() {
    recyclerView = rootView.findViewById(R.id.deck_list);
    assert recyclerView != null;
    setupRecyclerView(recyclerView);
  }

  /**
   * Creates a new view adapter and passing it deck names.
   * @param recyclerView passes RecyclerView
   */
  private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
    try {
      deckListRecyclerViewAdapter = new DeckListRecyclerViewAdapter(helper.getDeckDao().queryForAll());
      recyclerView
          .setAdapter(deckListRecyclerViewAdapter);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    helper = ((OrmHelper.OrmInteraction) getActivity()).getHelper();
  }


}

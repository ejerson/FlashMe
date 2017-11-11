package edu.cnm.deepdive.eb.flashme.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import edu.cnm.deepdive.eb.flashme.CardActivity;
import edu.cnm.deepdive.eb.flashme.DeckListActivity;
import edu.cnm.deepdive.eb.flashme.DeckMemberActivity;
import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.entities.Card;
import edu.cnm.deepdive.eb.flashme.entities.Deck;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import java.sql.SQLException;
import java.util.List;

/**
 * A fragment representing a single Student detail screen. This fragment is either contained in a
 * {@link DeckListActivity} in two-pane mode (on tablets) or a {@link DeckMemberActivity} on
 * handsets.
 */
public class DeckMemberFragment extends Fragment implements OnClickListener {


  /**
   * The fragment argument representing the item ID that this fragment represents.
   */
  public static final String DECK_ID = "deck_id";

  /**
   * The student content this fragment is presenting.
   */
  private Deck mItem;

  private OrmHelper helper = null;

  private int deckId;
  private Deck deck;
  private View rootView;
  private ListView cardList;
  private ArrayAdapter<Card> cardAdapter;



  /**
   * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon
   * screen orientation changes).
   */
  public DeckMemberFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments().containsKey(DECK_ID)) {
      // Load the dummy content specified by the fragment
      // arguments. In a real-world scenario, use a Loader
      // to load content from a content provider.

      deckId = getArguments().getInt(DECK_ID);

//      CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) getActivity()
//          .findViewById(R.id.toolbar);
//      if (appBarLayout != null) {
//        appBarLayout.setTitle(deck.getName());
//      }
    } else {
      deckId = 0;
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.deck_detail, container, false);


       cardList = rootView.findViewById(R.id.card_name);
       cardAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
       cardList.setAdapter(cardAdapter);

    Button one = rootView.findViewById(R.id.button_add_card);
    one.setOnClickListener(this);

    Button two = rootView.findViewById(R.id.button_review_card);
    two.setOnClickListener(this);

//    (
//        new OnClickListener() {
//          @Override
//          public void onClick(View view) {
//            Toast.makeText(getActivity(), "Yey", Toast.LENGTH_SHORT).show();
//          }
//        });
    return rootView;
  }

//  public OrmHelper getHelper() {
//    if (helper == null) {
//      helper = OpenHelperManager.getHelper(getContext(), OrmHelper.class);
//    }
//    return helper;
//  }
//
//  public synchronized void releaseHelper() {
//    if (helper != null) {
//      OpenHelperManager.releaseHelper();
//      helper = null;
//    }
//  }

  @Override
  public void onClick(View v) {

    switch (v.getId()) {
      case R.id.button_add_card:
        AddCardFragment dialog = new AddCardFragment();
        Bundle args = new Bundle();
        args.putInt(AddCardFragment.DECK_ID_KEY, deck.getId());
        dialog.setArguments(args);
        dialog.show(getActivity().getSupportFragmentManager(), "AddCardFragment");
        break;
      case R.id.button_review_card:
        startActivity(new Intent(getActivity(), CardActivity.class));

        break;
      default:
        break;
    }

  }



  @Override
  public void onStart() {
    super.onStart();
    helper = ((OrmHelper.OrmInteraction) getActivity()).getHelper();
    if (deckId > 0) {
      try {
        Dao<Deck, Integer> deckDao = helper.getDeckDao();
        Dao<Card, Integer> cardDao = helper.getCardDao();
        deck = deckDao.queryForId(getArguments().getInt(DECK_ID));
//        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(deck.getName());
        QueryBuilder<Card, Integer> builder = cardDao.queryBuilder();
        builder.where().eq("DECK_ID", deck.getId());
        List<Card> cards = cardDao.query(builder.prepare());
        cardAdapter.addAll(cards);
        cardAdapter.notifyDataSetChanged();
//        rootView.forceLayout();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }  else {
      deck = null;
    }
  }


}

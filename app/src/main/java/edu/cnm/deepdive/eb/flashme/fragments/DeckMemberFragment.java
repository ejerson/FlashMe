package edu.cnm.deepdive.eb.flashme.fragments;

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
//  private Deck mItem;

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
//      try {
//        mItem = ((DeckMemberFragmentDaoInteraction) getContext())
//            .getDaoDeck().queryForId(getArguments().getInt(DECK_ID));
//      } catch (SQLException e) {
//        throw new RuntimeException(e);
//      }
//
//      Activity activity = this.getActivity();
//      CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity
//          .findViewById(R.id.toolbar_layout);
//      if (appBarLayout != null) {
//        appBarLayout.setTitle(mItem.getName());
//      }
    } else {
      deckId = 0;
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.deck_detail, container, false);

    // Show the dummy content as text in a TextView.
//    if (mItem != null) {
       cardList = rootView.findViewById(R.id.deck_detail);

       cardAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);

       cardList.setAdapter(cardAdapter);
//    try {
//      cardList.setAdapter(new ArrayAdapter<> (getContext(), android.R.layout.simple_list_item_1, getHelper().getCardDao(Card.class).queryForEq("DECK_ID", deck.getId())));
//
//
//    } catch (SQLException e) {
//      throw new RuntimeException(e);
//    }
//    }

    ((Button) rootView.findViewById(R.id.button_add_card)).setOnClickListener(this);
//        new OnClickListener() {
//          @Override
//          public void onClick(View view) {
//              AddCardFragment dialog = new AddCardFragment();
//              Bundle args = new Bundle();
//              args.putInt(AddCardFragment.DECK_ID_KEY, deck.getId());
//              dialog.setArguments(args);
//              dialog.show(getActivity().getSupportFragmentManager(), "AddCardFragment");
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




//  @Override
//  public void onStop() {
//    super.onStop();
//    // make sure that my database doesn't consume memory, otherwise it would remain open if I don't
//    // release the database if my app is in the background
//    releaseHelper();
//  }


//  public interface DeckMemberFragmentDaoInteraction {
//
//    OrmHelper getHelper();
//
//    Dao<Deck, Integer> getDaoDeck() throws SQLException;
//  }
//
//  public interface CardMemberFragmentDaoInteraction {
//    Dao<Card, Integer> getDaoCard() throws SQLException;
//  }

  @Override
  public void onStart() {
    super.onStart();
//    getHelper();

    helper = ((OrmHelper.OrmInteraction) getActivity()).getHelper();
    if (deckId > 0) {
      try {
        Dao<Deck, Integer> deckDao = helper.getDeckDao();
        Dao<Card, Integer> cardDao = helper.getCardDao();
        deck = deckDao.queryForId(getArguments().getInt(DECK_ID));
        QueryBuilder<Card, Integer> builder = cardDao.queryBuilder();
        builder.where().eq("DECK_ID", deck.getId());
        List<Card> cards = cardDao.query(builder.prepare());
        cardAdapter.addAll(cards);
        cardAdapter.notifyDataSetChanged();
        cardList.invalidateViews();
        rootView.forceLayout();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }  else {
      deck = null;
    }
  }

  @Override
  public void onClick(View view) {
    AddCardFragment dialog = new AddCardFragment();
    Bundle args = new Bundle();
    args.putInt(AddCardFragment.DECK_ID_KEY, deck.getId());
    dialog.setArguments(args);
    dialog.show(getActivity().getSupportFragmentManager(), "AddCardFragment");
  }
}

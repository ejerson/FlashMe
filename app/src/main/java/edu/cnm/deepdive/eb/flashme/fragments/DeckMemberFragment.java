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
import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.entities.Card;
import edu.cnm.deepdive.eb.flashme.entities.Deck;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import java.sql.SQLException;
import java.util.List;

/**
 * A fragment representing a single Deck detail screen. This fragment is contained in a
 * {@link DeckListActivity}.
 */
public class DeckMemberFragment extends Fragment implements OnClickListener {

  /** The fragment argument representing the item ID that this fragment represents. */
  public static final String DECK_ID = "deck_id";

  private OrmHelper helper;
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
      deckId = getArguments().getInt(DECK_ID);
    } else {
      deckId = 0;
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.deck_detail, container, false);

    cardList = rootView.findViewById(R.id.card_front);
    cardAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
    cardList.setAdapter(cardAdapter);

    Button add_card_button = rootView.findViewById(R.id.button_add_card);
    add_card_button.setOnClickListener(this);

    Button review_card_button = rootView.findViewById(R.id.button_review_card);
    review_card_button.setOnClickListener(this);

    return rootView;
  }


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
//        startActivity(new Intent(getActivity(), CardActivity.class));
        ReviewCardFragment fragment = new ReviewCardFragment();
        args = new Bundle();
        args.putInt(DeckMemberFragment.DECK_ID,
            getActivity().getIntent().getIntExtra(DeckMemberFragment.DECK_ID, 0));
        fragment.setArguments(args); // bundle
        getActivity().getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment_container, fragment).commit();
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
        QueryBuilder<Card, Integer> builder = cardDao.queryBuilder();
        builder.where().eq("DECK_ID", deck.getId());
        List<Card> cards = cardDao.query(builder.prepare());
        cardAdapter.addAll(cards);
        cardAdapter.notifyDataSetChanged();
//        rootView.forceLayout();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    } else {
      deck = null;
    }
  }

    @Override
    public String toString() {
      return super.toString();

    }


  }

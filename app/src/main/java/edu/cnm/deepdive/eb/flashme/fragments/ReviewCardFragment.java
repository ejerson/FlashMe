package edu.cnm.deepdive.eb.flashme.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.entities.Card;
import edu.cnm.deepdive.eb.flashme.entities.Deck;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import java.sql.SQLException;
import java.util.List;

public class ReviewCardFragment extends Fragment implements OnClickListener {

  private static final String DECK_ID = "deck_id";
  public static final String CARD_ID = "card_id";

  private OrmHelper helper;
  private int deckId;
  private Deck deck;
  private Card card;
  private int cardId;
  private View rootView;

  private ArrayAdapter<Card> singleAdapter;
  private List<Card> deckCardCollection;
  private TextView cardReview;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if(getArguments().containsKey(DECK_ID)) {
      deckId = getArguments().getInt(DECK_ID);
    } else {
      deckId = 0;
    }

    if (getArguments().containsKey(CARD_ID)) {
      cardId = getArguments().getInt(CARD_ID);
    } else {
      cardId = 0;
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    rootView = inflater.inflate(R.layout.review_card_fragment, container, false);

    singleAdapter = new ArrayAdapter<>(getContext(), R.layout.single_card);

    Button card_review_button = rootView.findViewById(R.id.button_review);
    card_review_button.setOnClickListener(this);

    Button level_up_button = rootView.findViewById(R.id.button_level_up);
    level_up_button.setOnClickListener(this);

    return rootView;
  }

  @Override
  public void onClick(View view) {

    switch(view.getId()) {
      case R.id.button_review:
       randomCard();
        break;

      case R.id.button_level_up:
//        if (cardId > 0) {

    // duplicate the parent state, which is the listView
//    try {
//      String yey = deckCardCollection.get(4).getFront().toString();
//
//      Toast.makeText(getActivity(), yey, Toast.LENGTH_SHORT).show();
//
//
//
//
////      Dao<Deck, Integer> deckDao = helper.getDeckDao();
//      Dao<Card, Integer> cardDao = helper.getCardDao();
////      deck = deckDao.queryForId(getArguments().getInt(DECK_ID));
//      card = cardDao.queryForId(getArguments().getInt(CARD_ID));
//      // this is how I make my query more specific by using the and keyword
//
//      UpdateBuilder<Card, Integer> updateBuilder = cardDao.updateBuilder();
//      // set the criteria like you would a QueryBuilder
//      updateBuilder.where().eq("CARD_ID", card.getId());
//      // update the value of your field(s)
//      updateBuilder.updateColumnValue("TYPE", 2);
//      updateBuilder.update();
//    } catch (SQLException e) {
//      throw new RuntimeException(e);
//    }
//        } else {
//          deck = null;
//        }
        break;
        default:
          break;
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    helper = ((OrmHelper.OrmInteraction) getActivity()).getHelper();

    try {

      // TODO change this so it only query TYPE that has a specific DECK_ID
      Dao<Deck, Integer> deckDao = helper.getDeckDao();
      Dao<Card, Integer> cardDao = helper.getCardDao();
      deck = deckDao.queryForId(getArguments().getInt(DECK_ID));
//      card = cardDao.queryForId(getArguments().getInt(CARD_ID));
// get our query builder from the DAO
      QueryBuilder<Card, Integer> queryBuilder =
          cardDao.queryBuilder();
      // this is how I make my query more specific by using the and keyword
      Where<Card, Integer> where = queryBuilder.where();
// the name field must be equal to "foo"
      where.eq("TYPE", 1);
//// and
      where.and();
// only retrieve cards that belongs to a specific deck
      where.eq("DECK_ID", deck.getId());
// prepare my sql statement
      PreparedQuery<Card> preparedQuery = queryBuilder.prepare();
// query for all decks that have 1 as a type
      deckCardCollection = cardDao.query(preparedQuery);

      randomCard();
      singleAdapter.addAll(deckCardCollection);
//      singleAdapter.notifyDataSetChanged();



    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public final void randomCard() {
    cardReview = rootView.findViewById(R.id.review_random_card);
    cardReview.setText(showNextCard());
  }

  public final String showNextCard() {
    // query for a random card

//    currentCard = deckCardCollection.get(randomNumberGenerator());
    return deckCardCollection.get(randomNumberGenerator()).toString();
  }

  public int randomNumberGenerator() {
    int max = deckCardCollection.size();
    int min = 0;
    int range = max - min;

    int rng = (int) (Math.random() * range) + min;
    // generate random number from 0 to singleCard.size()
    return rng;
  }



}

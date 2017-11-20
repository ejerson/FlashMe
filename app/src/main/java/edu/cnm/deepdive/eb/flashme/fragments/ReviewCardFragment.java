package edu.cnm.deepdive.eb.flashme.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.entities.Card;
import edu.cnm.deepdive.eb.flashme.entities.Deck;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import java.sql.SQLException;
import java.util.ArrayList;
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

//  private ArrayAdapter<Card> singleAdapter;

  // create a pool of cards from each levels
  private List<Card> cardL1Collection;
  private List<Card> cardL2Collection;

  private ArrayList<String> cardL1Front = new ArrayList<>();
//  private List<Card> cardL2Front = new ArrayList<>();
  // 0 -14 = L1
  // 15-24 = L2
  // 55- 29 = L3

  private List<Card> cardL2Front;


  private TextView cardReview;
  private TextView cardCheck;

  private int currentRandomNumber;

  // create a pool of cards to be reviewed

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

//    singleAdapter = new ArrayAdapter<>(getContext(), R.layout.single_card);

    Button card_review_button = rootView.findViewById(R.id.button_review);
    card_review_button.setOnClickListener(this);

    Button card_check_button = rootView.findViewById(R.id.button_check);
    card_check_button.setOnClickListener(this);

    Button level_up_button = rootView.findViewById(R.id.button_level_up);
    level_up_button.setOnClickListener(this);



    return rootView;
  }

  @Override
  public void onClick(View view) {

    switch(view.getId()) {
      case R.id.button_review:
        // TODO able to show 30 cards, 15 from L1, 10 from L2, and 5 from L3;
        while (cardL1Front.size() < 14) {
          for (int i = 0; i < 15; i++) {
            cardL1Front.add(cardL1Collection.get(i).getFront());
          }

        }
       randomCard();

//       for (int i = 0; i < cardL1Front.length - 10; i++) {
//           cardL1Front[i] = cardL1Collection.get(i).getFront();
//       }

//       for (int i = 15; i < cardL1Front.length; i++) {
//         cardL1Front[i] = cardL2Collection.get(i - 15).getFront();
//       }

        String yes = String.valueOf(cardL1Front.size());
        Toast.makeText(getActivity(), yes, Toast.LENGTH_SHORT).show();

        break;

      case R.id.button_check:
        cardCheck();

        break;

      case R.id.button_level_up:

    // duplicate the parent state, which is the listView
    try {
      // TODO downgrade the level of the card being reviewed

//      Dao<Deck, Integer> deckDao = helper.getDeckDao();
      Dao<Card, Integer> cardDao = helper.getCardDao();
//      // this is how I make my query more specific by using the and keyword
      UpdateBuilder<Card, Integer> updateBuilder = cardDao.updateBuilder();
//      // set the criteria like you would a QueryBuilder
      updateBuilder.where().eq("CARD_ID", cardL1Collection.get(currentRandomNumber).getId());
//      // update the value of your field(s)
      updateBuilder.updateColumnValue("TYPE", 2);
      updateBuilder.update();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
        break;
        default:
          break;
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    helper = ((OrmHelper.OrmInteraction) getActivity()).getHelper();

    /** get level 1 cards */
    try {
      Dao<Deck, Integer> deckDao = helper.getDeckDao();
      Dao<Card, Integer> cardDao = helper.getCardDao();
      deck = deckDao.queryForId(getArguments().getInt(DECK_ID));
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
      PreparedQuery<Card> preparedQueryL1 = queryBuilder.prepare();
// query for all decks that have 1 as a type
      cardL1Collection = cardDao.query(preparedQueryL1);

      randomCard();
//      singleAdapter.addAll(cardL1Collection);
//      singleAdapter.notifyDataSetChanged();



    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    /** get level 2 cards */
    try {
      Dao<Deck, Integer> deckDao = helper.getDeckDao();
      Dao<Card, Integer> cardDao = helper.getCardDao();
      deck = deckDao.queryForId(getArguments().getInt(DECK_ID));
// get our query builder from the DAO
      QueryBuilder<Card, Integer> queryBuilder =
          cardDao.queryBuilder();
      // this is how I make my query more specific by using the and keyword
      Where<Card, Integer> where = queryBuilder.where();
// the name field must be equal to "foo"
      where.eq("TYPE", 2);
//// and
      where.and();
// only retrieve cards that belongs to a specific deck
      where.eq("DECK_ID", deck.getId());
// prepare my sql statement
      PreparedQuery<Card> preparedQuery = queryBuilder.prepare();
// query for all decks that have 2 as a type
      cardL2Collection = cardDao.query(preparedQuery);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }

  public final void randomCard() {
    cardReview = rootView.findViewById(R.id.review_random_card);
    cardReview.setText(showNextCard());
  }

  public final void cardCheck() {
    cardCheck = rootView.findViewById(R.id.check_random_card);
    cardCheck.setText(cardL1Collection.get(currentRandomNumber).getBack().toString());
  }

  public final String showNextCard() {
    // query for a random card

    return cardL1Collection.get(randomNumberGenerator()).getFront().toString();
  }

  public int randomNumberGenerator() {
    int max = cardL1Collection.size();
    int min = 0;
    int range = max - min;

    currentRandomNumber = (int) (Math.random() * range) + min;
    // generate random number from 0 to singleCard.size()
    return currentRandomNumber;
  }


}

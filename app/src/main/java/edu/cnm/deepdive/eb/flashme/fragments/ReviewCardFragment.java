package edu.cnm.deepdive.eb.flashme.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.squareup.picasso.Picasso;
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


  private ArrayList<Card> cardL1Id = new ArrayList<>();
  private ArrayList<Card> cardL2Id = new ArrayList<>();

  // 15 id from cardL1 and 10 id cardL2
  private ArrayList<Card> reviewPool = new ArrayList<>();


  private ArrayList<String> graduatedFront = new ArrayList<>();

  private TextView cardReview;
  private TextView cardCheck;

  private ImageView image_one;
  private ImageView image_two;
  private ImageView image_three;
  private ImageView image_four;

  private Card currentRandomCard;
  private String currentCardFront;
  private String currentCardBack;

  private List<Card> cards;

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

    image_one = rootView.findViewById(R.id.image_one);
    image_two = rootView.findViewById(R.id.image_two);
    image_three = rootView.findViewById(R.id.image_three);
    image_four = rootView.findViewById(R.id.image_four);

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


        randomNumberGenerator();
        randomCard();


        // a user gets a 30 out of 30 when cards have either been promoted or demoted
        // place all cards to the graduated pile - this pile contains cards that have gone
        // beyond the highest level.
        // the counter increases if
          // a level one card is promoted to level 2
          // a level two card is promoted to level 3
          // a level three card is graduated
             // graduated cards can be place back into the pile as level 1 (for now)

        String yes = String.valueOf(currentRandomCard);
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
      updateBuilder.where().eq("CARD_ID", currentRandomCard.getId());
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

    /** Review Pool Population */
    if (reviewPool.size() > 25) {
      Toast.makeText(getActivity(), "Please increase review limit.", Toast.LENGTH_SHORT).show();
    } else {

      if(cardL1Collection.isEmpty() && cardL1Id.size() > 15) {
        Toast.makeText(getActivity(), "Please create cards", Toast.LENGTH_SHORT).show();
      } else {
        // I want this  value to be 15 if cardL1Id is full, otherwise I want it to be cardL1.size
        int addItemSize = (cardL1Collection.size() > 15) ? 15 : cardL1Collection.size();
        reviewPool.addAll(cardL1Collection.subList(0, addItemSize));
      }

      if(cardL2Collection.isEmpty() && cardL2Id.size() > 10) {
        Toast.makeText(getActivity(), "Please promote cards to level 2.", Toast.LENGTH_SHORT).show();
      } else {
        int addItemSize = (cardL2Collection.size() > 10) ? 10 : cardL2Collection.size();
        reviewPool.addAll(cardL2Collection.subList(0, addItemSize));
      }

        randomNumberGenerator();
        randomCard();

    }


  }

  public Card randomNumberGenerator() {
    int max = reviewPool.size();
    int min = 0;
    int range = max - min;

    int currentPoolRandomId;

    currentPoolRandomId = (int) (Math.random() * range) + min;
    currentRandomCard = reviewPool.get(currentPoolRandomId);
    // generate random number from 0 to singleCard.size()
    return currentRandomCard;
  }

  public final void randomCard() {
    cardReview = rootView.findViewById(R.id.review_random_card);
    /** checks and retrieve value of the current random card*/
    if (cardL1Collection.isEmpty() && cardL2Collection.isEmpty()) {
      Toast.makeText(getActivity(), "Please create cards.", Toast.LENGTH_SHORT).show();
    } else {
      currentCardFront = currentRandomCard.getFront();
      currentCardBack = currentRandomCard.getBack();

      int width = getContext().getResources().getDisplayMetrics().widthPixels;

      Picasso.with(getContext())
          .load(currentRandomCard.getImageOne())
          .centerCrop().resize(width / 2, width / 2)
          .placeholder(R.drawable.loading)
          .into(image_one);

      Picasso.with(getContext())
          .load(currentRandomCard.getImageTwo())
          .centerCrop().resize(width / 2, width / 2)
          .placeholder(R.drawable.loading)
          .into(image_two);

      Picasso.with(getContext())
          .load(currentRandomCard.getImageThree())
          .centerCrop().resize(width / 2, width / 2)
          .placeholder(R.drawable.loading)
          .into(image_three);

      Picasso.with(getContext())
          .load(currentRandomCard.getImageFour())
          .centerCrop().resize(width / 2, width / 2)
          .placeholder(R.drawable.loading)
          .into(image_four);

      cardReview.setText(currentCardFront);
    }
  }

  public final void cardCheck() {
    cardCheck = rootView.findViewById(R.id.check_random_card);
    cardCheck.setText(currentCardBack);

  }

}

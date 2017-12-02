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

/** A fragment that handles the card review functionality of this app.
 *  Cards are reviewed using a streamlined version of Leitner's Game Schedule.
 *  Instead of users having to review card levels sequentially and at a specific date,
 *  Users are able to review cards from every level. The card review pool is
 *  populated with up to 15 level 1 cards and 10 level 2 cards.
 *
 * */
public class ReviewCardFragment extends Fragment implements OnClickListener {

  /** Constant for deck_id */
  private static final String DECK_ID = "deck_id";

  /** Stores the value of my helper. */
  private OrmHelper helper;
  /** Stores information about the current deck. */
  private Deck deck;
  /** Stores the view for this fragment. */
  private View rootView;
//  /** Contains all of level 1 cards. */
//  private List<Card> cardL1Collection;
//  /** Contains all of level 2 cards. */
//  private List<Card> cardL2Collection;
//  /** Contains all of level 2 cards. */
//  private List<Card> cardL3Collection;


  // push reviewPoolSize amount of cards here - make sure cards has "review pool member" status
  /** Contains all the cards to be reviewed */
  private List<Card> reviewPool = new ArrayList<>();

  /** Initially contains 1/3 of reviewPoolSize. */
  private List<Card> cardPoolOne = new ArrayList<>();
  /** Initially contains 1/3 of reviewPoolSize. */
  private List<Card> cardPoolTwo = new ArrayList<>();

  /** Initially contains 1/3 of reviewPoolSize. */
  private List<Card> cardPoolThree = new ArrayList<>();

  // push cards that are members of cardPoolThree - make sure cards has "graduated" status
  /** Will contain cards that have reached the maximum level. */
  private ArrayList<String> graduatedCards = new ArrayList<>();

  /** Front text value of card being reviewed. */
  private TextView cardReview;
  /** Back text value of card being reviewed. */
  private TextView cardCheck;

  // TODO change to dynamically created image views.
  /** ImageView for image one. */
  private ImageView image_one;
  /** ImageView for image one. */
  private ImageView image_two;
  /** ImageView for image one. */
  private ImageView image_three;
  /** ImageView for image one. */
  private ImageView image_four;

  // if currentRandomNumber == previousRandomNumber - generate another number.
  /** Current, Random card being reviewed. */
  private Card currentRandomCard;

  // this tracks what session they're in,
  // changed only when all cards in the pool have been reviewed

  // This needs to be persisted onto the database
  private int sessionNumber = 0;
  // this tracks user specified reviewPoolSize
  // This needs to be persisted too.
  private int reviewPoolSize = 6;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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
        switch(sessionNumber) {
          case 0:
            if (cardPoolOne.isEmpty()) {
              Toast.makeText(getActivity(), "Great work! Reviewed all cards.", Toast.LENGTH_SHORT).show();
            } else {
              randomNumberGenerator();
              randomCard();
            }
        }


        break;
      case R.id.button_check:
        cardCheck();
        switch(sessionNumber) {
          case 0:
            if (currentRandomCard.isReviewed() == false) {
              currentRandomCard.setReviewed(true);
              cardPoolTwo.add(currentRandomCard);
              cardPoolOne.remove(currentRandomCard);
            }

        }
//        getCardPool(reviewPool.size());
        break;
      case R.id.button_level_up:
//        getCardPool(reviewPool.size());
    try {
      // TODO downgrade the level of the card being reviewed
      Dao<Card, Integer> cardDao = helper.getCardDao();
      UpdateBuilder<Card, Integer> updateBuilder = cardDao.updateBuilder();
      updateBuilder.where().eq("CARD_ID", currentRandomCard.getId());
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

    /** Retrieves cards that has reviewed value false and add them onto the review pool */
    try {
      Dao<Deck, Integer> deckDao = helper.getDeckDao();
      Dao<Card, Integer> cardDao = helper.getCardDao();
      deck = deckDao.queryForId(getArguments().getInt(DECK_ID));
      QueryBuilder<Card, Integer> queryBuilder = cardDao.queryBuilder();
      Where<Card, Integer> where = queryBuilder.where();
      where.eq("REVIEWED", false);
      where.and();
      where.eq("DECK_ID", deck.getId());
      PreparedQuery<Card> preparedQueryL1 = queryBuilder.prepare();
      reviewPool = cardDao.query(preparedQueryL1);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

//    /** Retrieves level 2 cards from the database. */
//    try {
//      Dao<Deck, Integer> deckDao = helper.getDeckDao();
//      Dao<Card, Integer> cardDao = helper.getCardDao();
//      deck = deckDao.queryForId(getArguments().getInt(DECK_ID));
//      QueryBuilder<Card, Integer> queryBuilder = cardDao.queryBuilder();
//      Where<Card, Integer> where = queryBuilder.where();
//      where.eq("TYPE", 2);
//      where.and();
//      where.eq("DECK_ID", deck.getId());
//      PreparedQuery<Card> preparedQuery = queryBuilder.prepare();
//      reviewPool = cardDao.query(preparedQuery);
//    } catch (SQLException e) {
//      throw new RuntimeException(e);
//    }

//    /** Retrieves level 2 cards from the database. */
//    try {
//      Dao<Deck, Integer> deckDao = helper.getDeckDao();
//      Dao<Card, Integer> cardDao = helper.getCardDao();
//      deck = deckDao.queryForId(getArguments().getInt(DECK_ID));
//      QueryBuilder<Card, Integer> queryBuilder = cardDao.queryBuilder();
//      Where<Card, Integer> where = queryBuilder.where();
//      where.eq("TYPE", 3);
//      where.and();
//      where.eq("DECK_ID", deck.getId());
//      PreparedQuery<Card> preparedQuery = queryBuilder.prepare();
//      reviewPool = cardDao.query(preparedQuery);
//    } catch (SQLException e) {
//      throw new RuntimeException(e);
//    }

    /** Generates the review pool collection and ensures that a maximum of 25 cards can
     *  be reviewed at any given time.
     * */
    // make sure that only cards with the review status is added
    if (reviewPool.size() < reviewPoolSize/3) {
      Toast.makeText(getActivity(), "Please add more cards.", Toast.LENGTH_SHORT).show();
    } else {

      switch (sessionNumber) {
        case 0:
          for (int i = 0; i < reviewPoolSize/3; i++) {
            cardPoolOne.add(reviewPool.get(i));
          }
          randomNumberGenerator();
          randomCard();

      }
    }

    // gets the cards that are currently being reviewed -- might not need this.
    getCardPool(reviewPool.size());
  }

  /** Provides a random number to be used as a way to determine which card
   *  is going to be reviewed.
   * */
  public Card randomNumberGenerator() {
    int max = reviewPool.size();
    int min = 0;
    int range = max - min;

    int currentPoolRandomId;

    currentPoolRandomId = (int) (Math.random() * range) + min;
    currentRandomCard = cardPoolOne.get(currentPoolRandomId);
    return currentRandomCard;
  }

  /** Retrieves value of the current random card to be used for
   *  studying.
   *  */
  public final void randomCard() {
    cardReview = rootView.findViewById(R.id.review_random_card);
    if (reviewPool.isEmpty()) {
      Toast.makeText(getActivity(), "Please create cards.", Toast.LENGTH_SHORT).show();
    } else {
      cardReview.setText(currentRandomCard.getFront());
    }
  }

  /** Gives a user the ability to check if they answered a given card correctly.
   *  Shows images that the user picked in order to help with retention.
   * */
  public final void cardCheck() {
    cardCheck = rootView.findViewById(R.id.check_random_card);
    cardCheck.setText(currentRandomCard.getBack());

    int width = getContext().getResources().getDisplayMetrics().widthPixels;

    Picasso.with(getContext())
        .load(currentRandomCard.getImageOne())
        .centerCrop().resize(width / 2, width / 3)
        .placeholder(R.drawable.loading)
        .into(image_one);

    Picasso.with(getContext())
        .load(currentRandomCard.getImageTwo())
        .centerCrop().resize(width / 2, width / 3)
        .placeholder(R.drawable.loading)
        .into(image_two);

    Picasso.with(getContext())
        .load(currentRandomCard.getImageThree())
        .centerCrop().resize(width / 2, width / 3)
        .placeholder(R.drawable.loading)
        .into(image_three);

    Picasso.with(getContext())
        .load(currentRandomCard.getImageFour())
        .centerCrop().resize(width / 2, width / 3)
        .placeholder(R.drawable.loading)
        .into(image_four);


  }

  /** Updates the value of the cardPool */
  public void getCardPool (int cardPool) {
    try {
      Dao<Deck, Integer> deckDao = helper.getDeckDao();
      UpdateBuilder<Deck, Integer> updateBuilder = deckDao.updateBuilder();
      Where<Deck, Integer> where = updateBuilder.where();
      where.eq("DECK", deck.getName());
      updateBuilder.updateColumnValue("CARD_POOL", cardPool);
      updateBuilder.update();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

}

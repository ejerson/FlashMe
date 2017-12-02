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

/**
 * A fragment that handles the card review functionality of this app. Cards are reviewed using a
 * streamlined version of Leitner's Game Schedule. Instead of users having to review card levels
 * sequentially and at a specific date, Users are able to review cards from every level. The card
 * review pool is populated with up to 15 level 1 cards and 10 level 2 cards.
 */
public class ReviewCardFragment extends Fragment implements OnClickListener {

  /**
   * Constant for deck_id
   */
  private static final String DECK_ID = "deck_id";

  /**
   * Stores the value of my helper.
   */
  private OrmHelper helper;
  /**
   * Stores information about the current deck.
   */
  private Deck deck;
  /**
   * Stores the view for this fragment.
   */
  private View rootView;
//  /** Contains all of level 1 cards. */
//  private List<Card> cardL1Collection;
//  /** Contains all of level 2 cards. */
//  private List<Card> cardL2Collection;
//  /** Contains all of level 2 cards. */
//  private List<Card> cardL3Collection;

  // push reviewPoolSize amount of cards here - make sure cards has "review pool member" status
  /**
   * Contains all the cards to be reviewed
   */
  private List<Card> reviewPool = new ArrayList<>();

  /**
   * cardPoolOne to cardPoolThree keeps track of what's inside each card pool
   */
  // need to persist into the database?
  /**
   * Initially contains 1/3 of reviewPoolSize.
   */
  private List<Card> cardPoolOne = new ArrayList<>();
  // need to persist into the database?
  /**
   * Initially contains 1/3 of reviewPoolSize.
   */
  private List<Card> cardPoolTwo = new ArrayList<>();
// need to persist into the database?
  /**
   * Initially contains 1/3 of reviewPoolSize.
   */
  private List<Card> cardPoolThree = new ArrayList<>();

  // push cards that are members of cardPoolThree - make sure cards has "graduated" status
  /**
   * Will contain cards that have reached the maximum level.
   */
  private ArrayList<String> graduatedCards = new ArrayList<>();

  /**
   * Front text value of card being reviewed.
   */
  private TextView cardReview;
  /**
   * Back text value of card being reviewed.
   */
  private TextView cardCheck;

  // TODO change to dynamically created image views.
  /**
   * ImageView for image one.
   */
  private ImageView image_one;
  /**
   * ImageView for image one.
   */
  private ImageView image_two;
  /**
   * ImageView for image one.
   */
  private ImageView image_three;
  /**
   * ImageView for image one.
   */
  private ImageView image_four;

  // if currentRandomNumber == previousRandomNumber - generate another number.
  /**
   * Current, Random card being reviewed.
   */
  private Card currentRandomCard;

  // this tracks what session they're in,
  // changed only when all cards in the pool have been reviewed

  // This needs to be persisted onto the database
  private int sessionNumber = 1;
  // this tracks user specified reviewPoolSize
  // This needs to be persisted too.
  private long reviewPoolSize = 6;

//  sessionOnePool keeps pools all card inside one array to be used in each session
  private List <Card> sessionOnePool = new ArrayList<>();
  private List <Card> sessionTwoPool = new ArrayList<>();
  private List <Card> sessionThreePool = new ArrayList<>();

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

    switch (view.getId()) {
      case R.id.button_review:
        switch (sessionNumber) {
          case 0:
            if (sessionOnePool.isEmpty()) {
              Toast.makeText(getActivity(), "Great work! Reviewed all cards.", Toast.LENGTH_SHORT)
                  .show();
            } else {
              randomNumberGenerator();
              randomCard();
            }
            break;
          case 1:
            if (sessionTwoPool.isEmpty()) {
              Toast.makeText(getActivity(), "Great work! Reviewed all cards.", Toast.LENGTH_SHORT).show();
            } else {
              randomNumberGenerator();
              randomCard();
            }
            break;
        }

        break;
      case R.id.button_check:
        cardCheck();
        switch (sessionNumber) {
          case 0:
            if (currentRandomCard.isReviewed() == false) {
              currentRandomCard.setReviewed(true);
            }
            break;
          case 1:
            if (currentRandomCard.isReviewed() == false) {
              currentRandomCard.setReviewed(true);
            }
            break;
        }
        break;

      case R.id.button_level_up:
        switch (sessionNumber) {
          case 0:
            if (currentRandomCard.isReviewed() == true) {
              sessionTwoPool.add(currentRandomCard);
              sessionOnePool.remove(currentRandomCard);
            }
            break;
          case 1:
            if (currentRandomCard.isReviewed() == true) {
              sessionThreePool.add(currentRandomCard);
              sessionTwoPool.remove(currentRandomCard);
            }
            // push cardPool1 to cardPool2
//            try {
//              Dao<Card, Integer> cardDao = helper.getCardDao();
//              UpdateBuilder<Card, Integer> updateBuilder = cardDao.updateBuilder();
//              updateBuilder.where().eq("CARD_ID", currentRandomCard.getId());
//              updateBuilder.updateColumnValue("TYPE", 2);
//              updateBuilder.update();
//            } catch (SQLException e) {
//              throw new RuntimeException(e);
//            }
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
      queryBuilder.limit(reviewPoolSize);
      Where<Card, Integer> where = queryBuilder.where();
      where.eq("REVIEWED", false);
      where.and();
      where.eq("DECK_ID", deck.getId());
      where.and();
      where.eq("REVIEW_STATUS", "review pool member");
      PreparedQuery<Card> preparedQueryL1 = queryBuilder.prepare();
      reviewPool = cardDao.query(preparedQueryL1);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }


    /** Generates the review pool collection and ensures that a maximum of 25 cards can
     *  be reviewed at any given time.
     * */
    // make sure that only cards with the review status is added

    int reviewPoolAllocationSize = (int) reviewPoolSize / 3;

    if (reviewPool.size() < reviewPoolSize) {
      Toast.makeText(getActivity(), "Please add more cards.", Toast.LENGTH_SHORT).show();
    } else {

      // retrieve all cardPoolOne by getting the first 3rd of the review pool
      for (int i = 0; i < reviewPoolAllocationSize; i++) {
        cardPoolOne.add(reviewPool.get(i));
      }

      // populate cardPoolTwo by getting the second third of the review pool
      for (int i = 0; i < reviewPoolAllocationSize; i++) {
        cardPoolTwo.add(reviewPool.get(i + reviewPoolAllocationSize));
      }

      for (int i = 0; i < reviewPoolAllocationSize; i++) {
        cardPoolThree.add(reviewPool.get(i + (reviewPoolAllocationSize * 2)));
      }

      switch (sessionNumber) {
        case 0:
            sessionOnePool.addAll(cardPoolOne);
          break;
        case 1:
          sessionTwoPool.addAll(cardPoolOne);
          sessionTwoPool.addAll(cardPoolTwo);
          break;
      }

      randomNumberGenerator();
      randomCard();
    }

    // gets the cards that are currently being reviewed -- might not need this.
    getCardPool(reviewPool.size());
  }

  /**
   * Provides a random number to be used as a way to determine which card is going to be reviewed.
   */
  public Card randomNumberGenerator() {

    int max = 0;
    int min = 0;

    int currentPoolRandomId;

    switch(sessionNumber) {
      case 0:
        max = sessionOnePool.size();
        break;
      case 1:
        max = sessionTwoPool.size();
        break;
      default:
    }

    int range = max - min;
    currentPoolRandomId = (int) (Math.random() * range) + min;

    switch(sessionNumber) {
      case 0:
        currentRandomCard = sessionOnePool.get(currentPoolRandomId);
        break;
      case 1:
        currentRandomCard = sessionTwoPool.get(currentPoolRandomId);
    }

    return currentRandomCard;
  }

  /**
   * Retrieves value of the current random card to be used for studying.
   */
  public final void randomCard() {
    cardReview = rootView.findViewById(R.id.review_random_card);
    if (cardPoolOne.isEmpty()) {
      Toast.makeText(getActivity(), "Please create cards.", Toast.LENGTH_SHORT).show();
    } else {
      cardReview.setText(currentRandomCard.getFront());
    }
  }

  /**
   * Gives a user the ability to check if they answered a given card correctly. Shows images that
   * the user picked in order to help with retention.
   */
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

  /**
   * Updates the value of the cardPool
   */
  public void getCardPool(int cardPool) {
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

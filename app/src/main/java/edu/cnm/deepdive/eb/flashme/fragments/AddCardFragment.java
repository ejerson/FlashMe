package edu.cnm.deepdive.eb.flashme.fragments;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.j256.ormlite.dao.Dao;
import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.activities.ImageActivity;
import edu.cnm.deepdive.eb.flashme.entities.Card;
import edu.cnm.deepdive.eb.flashme.entities.Deck;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper.OrmInteraction;
import java.sql.SQLException;
import java.util.List;

/**
 * A fragment that handles the creation of cards, and facilitate smooth transition onto the
 * ImageActivity to allow users to add Images onto the database.
 *
 * It is within this fragment that the Card entity is used to create, and populate the database with
 * card values that will later be used by multiple components (fragments and activities) to do their
 * respective functions.
 */
public class AddCardFragment extends DialogFragment {

  /**
   * Constant value of DECK_ID.
   */

  public static final String DECK_ID_KEY = "deck_id";
  /**
   * Stores value of deckDao.
   */
  private Dao<Deck, Integer> deckDao;
  /**
   * Stores value of deck.
   */
  private Deck deck;
  /**
   * Stores value of helper.
   */
  private OrmHelper helper;

  /**
   * Saves the content of the bundle received from DeckMemberFragment
   */
  private List<String> cardFrontCollection;

  /**
   * Access and Saves the value of user specified card front value.
   */
  public static String currentFront;
  /**
   * Access and Saves the value of user specified card back value.
   */
  public static String currentBack;


  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Builder builder = new Builder(getActivity());

    LayoutInflater inflater = getActivity().getLayoutInflater();

    final View inflatedView = inflater.inflate(R.layout.dialog_add_card, null);
    helper = ((OrmInteraction) getActivity()).getHelper();

    try {
      deckDao = helper.getDeckDao();
      deck = deckDao.queryForId(getArguments().getInt(DECK_ID_KEY));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    /**
     * Receives the bundle sent by DeckMemberFragment when a user adds a card.
     *  Used by this fragment to validate whether a card already exist or not.
     * */
    Bundle args = getArguments();
    if (args != null && args.containsKey("cardFrontCollection")) {
      cardFrontCollection = args.getStringArrayList("cardFrontCollection");
    }

    /** Retrieves and saves the user specified value of the card front in a variable. */
    final EditText frontView = inflatedView.findViewById(R.id.card_front);
    /** Retrieves and saves the user specified value of the card back in a variable. */
    final EditText backView = inflatedView.findViewById(R.id.card_back);

    builder.setView(inflatedView);
    builder.setPositiveButton(R.string.dialogue_ok, new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {

        currentFront = frontView.getText().toString();
        currentBack = backView.getText().toString();

        // STRETCH GOAL give users the ability to override a card if it already exists
        if (cardFrontCollection.size() == 0) {
          addCard(currentFront);
        } else {
            if (cardFrontCollection.contains(currentFront)) {
              Toast.makeText(getActivity(), "Card already exists.", Toast.LENGTH_SHORT).show();
            } else {
              addCard(currentFront);
          }
        }

        if (currentFront.equalsIgnoreCase("") || currentBack.equalsIgnoreCase("")) {
          Toast.makeText(getActivity(), "Enter card details.", Toast.LENGTH_SHORT).show();
        }
      }
    });

    builder.setNegativeButton(getString(R.string.dialogue_cancel), new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        AddCardFragment.this.getDialog().cancel();
      }
    });
    return builder.create();
  }

  /**
   * Adds a card to the database and starts the ImageActivity
   */
  private void addCard(String front) {
    Card card = new Card();
    card.setDeck(deck);
    card.setFront(front);
    card.setType(1);
    card.setBack(currentBack);
    card.setImageOne("first image");
    card.setImageTwo("second image");
    card.setImageThree("third image");
    card.setImageFour("forth image");
    startActivity(new Intent(getActivity(), ImageActivity.class));
    try {
      helper.getCardDao().create(card);
    } catch (SQLException e) {
      throw new RuntimeException();
    }
  }
}

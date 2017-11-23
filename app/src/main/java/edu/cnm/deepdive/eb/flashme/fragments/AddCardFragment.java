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
import edu.cnm.deepdive.eb.flashme.ImageActivity;
import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.entities.Card;
import edu.cnm.deepdive.eb.flashme.entities.Deck;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper.OrmInteraction;
import java.sql.SQLException;
import java.util.List;


public class AddCardFragment extends DialogFragment {

  public static final String DECK_ID_KEY = "deck_id";

  private Dao<Deck, Integer> deckDao;
  private Dao <Card, Integer> cardDao;
  private Deck deck;
    private Deck card;
  private OrmHelper helper;

  private List<String> cardFrontCollection;


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

    Bundle args = getArguments();
    if (args != null && args.containsKey("cardFrontCollection")) {
      cardFrontCollection = args.getStringArrayList("cardFrontCollection");
    }
    // declare them final to be able to access these inside my OnClickListener
    // this value will never change, it makes this variable immutable
    // this variable can't refer to another textView
    final EditText frontView = inflatedView.findViewById(R.id.card_front);
    final EditText backView = inflatedView.findViewById(R.id.card_back);

    builder.setView(inflatedView);
    builder.setPositiveButton(R.string.dialogue_ok, new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {

        String front = frontView.getText().toString();
        currentBack = backView.getText().toString();

        // STRETCH GOAL give users the ability to override a card if it already exists
        if (cardFrontCollection.size() == 0) {
          addCard(front);
        } else {
          for (int j = 0; j < cardFrontCollection.size(); j++) {

            if (cardFrontCollection.get(j).contains(front)) {
              Toast.makeText(getActivity(), "Card already exists.", Toast.LENGTH_LONG).show();
            } else if (front.equalsIgnoreCase("") || currentBack.equalsIgnoreCase("")) {
              Toast.makeText(getActivity(), "Enter card details.", Toast.LENGTH_SHORT).show();
            } else {
              addCard(front);
              break;
            }
          }
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

  /** Adds a card to the database and starts the ImageActivity */
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

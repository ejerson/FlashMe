package edu.cnm.deepdive.eb.flashme.fragments;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.j256.ormlite.dao.Dao;
import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.entities.Card;
import edu.cnm.deepdive.eb.flashme.entities.Deck;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper.OrmInteraction;
import java.sql.SQLException;


public class AddCardFragment extends DialogFragment {
//  implements OrmHelper.OrmInteraction {

  public static final String DECK_ID_KEY = "deck_id";

  private Dao<Deck, Integer> deckDao;
  private Dao <Card, Integer> cardDao;
  private Deck deck;
  private Deck card;
  private OrmHelper helper;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Builder builder = new Builder(getActivity());

    LayoutInflater inflater = getActivity().getLayoutInflater();

    View inflatedView = inflater.inflate(R.layout.dialog_add_card, null);
    helper = ((OrmInteraction) getActivity()).getHelper();
    try {
      deckDao = helper.getDeckDao();
      deck = deckDao.queryForId(getArguments().getInt(DECK_ID_KEY));
    } catch (SQLException e) {
      throw new RuntimeException(e);
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

        // TODO validate (decks should do not contain cards with the same name)
        String front = frontView.getText().toString();
        String back = backView.getText().toString();

        if (front.equalsIgnoreCase("") || back.equalsIgnoreCase("")) {
          Toast.makeText(getActivity(), "Enter Card Details", Toast.LENGTH_SHORT).show();
        } else {

          Card card = new Card();
          card.setDeck(deck);
          card.setFront(front);
          card.setType(1);
          card.setBack(back);
          try {
            helper.getCardDao().create(card);

          } catch (SQLException e) {
            throw new RuntimeException();
          }

          ChooseImageFragment fragment = new ChooseImageFragment();
          Bundle args = new Bundle();
          args.putInt(DeckMemberFragment.DECK_ID,
              getActivity().getIntent().getIntExtra(DeckMemberFragment.DECK_ID, 0));
          fragment.setArguments(args); // bundle
          getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

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
}

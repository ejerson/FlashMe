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
//
//  FragmentManager manager = getActivity().getSupportFragmentManager();
//  Fragment fragment = manager.findFragmentById(R.id.fragment_container);


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

    final EditText contentView = inflatedView.findViewById(R.id.new_card);

    builder.setView(inflatedView);
    builder.setPositiveButton(R.string.dialogue_ok, new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {

        // TODO validate (make decks do not contain blank card or cards with the same name
        String name = contentView.getText().toString();
        Card card = new Card();
        card.setDeck(deck);
        card.setName(name);
        try {

//          QueryBuilder<Card, Integer> cardQb = cardDao.queryBuilder();
//          cardQb.where().eq("FRONT", card.getName());
//          PreparedQuery<Card> preparedQuery = cardQb.prepare();
//          List<Card> singleCard = cardDao.query(preparedQuery);
//          if (singleCard.equals(card.getName())) {
//            Toast.makeText(getContext(), "exist", Toast.LENGTH_SHORT);
//          } else {

            helper.getCardDao().create(card);
//          }

        } catch (SQLException e) {
          throw new RuntimeException();
        }

//        startActivity(new Intent(getActivity(), CardActivity.class));

        ChooseImageFragment fragment = new ChooseImageFragment();
        Bundle args = new Bundle();
        args.putInt(DeckMemberFragment.DECK_ID,
            getActivity().getIntent().getIntExtra(DeckMemberFragment.DECK_ID, 0));
        fragment.setArguments(args); // bundle
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

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

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
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.entities.Card;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper.OrmInteraction;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class EditCardFragment extends DialogFragment {
  private OrmHelper helper;
  private ArrayList<String> stringCollection;
  private Card cards;
  private String content;


  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Builder builder = new Builder(getActivity());

    Bundle bundle = this.getArguments();
    LayoutInflater inflater = getActivity().getLayoutInflater();

    View inflatedView = inflater.inflate(R.layout.dialog_add_card, null);
    helper = ((OrmInteraction) getActivity()).getHelper();

    Bundle args = getArguments();
    if (args  != null && args.containsKey("stringCollection")) {
      stringCollection = args.getStringArrayList("stringCollection");
    }


    final EditText frontView = inflatedView.findViewById(R.id.card_front);
    final EditText backView = inflatedView.findViewById(R.id.card_back);

//    for(int i = 0; i < stringCollection.size(); i++) {
        try {
          Dao<Card, Integer> cardDao = helper.getCardDao();
          QueryBuilder<Card, Integer> cardBuilder = cardDao.queryBuilder();
          cardBuilder.where().eq("FRONT", stringCollection.get(0));
          List<Card> cards = cardDao.query(cardBuilder.prepare());
          frontView.setText(cards.get(0).getFront());
          backView.setText(cards.get(0).getBack());

          this.content = backView.getText().toString();

        } catch (SQLException e) {
          e.printStackTrace();
        }
    
    // declare them final to be able to access these inside my OnClickListener
    // this value will never change, it makes this variable immutable
    // this variable can't refer to another textView

    builder.setView(inflatedView);
    builder.setPositiveButton(R.string.dialogue_ok, new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        // A user chooses what card he/she wants to update
        // Then a EditCardFragment will appear, which will contain
        // the first value of front, back, and image
        // of the first card on a list of cards

          try {
            Dao<Card, Integer> cardDao = helper.getCardDao();
            UpdateBuilder<Card, Integer> updateBuilder = cardDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("FRONT", stringCollection.get(0));
            // update the value of your field(s)
            updateBuilder.updateColumnValue("FRONT", frontView.getText().toString());
//          updateBuilder.updateColumnValue("BACK", backView.getText().toString());
            updateBuilder.update();
          } catch (SQLException e) {
            e.printStackTrace();
          }

          try {
            Dao<Card, Integer> cardDao = helper.getCardDao();
            UpdateBuilder<Card, Integer> updateBuilder = cardDao.updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("BACK", content);
            // update the value of your field(s)
            updateBuilder.updateColumnValue("BACK", backView.getText().toString());
            updateBuilder.update();
          } catch (SQLException e) {
            e.printStackTrace();
          }

//        ((DeckMemberFragment)getFragmentManager().findFragmentById(R.id.deck_detail_container)).queryForCards();

      }
    });

    builder.setNegativeButton(getString(R.string.dialogue_cancel), new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        EditCardFragment.this.getDialog().cancel();
      }
    });
    return builder.create();
  }
}

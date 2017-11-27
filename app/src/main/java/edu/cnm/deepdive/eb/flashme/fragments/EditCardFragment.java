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
import edu.cnm.deepdive.eb.flashme.activities.DeckMemberActivity;
import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.entities.Card;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper.OrmInteraction;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** A fragment that handles card edit functionality. The database is queried
 *  for the value of the specific card being edited, and updates the value
 *  of that given card when the user confirms their desired changes are
 *  entered.
 * */
public class EditCardFragment extends DialogFragment {
  private OrmHelper helper;

  /** Stores the value of individual cards that have been selected for editing. */
  private ArrayList<String> stringCollection;


  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Builder builder = new Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();

    View inflatedView = inflater.inflate(R.layout.dialog_add_card, null);
    helper = ((OrmInteraction) getActivity()).getHelper();

    Bundle args = getArguments();
    if (args  != null && args.containsKey("stringCollection")) {
      stringCollection = args.getStringArrayList("stringCollection");
    }


    final EditText frontView = inflatedView.findViewById(R.id.card_front);
    final EditText backView = inflatedView.findViewById(R.id.card_back);

        /** Queries the database for the specific card chosen to be edited. */
        try {
          Dao<Card, Integer> cardDao = helper.getCardDao();
          QueryBuilder<Card, Integer> cardBuilder = cardDao.queryBuilder();
          cardBuilder.where().eq("FRONT", stringCollection.get(0));
          List<Card> cards = cardDao.query(cardBuilder.prepare());
          frontView.setText(cards.get(0).getFront());
          backView.setText(cards.get(0).getBack());
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }

    builder.setView(inflatedView);
    builder.setPositiveButton(R.string.dialogue_ok, new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {

        /** Updates the value of the card chosen for editing. */
          try {
            Dao<Card, Integer> cardDao = helper.getCardDao();
            UpdateBuilder<Card, Integer> updateBuilder = cardDao.updateBuilder();
            updateBuilder.where().eq("FRONT", stringCollection.get(0));
            updateBuilder.updateColumnValue("FRONT", frontView.getText().toString());
            updateBuilder.updateColumnValue("BACK", backView.getText().toString());
            updateBuilder.update();
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }

        /** Queries the database to ensure that the card list
         *  being displayed for the user is updated.
         * */
        ((DeckMemberActivity) getActivity()).queryForCards();
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

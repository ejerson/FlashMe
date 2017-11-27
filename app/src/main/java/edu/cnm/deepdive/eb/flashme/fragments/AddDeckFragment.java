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
import edu.cnm.deepdive.eb.flashme.activities.DeckListActivity;
import edu.cnm.deepdive.eb.flashme.activities.DeckMemberActivity;
import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.entities.Deck;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import java.sql.SQLException;

/** A fragment that handles the deck creation process and calls the
 * refreshRecyclerView() to update the RecyclerView on deck addition.
 * This fragment is contained within {@link DeckMemberActivity}
 * */
public class AddDeckFragment extends DialogFragment {

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Builder builder = new Builder(getActivity());

    LayoutInflater inflater = getActivity().getLayoutInflater();

    View inflatedView = inflater.inflate(R.layout.dialog_add_content, null);

    final EditText contentView = inflatedView.findViewById(R.id.new_deck);


    builder.setView(inflatedView);
    builder.setPositiveButton(R.string.dialogue_ok, new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {

        OrmHelper helper = new OrmHelper(getContext());

        String name = contentView.getText().toString();
        Deck deck = new Deck();
        deck.setName(name);
        deck.setPool(0);
        try {
          helper.getDeckDao().create(deck);
        } catch (SQLException e) {
          throw new RuntimeException();
        }
        ((DeckListActivity) getActivity()).refreshRecyclerView();

      }
    });

    builder.setNegativeButton(getString(R.string.dialogue_cancel), new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        AddDeckFragment.this.getDialog().cancel();
      }
    });
    return builder.create();
  }
}

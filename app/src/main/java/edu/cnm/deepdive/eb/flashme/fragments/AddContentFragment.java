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
import edu.cnm.deepdive.eb.flashme.DeckListActivity;
import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.entities.Deck;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import java.sql.SQLException;


public class AddContentFragment extends DialogFragment {

  private OrmHelper helper = null;

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

        String content = contentView.getText().toString();
        Deck deck = new Deck();
        deck.setName(content);
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
        AddContentFragment.this.getDialog().cancel();
      }
    });
    return builder.create();
  }
}

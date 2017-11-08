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
import edu.cnm.deepdive.eb.flashme.entities.Card;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import java.sql.SQLException;


public class AddCardFragment extends DialogFragment {

  private OrmHelper helper = null;


  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Builder builder = new Builder(getActivity());

    LayoutInflater inflater = getActivity().getLayoutInflater();

    View inflatedView = inflater.inflate(R.layout.dialog_add_card, null);



    // declare them final to be able to access these inside my OnClickListener
    // this value will never change, it makes this variable immutable
    // this variable can't refer to another textView
//    final EditText contentView = inflatedView.findViewById(R.id.new_content);
//    final EditText idView = inflatedView.findViewById(R.id.new_id);
//    final EditText colorView = inflatedView.findViewById(R.id.new_color);

    final EditText contentView = inflatedView.findViewById(R.id.new_card);

    builder.setView(inflatedView);
    builder.setPositiveButton(R.string.dialogue_ok, new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
//        Toast toast = Toast.makeText(getActivity(), "yey", Toast.LENGTH_SHORT);
//        toast.show();

        OrmHelper helper = new OrmHelper(getContext());

        String content = contentView.getText().toString();
        Card card = new Card();
        card.setName(content);
        try {
          helper.getCardDao(Card.class).create(card);
        } catch (SQLException e) {
          throw new RuntimeException();
        }
        ((DeckListActivity) getActivity()).refreshRecyclerView();

        // TODO Query my card creation so I get any cards that has equivalent deck_name column
        // TODO Post my query so it is loaded inside DecKMemberFragment

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

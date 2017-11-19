package edu.cnm.deepdive.eb.flashme.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import edu.cnm.deepdive.eb.flashme.DeckListActivity;
import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.entities.Card;
import edu.cnm.deepdive.eb.flashme.entities.Deck;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a single Deck detail screen. This fragment is contained in a
 * {@link DeckListActivity}.
 */
public class DeckMemberFragment extends Fragment implements OnClickListener {

  /** The fragment argument representing the item ID that this fragment represents. */
  public static final String DECK_ID = "deck_id";
  public static final String DECK = "deck_name";

  private OrmHelper helper;
  private int deckId;
  private Deck deck;
  private Card card;
  private View rootView;
  List<Card> cards;
  private ListView cardList;
  private ArrayAdapter<Card> cardAdapter;
  private String currentItemText;
  private ArrayList<String> stringCollection = new ArrayList<>();

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon
   * screen orientation changes).
   */
  public DeckMemberFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments().containsKey(DECK_ID)) {
      deckId = getArguments().getInt(DECK_ID);
    } else {
      deckId = 0;
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.deck_detail, container, false);



    // TODO how to customize what gets displayed in my view
    cardList = rootView.findViewById(R.id.card_front);
//    MyCustomAdapter adapter = new MyCustomAdapter(cardList, this);
    cardAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice);
    cardList.setAdapter(cardAdapter);

    /** pools the front value of each individual checked item/s and
     * store them inside List<String> stringCollection */
    getItemTextValue();

    Button edit_card_button = rootView.findViewById(R.id.button_edit_card);
    edit_card_button.setOnClickListener(this);

    Button add_card_button = rootView.findViewById(R.id.button_add_card);
    add_card_button.setOnClickListener(this);

    Button review_card_button = rootView.findViewById(R.id.button_review_card);
    review_card_button.setOnClickListener(this);

    Button delete_card_button = rootView.findViewById(R.id.button_delete_card);
    delete_card_button.setOnClickListener(this);

    return rootView;
  }

  String getItemTextValue() {
    cardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // change the checkbox state
        CheckedTextView checkedTextView = ((CheckedTextView)view);
        checkedTextView.setChecked(!checkedTextView.isChecked());

        currentItemText = (String) checkedTextView.getText();

        if(checkedTextView.isChecked()) {
          stringCollection.add(currentItemText);
        } else {
          stringCollection.remove(currentItemText);
        }
      }
    });
    return currentItemText;
  }

  @Override
  public void onClick(View view) {

    switch (view.getId()) {
      case R.id.button_add_card:
        // FIXME is there a way for me to select a given card using a check
        AddCardFragment dialog = new AddCardFragment();
        Bundle args = new Bundle();
        args.putInt(AddCardFragment.DECK_ID_KEY, deck.getId());
        dialog.setArguments(args);
        dialog.show(getActivity().getSupportFragmentManager(), "AddCardFragment");
        break;
      case R.id.button_review_card:
        ReviewCardFragment review = new ReviewCardFragment();
        Bundle argsReview = new Bundle();
        argsReview.putInt(AddCardFragment.DECK_ID_KEY, deck.getId());
        review.setArguments(argsReview); // bundle
        getActivity().getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment_container, review).commit();
        break;
      case R.id.button_delete_card:
        try {
          Dao<Card, Integer> cardDao = helper.getCardDao();
          DeleteBuilder<Card, Integer> cardDeleteBuilder = cardDao.deleteBuilder();

          for(int i = 0; i < stringCollection.size(); i++) {
            cardDeleteBuilder.where().eq("FRONT", stringCollection.get(i));
            cardDeleteBuilder.delete();
          }

          cardList.invalidateViews();
          cardAdapter.notifyDataSetChanged();

        } catch (SQLException e) {
          throw new RuntimeException();
        }
        break;

      // STRETCH GOAL users can pick multiple cards to edit
      case R.id.button_edit_card:
       if(stringCollection.size() == 1) {
         EditCardFragment edit = new EditCardFragment();
         Bundle argsEdit = new Bundle();
         argsEdit.putStringArrayList("stringCollection", stringCollection);
         edit.setArguments(argsEdit); // bundle
         edit.show(getActivity().getSupportFragmentManager(), "EditCardFragment");
       } else {
         Toast.makeText(getActivity(), "Please select one card.", Toast.LENGTH_SHORT).show();
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

    if (deckId > 0) {
      try {
        Dao<Deck, Integer> deckDao = helper.getDeckDao();
        Dao<Card, Integer> cardDao = helper.getCardDao();
        deck = deckDao.queryForId(getArguments().getInt(DECK_ID));
        QueryBuilder<Card, Integer> builder = cardDao.queryBuilder();
        getActivity().setTitle(deck.getName());
        builder.where().eq("DECK_ID", deck.getId());
        cards = cardDao.query(builder.prepare());
        cardAdapter.addAll(cards);
        cardAdapter.notifyDataSetChanged();
//        rootView.forceLayout();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    } else {
      deck = null;
    }
  }

    @Override
    public String toString() {
      return super.toString();
    }

}

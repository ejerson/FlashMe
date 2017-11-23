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
public class DeckMemberFragment
    extends Fragment
    implements OnClickListener {

  /** The fragment argument representing the item ID that this fragment represents. */
  public static final String DECK_ID = "deck_id";
  public static final String DECK = "deck_name";

  private OrmHelper helper;
  private int deckId;
  private Deck deck;
//  private Card card;
  private View rootView;

  private List<Card> cards;
  private ListView cardList;
  private ArrayAdapter<Card> cardAdapter;
  private String currentItemText;
  private ArrayList<String> stringCollection = new ArrayList<>();

  /** Stores card front values to be used for validation inside AddCardFragment */
  private ArrayList<String> cardFrontCollection = new ArrayList<>();
  CheckedTextView checkedTextView;

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

  private void getItemTextValue() {
    cardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // change the checkbox state
        checkedTextView = ((CheckedTextView)view);
        checkedTextView.setChecked(!checkedTextView.isChecked());

        currentItemText = (String) checkedTextView.getText();

        if (checkedTextView.isChecked()) {
          stringCollection.add(currentItemText);
        } else {
          stringCollection.remove(currentItemText);
        }

      }
    });
//    return null;
  }

  @Override
  public void onClick(View view) {

    switch (view.getId()) {
      case R.id.button_add_card:
        AddCardFragment dialog = new AddCardFragment();
        Bundle args = new Bundle();
        args.putInt(AddCardFragment.DECK_ID_KEY, deck.getId());
        args.putStringArrayList("cardFrontCollection", cardFrontCollection);
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
            checkedTextView.setChecked(false);
          }

          queryForCards();
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
      queryForCards();
    } else {
      deck = null;
    }

    for (int i = 0; i < cards.size(); i ++) {
      cardFrontCollection.add(cards.get(i).getFront().toString());
    }

  }

  /** Queries the database for cards with a specific foreign key. */
  public void queryForCards() {
      try {
        Dao<Deck, Integer> deckDao = helper.getDeckDao();
        Dao<Card, Integer> cardDao = helper.getCardDao();
        deck = deckDao.queryForId(getArguments().getInt(DECK_ID));
        QueryBuilder<Card, Integer> builder = cardDao.queryBuilder();
        getActivity().setTitle(deck.getName());
        builder.where().eq("DECK_ID", deck.getId());
        cards = cardDao.query(builder.prepare());
        cardAdapter.clear();
        cardAdapter.addAll(cards);
        cardAdapter.notifyDataSetChanged();

      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
  }

  public List<Card> getCards() {
    return cards;
  }

  public void setCards(List<Card> cards) {
    this.cards = cards;
  }

  @Override
    public String toString() {
      return super.toString();
    }

}

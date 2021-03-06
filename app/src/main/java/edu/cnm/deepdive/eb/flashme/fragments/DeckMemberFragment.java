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
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.activities.DeckMemberActivity;
import edu.cnm.deepdive.eb.flashme.entities.Card;
import edu.cnm.deepdive.eb.flashme.entities.Deck;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a single Card List screen. This fragment is contained in the {@link
 * DeckMemberActivity}.
 */
public class DeckMemberFragment
    extends Fragment
    implements OnClickListener {

  /**
   * The fragment argument representing the deck ID that this fragment represents.
   */
  public static final String DECK_ID = "deck_id";
  /**
   * The fragment argument representing the deck NAME that this fragment represents.
   */
  public static final String DECK = "deck_name";

  /**
   * Stores the value of my helper to be used by the queries within this fragment.
   */
  private OrmHelper helper;
  /**
   * Stores the id of the current deck.
   */
  private int deckId;
  /**
   * Stores the current deck.
   */
  private Deck deck;
  /**
   * Stores the value of the rootView
   */
  private View rootView;

  /**
   * Contains Card objects that are retrieved from the database.
   */
  private List<Card> cards;
  /**
   * Utilized as a way to display each cards.
   */
  private ListView cardList;
  /**
   * The adapter for the ListView and List<Card>.
   */
  private ArrayAdapter<Card> cardAdapter;

  /**
   * The value of the currently checked card checkbox.
   */
  private String currentItemText;
  /**
   * Collection of currentItemText that is used to determine which cards to be deleted.
   */
  private ArrayList<String> stringCollection = new ArrayList<>();

  /**
   * Stores card values to be used for validation inside AddCardFragment
   */
  private ArrayList<Card> cardFrontCollection = new ArrayList<>();
  /**
   * Flag that shows/stores whether a card checkbox is checked or not.
   */
  private CheckedTextView checkedTextView;
  /**
   * Stores value of edit button.
   */
  private Button edit_card_button;
  /**
   * Stores value of review button.
   */
  private Button review_card_button;
  /**
   * Stores value of add button.
   */
  private Button add_card_button;
  /**
   * Stores value of delete button.
   */
  private Button delete_card_button;

  public int pool;

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
    cardAdapter = new ArrayAdapter<>(getContext(),
        android.R.layout.simple_list_item_multiple_choice);
    cardList.setAdapter(cardAdapter);

    getItemTextValue();

    edit_card_button = rootView.findViewById(R.id.button_edit_card);
    edit_card_button.setOnClickListener(this);

    add_card_button = rootView.findViewById(R.id.button_add_card);
    add_card_button.setOnClickListener(this);

    review_card_button = rootView.findViewById(R.id.button_review_card);
    review_card_button.setOnClickListener(this);

    delete_card_button = rootView.findViewById(R.id.button_delete_card);
    delete_card_button.setOnClickListener(this);

    return rootView;
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.button_add_card:
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
        // FIXME if two cards have the same name, they are both deleted even
      // if cards are in different deck
      case R.id.button_delete_card:
        if (stringCollection.isEmpty()) {
          Toast.makeText(getActivity(), getString(R.string.select_card), Toast.LENGTH_SHORT).show();
        } else {
          try {
            Dao<Card, Integer> cardDao = helper.getCardDao();
            DeleteBuilder<Card, Integer> cardDeleteBuilder = cardDao.deleteBuilder();
            for (int i = 0; i < stringCollection.size(); i++) {
              cardDeleteBuilder.where().eq("FRONT", stringCollection.get(i));
              cardDeleteBuilder.delete();
              checkedTextView.setChecked(false);
            }
            queryForCards();
            pool = getDeck().getPool() - stringCollection.size();
            getCardPool(pool);

            if (cardAdapter.isEmpty()) {
              delete_card_button.setEnabled(false);
              review_card_button.setEnabled(false);
              edit_card_button.setEnabled(false);
            }
            cardList.invalidateViews();
            cardAdapter.notifyDataSetChanged();

          } catch (SQLException e) {
            throw new RuntimeException();
          }
        }
        break;
      case R.id.button_edit_card:
          if (stringCollection.size() == 1) {
            EditCardFragment edit = new EditCardFragment();
            Bundle argsEdit = new Bundle();
            argsEdit.putStringArrayList("stringCollection", stringCollection);
            edit.setArguments(argsEdit);
            edit.show(getActivity().getSupportFragmentManager(), "EditCardFragment");
          } else {
            Toast.makeText(getActivity(), getString(R.string.select_single_card), Toast.LENGTH_SHORT).show();

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

      for (int j = 0; j < cards.size(); j++) {
        cardFrontCollection.add(cards.get(j));
      }


    if (cards.size() == 0) {
      review_card_button.setEnabled(false);
      edit_card_button.setEnabled(false);
      delete_card_button.setEnabled(false);
    } else {
      review_card_button.setEnabled(true);
      edit_card_button.setEnabled(true);
      delete_card_button.setEnabled(true);
    }

  }

  /**
   * Queries the database for cards with a specific foreign key.
   */
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

  /**
   * pools the front value of each individual checked item/s and store them inside List<String>
   * stringCollection
   */
  private void getItemTextValue() {
    cardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // change the checkbox state
        checkedTextView = ((CheckedTextView) view);
        checkedTextView.setChecked(!checkedTextView.isChecked());

        currentItemText = (String) checkedTextView.getText();

        if (checkedTextView.isChecked()) {
          stringCollection.add(currentItemText);
        } else {
          stringCollection.remove(currentItemText);
        }

      }
    });
  }

  /**
   * Provides access to the current deck.
    * @return Returns a deck
   */
  public Deck getDeck() {
    return deck;
  }

  /** Updates the value of the cardPool */
  public void getCardPool (int cardPool) {
    try {
      Dao<Deck, Integer> deckDao = helper.getDeckDao();
      UpdateBuilder<Deck, Integer> updateBuilder = deckDao.updateBuilder();
      Where<Deck, Integer> where = updateBuilder.where();
      where.eq("DECK", getDeck().getName());
      updateBuilder.updateColumnValue("CARD_POOL", cardPool);
      updateBuilder.update();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}

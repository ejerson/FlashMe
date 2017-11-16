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
import com.j256.ormlite.stmt.QueryBuilder;
import edu.cnm.deepdive.eb.flashme.DeckListActivity;
import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.entities.Card;
import edu.cnm.deepdive.eb.flashme.entities.Deck;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import java.sql.SQLException;
import java.util.List;

/**
 * A fragment representing a single Deck detail screen. This fragment is contained in a
 * {@link DeckListActivity}.
 */
public class DeckMemberFragment extends Fragment implements OnClickListener {

  /** The fragment argument representing the item ID that this fragment represents. */
  public static final String DECK_ID = "deck_id";
//  public static final String CARD_ID = "card_id";

  private OrmHelper helper;
  private int deckId;
  private Deck deck;
  private Card card;
  private View rootView;
  private ListView cardList;
  private ArrayAdapter<Card> cardAdapter;

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

    // TODO Put this back
//    R.layout.single_card, R.id.card_front



    cardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // change the checkbox state
        CheckedTextView checkedTextView = ((CheckedTextView)view);
        checkedTextView.setChecked(!checkedTextView.isChecked());


        // TODO 1 : collect all of the text value,
        // TODO 2 : use an array of string to store them,
        // TODO 3 : only get the text if the value of is checked is true
        // TODO 4 : get my text, delete from database
        // TODO 5 : make sure to validate my card front so no similar entry is allowed

//        int checkedPos = Integer
//            .parseInt(String.valueOf(cardList.getAdapter().getItemId((int) id)));

        String yey = String.valueOf(checkedTextView.getText());

//        String yey = String.valueOf(checkedTextView.isChecked());
        Toast.makeText(getActivity(), yey, Toast.LENGTH_SHORT).show();

      }
    });



//    cardList.setOnItemClickListener(new OnItemClickListener() {
//      @Override
//      public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
////            String yey = getView().findViewById(R.id.card_front).toString();
//        String yey = cardList.getAdapter().getItem(1).toString();
////            String poop = String.valueOf(cardList.getAdapter().getItemId(0));
////            Toast.makeText(getActivity(), yey, Toast.LENGTH_LONG).show();
//
////            String hi = String.valueOf(checkedPositions.size());
//
//        Toast.makeText(getActivity(), yey, Toast.LENGTH_SHORT).show();
//
//              CheckBox cb = rootView.findViewById(R.id.checkbox);
//
//        if (cb.isChecked()) {
//          Toast.makeText(getActivity(), "Row " + yey + " is checked", Toast.LENGTH_SHORT).show();
//
////              checkedPositions.add(position); // add position of the row
//          // when checkbox is checked
//        } else {
////              checkedPositions.remove(position); // remove the position when the
//          // checkbox is unchecked
//          Toast.makeText(getActivity(), "Row " + yey + " is unchecked", Toast.LENGTH_SHORT).show();
//        }
//
//
//        int cntChoice = cardList.getCount();
//
//        String checked = "";
//
//        String unchecked = "";
//        SparseBooleanArray sparseBooleanArray = cardList.getCheckedItemPositions();
//
//        for(int i = 0; i < cntChoice; i++)
//        {
//
//          if(sparseBooleanArray.get(i) == true)
//          {
//            checked += cardList.getItemAtPosition(i).toString() + "\n";
//            Toast.makeText(getActivity(), checked, Toast.LENGTH_SHORT).show();
//          }
//
//          else  if(sparseBooleanArray.get(i) == false)
//          {
//            unchecked+= cardList.getItemAtPosition(i).toString() + "\n";
//            Toast.makeText(getActivity(), unchecked, Toast.LENGTH_SHORT).show();
//          }
//
//        }
//
//      }
//    });




//    Button delete_card_button = rootView.findViewById(R.id.button_delete_card);
//    delete_card_button.setOnClickListener(this);

    Button add_card_button = rootView.findViewById(R.id.button_add_card);
    add_card_button.setOnClickListener(this);

    Button review_card_button = rootView.findViewById(R.id.button_review_card);
    review_card_button.setOnClickListener(this);

    return rootView;
  }

  @Override
  public void onClick(View view) {

    switch (view.getId()) {


      // Adds a card to a specific deck id key (foreign key)
      case R.id.button_add_card:

        // FIXME is there a way for me to select a given card using a check
//         box then deleting them using a button.
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
//      case R.id.button_delete_card:
//        Toast.makeText(getActivity(), "yey", Toast.LENGTH_SHORT).show();

      case R.id.checkbox:
//        CheckBox cb = getView().findViewById(R.id.checkbox);
//
//        String cbChecked = String.valueOf(cb.isChecked());
//
//        Toast.makeText(getActivity(), cbChecked, Toast.LENGTH_SHORT).show();


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
        builder.where().eq("DECK_ID", deck.getId());
        List<Card> cards = cardDao.query(builder.prepare());
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

    public void clickToast(View view) {
      Toast.makeText(getActivity(), "Yey", Toast.LENGTH_SHORT).show();
    }

  }

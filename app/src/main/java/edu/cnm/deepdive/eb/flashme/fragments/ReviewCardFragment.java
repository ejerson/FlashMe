package edu.cnm.deepdive.eb.flashme.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.entities.Card;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class ReviewCardFragment extends Fragment implements OnClickListener {

  public static final String CARD_ID = "card_id";

  private OrmHelper helper;
  private Card card;
  private int cardId;

  private View rootView;
  private ListView singleCardCollection;
  private ArrayAdapter<Card> singleAdapter;

  public static ReviewCardFragment newInstance(UUID cardId) {
    Bundle args = new Bundle();
    args.putSerializable(CARD_ID, cardId);

    ReviewCardFragment fragment = new ReviewCardFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments().containsKey(CARD_ID)) {
      cardId = getArguments().getInt(CARD_ID);
    } else {
      cardId = 0;
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    rootView = inflater.inflate(R.layout.review_card_fragment, container, false);

    singleCardCollection = rootView.findViewById(R.id.single_card);
    singleAdapter = new ArrayAdapter<>(getContext(), R.layout.single_card);
    singleCardCollection.setAdapter(singleAdapter);

//    Button card_review_button = rootView.findViewById(R.id.button_review);
//    card_review_button.setOnClickListener(this);

    Button level_up_button = rootView.findViewById(R.id.button_level_up);
    level_up_button.setOnClickListener(this);

    return rootView;
  }

  @Override
  public void onClick(View view) {


    try {
      Dao<Card, Integer> cardDao = helper.getCardDao();
      // get our query builder from the DAO
      QueryBuilder<Card, Integer> queryBuilder =
          cardDao.queryBuilder();
// the 'password' field must be equal to "qwerty"
      queryBuilder.where().eq("CARD_ID", 1);
      PreparedQuery<Card> preparedQuery = queryBuilder.prepare();
// query for all accounts that have "qwerty" as a password
      List<Card> card = cardDao.query(preparedQuery);
      Toast.makeText(getActivity(), card.size(), Toast.LENGTH_SHORT).show();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

//    switch(view.getId()) {
//      case R.id.button_review:
//        onStart();
//        break;

//      case R.id.button_level_up:
    // FIXME my card variable is null, why?
    // I think I might need to find a way to
    // make sure that I am getting the currently viewed
    // card's id to be associated with my level_up button
//        if (cardId > 0) {

    // duplicate the parent state, which is the listView
//    try {
//      Dao<Card, Integer> cardDao = helper.getCardDao();
//      card = cardDao.queryForId(getArguments().getInt(CARD_ID));
//      UpdateBuilder<Card, Integer> updateBuilder = cardDao.updateBuilder();
//      // set the criteria like you would a QueryBuilder
//      updateBuilder.where().eq("CARD_ID", card.getId());
//      // update the value of your field(s)
//      updateBuilder.updateColumnValue("TYPE", 2);
//      updateBuilder.update();
//    } catch (SQLException e) {
//      throw new RuntimeException(e);
//    }
//        } else {
//          card = null;
//        }
//        break;
//        default:
//          break;
//    }

  }

  @Override
  public void onStart() {
    super.onStart();
    helper = ((OrmHelper.OrmInteraction) getActivity()).getHelper();

    try {
      Dao<Card, Integer> cardDao = helper.getCardDao();
// get our query builder from the DAO
      QueryBuilder<Card, Integer> queryBuilder =
          cardDao.queryBuilder();
// the 'password' field must be equal to "qwerty"
      queryBuilder.where().eq("TYPE", 1);
// prepare our sql statement
      PreparedQuery<Card> preparedQuery = queryBuilder.prepare();
// query for all accounts that have "qwerty" as a password
      List<Card> singleCardCollection = cardDao.query(preparedQuery);

      int max = singleCardCollection.size();
      int min = 0;
      int range = max - min;

      // generate random number from 0 to singleCard.size()
      int rand = (int) (Math.random() * range) + min;
      singleAdapter.add(singleCardCollection.get(rand));
      singleAdapter.notifyDataSetChanged();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }


}

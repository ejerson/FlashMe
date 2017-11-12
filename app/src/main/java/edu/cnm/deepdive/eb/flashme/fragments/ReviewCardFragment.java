package edu.cnm.deepdive.eb.flashme.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.entities.Card;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import java.sql.SQLException;
import java.util.List;

public class ReviewCardFragment extends Fragment {

  private OrmHelper helper = null;
  private Card card;

  private View rootView;
  private ListView singleCard;
  private ArrayAdapter<Card> singleAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    rootView = inflater.inflate(R.layout.review_card_fragment, container, false);

    singleCard = rootView.findViewById(R.id.single_card);
    singleAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
    singleCard.setAdapter(singleAdapter);

    return rootView;

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
      queryBuilder.where().eq("TYPE", "Level 1");
// prepare our sql statement
      PreparedQuery<Card> preparedQuery = queryBuilder.prepare();
// query for all accounts that have "qwerty" as a password
      List<Card> singleCard = cardDao.query(preparedQuery);

      int max = singleCard.size();
      int min = 0;
      int range = max - min;

      // generate random number from 0 to singleCard.size()
      int rand = (int) (Math.random() * range) + min;
      singleAdapter.add(singleCard.get(rand));
      singleAdapter.notifyDataSetChanged();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
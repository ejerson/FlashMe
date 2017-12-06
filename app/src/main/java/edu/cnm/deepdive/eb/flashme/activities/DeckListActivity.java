package edu.cnm.deepdive.eb.flashme.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.adapters.DeckListRecyclerViewAdapter;
import edu.cnm.deepdive.eb.flashme.enteties.Deck;
import edu.cnm.deepdive.eb.flashme.enteties.User;
import edu.cnm.deepdive.eb.flashme.fragments.AddDeckFragment;
import edu.cnm.deepdive.eb.flashme.fragments.DeckListFragment;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * An activity representing a list of Decks. This activity handles
 * the implementation of the recycler view and contains a method
 * that is connected to a button that allows a user to open the AddCardFragment.
 *
 */
public class DeckListActivity
    extends AppCompatActivity
    implements OrmHelper.OrmInteraction {

  private String userId;

  private  List<Deck> Decks = new ArrayList<>();
  private  List<User> Users = new ArrayList<>();
  private OrmHelper helper = null;

  DeckListRecyclerViewAdapter deckListRecyclerViewAdapter;

  FragmentManager manager = getSupportFragmentManager();
  DeckListFragment fragment = (DeckListFragment) manager.findFragmentById(R.id.main_fragment_container);

  RecyclerView recyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Creates an instance of the helper class, this line forces android to create my database if it doesn't exist already
    getHelper().getWritableDatabase().close();
    setContentView(R.layout.activity_main);

    new UsersHttpTask().execute();
//    new UsersHttpTask().execute();
//    new DeckHttpTask().execute();

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);



//    refreshRecyclerView();
  }

  /**
   * Updates the recyclerView when a new deck is added.
   */
  public void refreshRecyclerView() {
    recyclerView = (RecyclerView) findViewById(R.id.deck_list);
    assert recyclerView != null;
    setupRecyclerView(recyclerView);
  }

  public void setupRecyclerView(@NonNull RecyclerView recyclerView) {
    deckListRecyclerViewAdapter = new DeckListRecyclerViewAdapter(Decks);
    recyclerView.setAdapter(deckListRecyclerViewAdapter);
  }

  /**
   * Creates a new view adapter and passing it deck names.
//   * @param recyclerView passes RecyclerView
   */


  @Override
  public OrmHelper getHelper() {
    if (helper == null) {
      helper = OpenHelperManager.getHelper(this, OrmHelper.class);
    }
    return helper;
  }

  @Override
  public synchronized void releaseHelper() {
    if (helper != null) {
      OpenHelperManager.releaseHelper();
      helper = null;
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    getHelper();
//    refreshRecyclerView();
  }

  @Override
  protected void onStop() {
    super.onStop();
    releaseHelper();
  }


  /**
   * Provides user a way to enter a deck name.
   * @param view passes the container view
   */
  public void showAddDeckDialog(View view) {
    AddDeckFragment dialog = new AddDeckFragment();
    dialog.show(getSupportFragmentManager(), "AddDeckFragment");
  }

//  public void deleteDeck() {
//    try {
//      for (int i = 0; i < deckListRecyclerViewAdapter.getDeckDeletePool().size(); i++) {
//        Dao<Card, Integer> cardDao = helper.getCardDao();
//        DeleteBuilder<Card, Integer> cardDeleteBuilder = cardDao.deleteBuilder();
//        cardDeleteBuilder.where()
//            .eq("DECK_ID", deckListRecyclerViewAdapter.getDeckDeletePool().get(i));
//        cardDeleteBuilder.delete();
//        Dao<Deck, Integer> deckDao = helper.getDeckDao();
//        DeleteBuilder<Deck, Integer> deckDeleteBuilder = deckDao.deleteBuilder();
//        deckDeleteBuilder.where()
//            .eq("DECK_ID", deckListRecyclerViewAdapter.getDeckDeletePool().get(i));
//        deckDeleteBuilder.delete();
//      }
//      refreshRecyclerView();
//    } catch (SQLException e) {
//      throw new RuntimeException(e);
//    }
//  }

//  private class UserHttpTask extends AsyncTask<Void, Void, User> {
//
//    @Override
//    protected User doInBackground(Void... voids) {
//      try {
//        // final String url = "http://10.0.2.2:8080/classrooms/1"; Use 10.0.2.2 instead of local host
//        final String url = "http://10.0.2.2:8080/users/search/findByEmailAddress?emailaddress={email}";
//        //Retrieves JSON and transforms them into java objects
//        RestTemplate restTemplate = new RestTemplate();
//
//        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//        User user = restTemplate.getForObject(url, User.class, "edge@nmsu.edu");
//
//        return user;
//
//      } catch (Exception e) {
//        Log.e("MainActivity", e.getMessage(), e);
//      }
//      return null;
//    }
//
//    @Override
//    protected void onPostExecute(User user) {
////      TextView greetingId = (TextView) findViewById(R.id.greeting_id);
////      TextView greetingContent = (TextView) findViewById(R.id.greeting_content);
//      userId = String.valueOf(user.getId());
////      greetingId.setText(user.getEmailAddress());
////      greetingContent.setText(user.getUserName());
//
//    }
//  }

  public class UsersHttpTask extends AsyncTask<Void, Void, List<User>> {
    @Override
    protected List<User> doInBackground(Void... voids) {
      try {
        // final String url = "http://10.0.2.2:8080/classrooms/1"; Use 10.0.2.2 instead of local host
        final String url = "http://10.0.2.2:8080/users";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        User.UserCollection collection = restTemplate.getForObject(url, User.UserCollection.class);
        return collection.getEmbedded().getUsers();
      } catch (Exception e) {
        Log.e("MainActivity", e.getMessage(), e);
      }
      return null;
    }

    @Override
    protected void onPostExecute(List<User> users) {
//        userId = users.get(0).getLinks().getSelf();

    }
  }

//  public class DeckHttpTask extends AsyncTask<Void, Void, List<Deck>> {
//    @Override
//    protected List<Deck> doInBackground(Void... voids) {
//      try {
//        // final String url = "http://10.0.2.2:8080/classrooms/1"; Use 10.0.2.2 instead of local host
//        final String url = "http://10.0.2.2:8080/users/1/decks";
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//        Deck.DeckCollection collection = restTemplate.getForObject(url, Deck.DeckCollection.class);
//        return collection.getEmbedded().getDecks();
//      } catch (Exception e) {
//        Log.e("MainActivity", e.getMessage(), e);
//      }
//      return null;
//    }
//
//    @Override
//    protected void onPostExecute(List<Deck> decks) {
//        Decks.addAll(decks);
//      if (fragment == null) {
//        fragment = new DeckListFragment();
////      Bundle args = new Bundle();
////      args.putInt(DECK_ID, getIntent().getIntExtra(DECK_ID, 0));
////      fragment.setArguments(args);
//        manager.beginTransaction().replace(R.id.main_fragment_container, fragment).commit();
//      }
////      TextView greetingContent = (TextView) findViewById(R.id.greeting_content);
////      greetingContent.setText(decks.get(0).getDeckName());
//    }
//  }


  public DeckListRecyclerViewAdapter getDeckListRecyclerViewAdapter() {
    return deckListRecyclerViewAdapter;
  }

  public List<Deck> getDecks() {
    return Decks;
  }

}


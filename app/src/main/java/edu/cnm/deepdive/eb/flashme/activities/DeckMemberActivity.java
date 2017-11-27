package edu.cnm.deepdive.eb.flashme.activities;

import static edu.cnm.deepdive.eb.flashme.fragments.DeckMemberFragment.DECK_ID;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import edu.cnm.deepdive.eb.flashme.R;
import edu.cnm.deepdive.eb.flashme.fragments.DeckMemberFragment;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;

/**
 * An activity representing a list of cards within a specific deck.
 * This activity contains all of the fragments that handles the majority of the functionality
 * within this app.
 *
 * */
public class DeckMemberActivity
    extends AppCompatActivity
    implements OrmHelper.OrmInteraction {

  FragmentManager manager = getSupportFragmentManager();
  DeckMemberFragment fragment = (DeckMemberFragment) manager.findFragmentById(R.id.fragment_container);

  private OrmHelper helper = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_deck_detail);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Show the Up button in the action bar.
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    deckMemberFragment();
  }

  /** Creates a new instance of the DeckMemberFragment when the fragment value is null.
   *  Passes a bundle that contains DECK_ID with a default value of 0.
   *  Manages fragment replacement.
   *
   * */
  private void deckMemberFragment() {
    if (fragment == null) {
      fragment = new DeckMemberFragment();
      Bundle args = new Bundle();
      args.putInt(DECK_ID, getIntent().getIntExtra(DECK_ID, 0));
      fragment.setArguments(args);
      manager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      onBackPressed();
      // This ID represents the Home or Up button. In the case of this
      // activity, the Up button is shown. For
      // more details, see the Navigation pattern on Android Design:
      //
      // http://developer.android.com/design/patterns/navigation.html#up-vs-back
      //
//      navigateUpTo(new Intent(this, DeckListActivity.class));
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onStart() {
    super.onStart();
    getHelper();
  }

  @Override
  protected void onStop() {
    releaseHelper();
    super.onStop();
  }

  @Override
  public synchronized OrmHelper getHelper() {
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

  /** Queries the database for cards after deleting card/s or editing a card. */
  public void queryForCards() {
    fragment.queryForCards();
  }

}



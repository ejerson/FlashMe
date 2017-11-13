package edu.cnm.deepdive.eb.flashme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import edu.cnm.deepdive.eb.flashme.fragments.DeckMemberFragment;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;

/**
 * An activity representing a single Student detail screen. This activity is only used narrow width
 * devices. On tablet-size devices, item details are presented side-by-side with a list of items in
 * a {@link DeckListActivity}.
 */
public class DeckMemberActivity
    extends AppCompatActivity
    implements OrmHelper.OrmInteraction {

  FragmentManager manager = getSupportFragmentManager();
  Fragment fragment = manager.findFragmentById(R.id.fragment_container);

  private OrmHelper helper = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_deck_detail);
    Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
    setSupportActionBar(toolbar);



    // Show the Up button in the action bar.
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    // savedInstanceState is non-null when there is fragment state
    // saved from previous configurations of this activity
    // (e.g. when rotating the screen from portrait to landscape).
    // In this case, the fragment will automatically be re-added
    // to its container so we don't need to manually add it.
    // For more information, see the Fragments API guide at:
//    if (savedInstanceState == null) {
//      // Create the detail fragment and add it to the activity
//      // using a fragment transaction.
//      // This allows me to pass info to my fragments
//      Bundle arguments = new Bundle();
//      arguments.putInt(DeckMemberFragment.DECK_ID,
//          getIntent().getIntExtra(DeckMemberFragment.DECK_ID, 0));
//      DeckMemberFragment fragment = new DeckMemberFragment();
//      fragment.setArguments(arguments);
//      getSupportFragmentManager().beginTransaction()
//          .add(R.id.fragment_container, fragment)
//          .commit();
//    }

    deckMemberFragment();
  }

  private void deckMemberFragment() {
    if (fragment == null) {
      fragment = new DeckMemberFragment();
      Bundle args = new Bundle();
      args.putInt(DeckMemberFragment.DECK_ID, getIntent().getIntExtra(DeckMemberFragment.DECK_ID, 0));
      fragment.setArguments(args); // bundle
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

//  @Override
  public synchronized void releaseHelper() {
    if (helper != null) {
      OpenHelperManager.releaseHelper();
      helper = null;
    }
  }

}

package edu.cnm.deepdive.eb.flashme;

import static edu.cnm.deepdive.eb.flashme.fragments.DeckMemberFragment.DECK_ID;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.eb.flashme.fragments.AddCardFragment;
import edu.cnm.deepdive.eb.flashme.fragments.DeckMemberFragment;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import edu.cnm.deepdive.eb.flashme.model.GoogleItem;
import edu.cnm.deepdive.eb.flashme.services.MyService;
import edu.cnm.deepdive.eb.flashme.utils.NetworkHelper;

/**
 * An activity representing a single Student detail screen. This activity is only used narrow width
 * devices. On tablet-size devices, item details are presented side-by-side with a list of items in
 * a {@link DeckListActivity}.
 */
public class DeckMemberActivity
    extends AppCompatActivity
    implements OrmHelper.OrmInteraction {

  ImageView[] imageView;
  GridLayout imageLayout;

  static final String cx = String.valueOf(R.string.search_engine_id);
  static final String key = String.valueOf(R.string.google_key);


  private static final String JSON_URL =
      "https://www.googleapis.com/customsearch/v1?q=wells&cx=" + cx + "&fileType=png%2C+jpg&imgType=photo&searchType=image&fields=items%2Flink&key=" + key;

  private boolean networkOk;

  private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
//      String message =
//          intent.getStringExtra(MyService.MY_SERVICE_PAYLOAD);

      GoogleItem[] dataItems = (GoogleItem[]) intent
          .getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);

      imageLayout = (GridLayout) findViewById(R.id.image_gridlayout);

      imageView = new ImageView[dataItems.length];


      for (int i = 0; i < dataItems.length; i++) {
        imageView[i] = new ImageView(context);
        imageView[i].setPadding(5, 5, 5, 5);

        int width= context.getResources().getDisplayMetrics().widthPixels;

        Picasso.with(context)
            .load(dataItems[i].getLink())
            .centerCrop().resize(width/2,width/2)
            .into(imageView[i]);
        imageLayout.addView(imageView[i]);
      }
    }
  };

  FragmentManager manager = getSupportFragmentManager();
  Fragment fragment = manager.findFragmentById(R.id.fragment_container);

  private OrmHelper helper = null;
  private static String currentBack = AddCardFragment.currentBack;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_deck_detail);
    Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
    setSupportActionBar(toolbar);

    LocalBroadcastManager.getInstance(getApplicationContext())
        .registerReceiver(mBroadcastReceiver,
            new IntentFilter(MyService.MY_SERVICE_MESSAGE));

    networkOk = NetworkHelper.hasNetworkAccess(this);


    // Show the Up button in the action bar.
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    deckMemberFragment();
  }

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

//  @Override
  public synchronized void releaseHelper() {
    if (helper != null) {
      OpenHelperManager.releaseHelper();
      helper = null;
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    LocalBroadcastManager.getInstance(getApplicationContext())
        .unregisterReceiver(mBroadcastReceiver);
  }

  public void runClickHandler(View view) {

    if (networkOk) {
      Intent intent = new Intent(this, MyService.class);
      intent.setData(Uri.parse(JSON_URL));
      startService(intent);
    } else {
      Toast.makeText(this, "Network not available!", Toast.LENGTH_SHORT).show();
    }
  }

  public void clearClickHandler(View view) {
  }

}



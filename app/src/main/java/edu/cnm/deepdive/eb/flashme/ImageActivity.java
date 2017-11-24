package edu.cnm.deepdive.eb.flashme;

import static edu.cnm.deepdive.eb.flashme.fragments.AddCardFragment.currentBack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.eb.flashme.entities.Card;
import edu.cnm.deepdive.eb.flashme.helpers.OrmHelper;
import edu.cnm.deepdive.eb.flashme.model.GoogleItem;
import edu.cnm.deepdive.eb.flashme.services.MyService;
import edu.cnm.deepdive.eb.flashme.utils.NetworkHelper;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ImageActivity
    extends AppCompatActivity
    implements OrmHelper.OrmInteraction {

  private OrmHelper helper;

  /** Stores and concatenates Google Custom Search API endpoint */
  private StringBuilder urlBuilder;
  /** Flag that indicates whether device is connected to a network or not */
  private boolean networkOk;
  /** Stores the value of the current card back being added to the deck and database */
  private String cardBackKeyword = currentBack;
  /** Takes the value of cardBackKeyword and splits it into individual words which
   * are then stored inside an array */
  private String[] arr = cardBackKeyword.split(" ");
  /** Stores the dynamically created imageViews */
  private ImageView[] imageView;
  /** Stores the individual links retrieved from Google Custom Search API*/
  private GoogleItem[] dataItems;
  /** Contains the imageView that displays images */
  private GridLayout imageLayout;
  /** Stores the url of user selected images to be saved in the database */
  private ArrayList<String> currentLink = new ArrayList<>();
  /** Converts the text from arr into clickable spans */
  private ClickableSpan clickableSpan;
  /** Stores individual clickableSpan to be used by user as a way to search Google */
  private List<SpannableString> ss = new ArrayList<>();

  /**  Receives and handles broadcast intents sent by sendBroadcast(Intent).
   *
   * */
  private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

    /** Called when a BroadcastReceiver is receiving an Intent broadcast. */
    @Override
    public void onReceive(final Context context, Intent intent) {
      dataItems = (GoogleItem[]) intent
          .getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);
      handlesImages(context);
      }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    clickableSpan = new ClickableSpan() {

      @Override
      public void onClick(View textView) {
        String cx = getString(R.string.search_engine_id);
        String key = getString(R.string.google_key);

        String currentKeyword = ((TextView) textView).getText().toString();
        urlBuilder = new StringBuilder()
            .append("https://www.googleapis.com/customsearch/v1?q=")
            .append(currentKeyword)
            .append("&cx=")
            .append(cx)
            .append("&fileType=png%2C+jpg&imgType=photo&searchType=image&fields=items%2Flink&key=")
            .append(key);

        if (networkOk) {
          Intent intent = new Intent(ImageActivity.this, MyService.class);
          intent.setData(Uri.parse(urlBuilder.toString()));
          startService(intent);
        } else {
          Toast.makeText(ImageActivity.this, "Network not available!", Toast.LENGTH_SHORT).show();
        }
      }

    };

    LocalBroadcastManager.getInstance(getApplicationContext())
        .registerReceiver(broadcastReceiver,
            new IntentFilter(MyService.MY_SERVICE_MESSAGE));

    networkOk = NetworkHelper.hasNetworkAccess(this);
  }

  @Override
  protected void onStart() {
    super.onStart();


    for (int i = 0; i < arr.length; i++) {
      ss.add(new SpannableString(arr[i]));
    }
    for (int i = 0; i < ss.size(); i++) {
      ss.get(i).setSpan(clickableSpan, 0, ss.get(i).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    LinearLayout rl = (LinearLayout) findViewById(R.id.image_textlayout);

    TextView[] ssTv = new TextView[arr.length];

    for (int i = 0; i < arr.length; i++) {
      ssTv[i] = new TextView(ImageActivity.this);
      ssTv[i].setText(ss.get(i));
      ssTv[i].setTextSize(30);
      ssTv[i].setPadding(10, 5, 10, 10);
      ssTv[i].setMovementMethod(LinkMovementMethod.getInstance());
      ssTv[i].setHighlightColor(Color.TRANSPARENT);
      ssTv[i].setTextColor(Color.parseColor("#000000"));
      rl.addView(ssTv[i]);
    }
  }

  /** Receives and handles the data(image links) provided by the BroadcastReceiver class.
   *
   *  Creates ImageViews for individual image url.
   *
   *  Utilizes Square's Picasso Library to use individual image url
   *  provided by the BroadcastReceiver as image uri for each ImageView.
   *
   *  Handles the behaviour of each ImageView according to user input.
   * */

  private void handlesImages(final Context context) {
    imageLayout = (GridLayout) findViewById(R.id.image_gridlayout);
    imageView = new ImageView[dataItems.length];
    int i;

    imageLayout.removeAllViews();
    for ( i = 0; i < dataItems.length; i++) {
      imageView[i] = new ImageView(context);
      imageView[i].setPadding(5, 5, 5, 5);
      imageView[i].setId(i);
      imageView[i].setClickable(true);

      if (!currentLink.contains(dataItems[i].getLink())) {
        setLocked(imageView[i]);
      }

      imageView[i].setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          for (int i = 0; i < dataItems.length; i++) {
            if (imageView[i].getId() == view.getId()) {
              if (currentLink.contains(dataItems[i].getLink())) {
                currentLink.remove(dataItems[i].getLink());
                setLocked(imageView[i]);
              } else {
                if ( currentLink.size() < 4) {
                  setUnlocked(imageView[i]);
                  imageView[i].setSelected(true);
                  currentLink.add(dataItems[i].getLink());
                }
              }
            }
          }

          if (currentLink.size() > 4) {
            Toast.makeText(ImageActivity.this, "Only choose 4 images!", Toast.LENGTH_SHORT)
                .show();
          }
        }
      });

      int width = context.getResources().getDisplayMetrics().widthPixels;

      Picasso.with(context)
          .load(dataItems[i].getLink())
          .centerCrop().resize(width / 2, width / 2)
          .placeholder(R.drawable.loading)
          .into(imageView[i]);
      imageLayout.addView(imageView[i]);

    }
  }

  /** Converts images into a black and white configuration inside handlesImages */
  public static void  setLocked(ImageView v)
  {
    ColorMatrix matrix = new ColorMatrix();
    matrix.setSaturation(0);  //0 means grayscale
    ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
    v.setColorFilter(cf);
    v.setImageAlpha(128);   // 128 = 0.5
  }

  /** Reverts images to their original color configuration inside handlesImages */
  public static void  setUnlocked(ImageView v)
  {
    v.setColorFilter(null);
    v.setImageAlpha(255);
  }

  @Override
  public synchronized OrmHelper getHelper() {
    if (helper == null) {
      helper = OpenHelperManager.getHelper(this, OrmHelper.class);
    }
    return helper;
  }

  public void addCardImages(View view) {
    getHelper();
    if (currentLink.size() != 4) {
      Toast.makeText(this, "Choose 4 cards.", Toast.LENGTH_SHORT).show();
    } else {
      try {
        Dao<Card, Integer> cardDao = helper.getCardDao();
        UpdateBuilder<Card, Integer> updateBuilder = cardDao.updateBuilder();
        updateBuilder.where().eq("IMAGE_ONE", "first image");
        updateBuilder.where().eq("IMAGE_TWO", "second image");
        updateBuilder.where().eq("IMAGE_THREE", "third image");
        updateBuilder.where().eq("IMAGE_FOUR", "forth image");
        updateBuilder.updateColumnValue("IMAGE_ONE", currentLink.get(0));
        updateBuilder.updateColumnValue("IMAGE_TWO", currentLink.get(1));
        updateBuilder.updateColumnValue("IMAGE_THREE", currentLink.get(2));
        updateBuilder.updateColumnValue("IMAGE_FOUR", currentLink.get(3));
        updateBuilder.update();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }

    }

  @Override
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
        .unregisterReceiver(broadcastReceiver);
  }
}

package edu.cnm.deepdive.eb.flashme;

import static edu.cnm.deepdive.eb.flashme.fragments.AddCardFragment.currentBack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
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
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.eb.flashme.model.GoogleItem;
import edu.cnm.deepdive.eb.flashme.services.MyService;
import edu.cnm.deepdive.eb.flashme.utils.NetworkHelper;
import java.util.ArrayList;
import java.util.List;


public class ImageActivity extends AppCompatActivity {

  private StringBuilder stringBuilder;
  private boolean networkOk;

  private String cardBackKeyword = currentBack;
  private String[] arr = cardBackKeyword.split(" ");
  private String currentLink;

  List<SpannableString> ss = new ArrayList<>();;

  private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      GoogleItem[] dataItems = (GoogleItem[]) intent
          .getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);

      GridLayout imageLayout = (GridLayout) findViewById(R.id.image_gridlayout);

      ImageView[] imageView = new ImageView[dataItems.length];

        for (int i = 0; i < dataItems.length; i++) {
          imageView[i] = new ImageView(context);
          imageView[i].setPadding(5, 5, 5, 5);

          // TODO create on click listeners for my images to retrieve image link to
          // save

          int width = context.getResources().getDisplayMetrics().widthPixels;

          Picasso.with(context)
              .load(dataItems[i].getLink())
              .centerCrop().resize(width / 2, width / 2)
              .placeholder(R.drawable.loading)
              .into(imageView[i]);
          imageLayout.addView(imageView[i]);
        }
      }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image);

    Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
    setSupportActionBar(toolbar);

    LocalBroadcastManager.getInstance(getApplicationContext())
        .registerReceiver(broadcastReceiver,
            new IntentFilter(MyService.MY_SERVICE_MESSAGE));

    networkOk = NetworkHelper.hasNetworkAccess(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    LocalBroadcastManager.getInstance(getApplicationContext())
        .unregisterReceiver(broadcastReceiver);
  }


  @Override
  protected void onStart() {
    super.onStart();

    final ClickableSpan clickableSpan = new ClickableSpan() {

      @Override
      public void onClick(View textView) {
        String cx = getString(R.string.search_engine_id);
        String key = getString(R.string.google_key);

        String currentKeyword = ((TextView) textView).getText().toString();
        stringBuilder = new StringBuilder()
            .append("https://www.googleapis.com/customsearch/v1?q=").append(currentKeyword)
            .append("&cx=").append(cx).append("&fileType=png%2C+jpg&imgType=photo&searchType=image&fields=items%2Flink&key=")
            .append(key);

        if (networkOk) {
          Intent intent = new Intent(ImageActivity.this, MyService.class);
          intent.setData(Uri.parse(stringBuilder.toString()));
          startService(intent);
        } else {
          Toast.makeText(ImageActivity.this, "Network not available!", Toast.LENGTH_SHORT).show();
        }
      }

    };

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
}

package edu.cnm.deepdive.eb.flashme.fragments;

import static edu.cnm.deepdive.eb.flashme.fragments.AddCardFragment.currentBack;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import edu.cnm.deepdive.eb.flashme.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChooseImageFragment extends Fragment {

  private String cardBackKeyword = currentBack;
  private String[] arr = cardBackKeyword.split(" ");
//  private SpannableString yes;

  List<SpannableString> ss;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.image_fragment_view, container, false);


//    Bundle args = getArguments();
//    if (args  != null && args.containsKey("currentBack")) {
//      currentBack = args.getString("currentBack");
//    }

    final ClickableSpan clickableSpan = new ClickableSpan() {

      @Override
      public void onClick(View textView) {
        // TODO send the text of my clicked SpannableString to google API
        Toast.makeText(getActivity(), ((TextView) textView).getText(), Toast.LENGTH_SHORT).show();
        
      }

      @Override
      public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
      }
    };

    ss = new ArrayList<>();

    for (int i = 0; i < arr.length; i++) {
      ss.add(new SpannableString(arr[i]));
    }
    for (int i = 0; i < ss.size(); i++) {
      ss.get(i).setSpan(clickableSpan, 0, ss.get(i).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    LinearLayout rl = v.findViewById(R.id.image_layout);

    TextView[] ssTv = new TextView[arr.length];

    for (int i = 0; i < arr.length; i++) {
      ssTv[i] = new TextView(getActivity());
      ssTv[i].setText(ss.get(i));
      ssTv[i].setTextSize(30);
      ssTv[i].setPadding(5, 5, 5, 10);
      ssTv[i].setMovementMethod(LinkMovementMethod.getInstance());
      ssTv[i].setHighlightColor(Color.TRANSPARENT);
      ssTv[i].setTextColor(Color.parseColor("#000000"));
      rl.addView(ssTv[i]);
    }

    final EditText eText;
    Button btn;

    Log.d("search", "**** APP START");

    eText = v.findViewById(R.id.image_search_text);
    btn = v.findViewById(R.id.button_image_search);
    final TextView result = v.findViewById(R.id.image_links);

    btn.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {

        final String str = eText.getText().toString();
        Log.d("search", "Searching for :" + str);
        result.setText("Searching for :" + str);

        Thread thread = new Thread(new Runnable()
        {
          @Override
          public void run()
          {

            try {
              // looking for
              String strNoSpaces = str.replace(" ", "+");

              // Your API key
              String key= getString(R.string.google_key);

              // Your Search Engine ID
              String cx = getString(R.string.search_engine_id);

              String url2 = "https://www.googleapis.com/customsearch/v1?q=" + strNoSpaces + "&key=" + key + "&cx=" + cx + "&alt=json&fileType=png%2C+jpg&imgType=photo&searchType=image&fields=items%2Flink" ;
              Log.d("search", "Url = "+  url2);
              String result2 = httpGet(url2);

              result.setText(result2);

            }
            catch(Exception e) {
              System.out.println("Error1 " + e.getMessage());
            }

          }

          private String httpGet(String urlStr) throws IOException {

            URL url = new URL(urlStr);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if(conn.getResponseCode() != 200) {
              throw new IOException(conn.getResponseMessage());
            }

            Log.d("search", "Connection status = " + conn.getResponseMessage());

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while((line = rd.readLine()) != null) {

              Log.d("search", "Line =" + rd.readLine());
              sb.append(line+"\n");

            }
            rd.close();

            conn.disconnect();
            return sb.toString();
          }
        });

        thread.start();

      }
    });
    return v;
  }
  }






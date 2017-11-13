package edu.cnm.deepdive.eb.flashme.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.cnm.deepdive.eb.flashme.R;
import java.util.ArrayList;
import java.util.List;

public class ChooseImageFragment extends Fragment {

  // TODO link my card Back values to mCardBackKeyword
  String mCardBackKeyword = "Imagine I love candy do you love them too? If not, go away!";
  String[] arr = mCardBackKeyword.split(" ");
  private SpannableString ssOne, ssTwo, ssThree, ssFour;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.image_fragment_view, container, false);

    ClickableSpan clickableSpan = new ClickableSpan() {
      @Override
      public void onClick(View textView) {
        // TODO send the text of my clicked SpannableString to google API
        // TODO sign up for an Bing Image API
      }

      @Override
      public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
      }
    };

List<SpannableString> ss = new ArrayList<>();

for (int i = 0; i < arr.length; i++) {
  ss.add(new SpannableString(arr[i]));
}
      for (int i = 0; i < ss.size(); i ++ ) {
       ss.get(i).setSpan(clickableSpan, 0, ss.get(i).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      }

    LinearLayout rl = v.findViewById(R.id.image_layout);


    TextView[] ssTv = new TextView[arr.length];

    for(int i=0; i< arr.length; i++)
    {
      ssTv[i] = new TextView(getActivity());
      ssTv[i].setText(ss.get(i));
      ssTv[i].setTextSize(30);
      ssTv[i].setPadding(5, 5, 5, 10);
      ssTv[i].setMovementMethod(LinkMovementMethod.getInstance());
      ssTv[i].setHighlightColor(Color.TRANSPARENT);
//      ssTv[i].setPadding(50, 50, 0, 0);
      ssTv[i].setTextColor(Color.parseColor("#000000"));
      rl.addView(ssTv[i]);
    }

    return v;
  }

}

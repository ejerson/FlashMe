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

  String pig = "Imagine I love candy!";
  String[] arr = pig.split(" ");
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
      }

      @Override
      public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
      }
    };

List<SpannableString> ss = new ArrayList<>();

      ss.add(new SpannableString(arr[0]));
      ss.add(new SpannableString(arr[1]));
      ss.add(new SpannableString(arr[2]));
      ss.add(new SpannableString(arr[3]));

      for (int i = 0; i < ss.size(); i ++ ) {
       ss.get(i).setSpan(clickableSpan, 0, ss.get(i).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      }

    LinearLayout rl = v.findViewById(R.id.image_layout);

    TextView[] ssTv = new TextView[arr.length];

    for(int i=0; i< arr.length; i++)
    {
      ssTv[i] = new TextView(getActivity());
      // TODO assign each TextView with its own SpannableString
      ssTv[i].setText(ss.get(1));
      ssTv[i].setTextSize(20);
      ssTv[i].setMovementMethod(LinkMovementMethod.getInstance());
      ssTv[i].setHighlightColor(Color.TRANSPARENT);

      ssTv[i].setPadding(50, 50, 0, 0);
      ssTv[i].setTextColor(Color.parseColor("#000000"));
      rl.addView(ssTv[i]);
    }

    return v;
  }

}

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
import android.widget.Toast;
import edu.cnm.deepdive.eb.flashme.R;
import java.util.ArrayList;
import java.util.List;

public class ChooseImageFragment extends Fragment {

  String pig = "Imagine I love candy!";
  String[] arr = pig.split(" ");
  private SpannableString ssOne, ssTwo, ssThree, ssFour;
  TextView ssTextView;


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

        Toast.makeText(getActivity(), ssOne, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), ssTwo, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), ssThree, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), ssFour, Toast.LENGTH_SHORT).show();

//        ChooseImageFragment fragment = new ChooseImageFragment();
//        Bundle args = new Bundle();
////        args.putInt(DeckMemberFragment.DECK_ID,
////            getActivity().getIntent().getIntExtra(DeckMemberFragment.DECK_ID, 0));
//        fragment.setArguments(args); // bundle
//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();


      }

      @Override
      public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
      }
    };



List<SpannableString> ss = new ArrayList<>();

      ss.add(ssOne = new SpannableString(arr[0]));
      ss.add(ssTwo = new SpannableString(arr[1]));
      ss.add(ssThree = new SpannableString(arr[2]));
      ss.add(ssFour = new SpannableString(arr[3]));

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


    // TODO the back of a card turns into something clickable which will search my api for images/ 1 max for now
    // TODO make sure the chosen keyword gets loaded along with a new fragment

    return v;
  }

}

package edu.cnm.deepdive.eb.flashme.fragments;

import android.app.AlertDialog.Builder;
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
import android.widget.TextView;
import edu.cnm.deepdive.eb.flashme.R;

public class ChooseImageFragment extends Fragment {

  String pig = "Imagine I love candy!";
  String[] arr = pig.split(" ");
  private SpannableString ss;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Builder builder = new Builder(getActivity());

    LayoutInflater inflater = getActivity().getLayoutInflater();

    View inflatedView = inflater.inflate(R.layout.image_fragment_view, null);

  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.image_fragment_view, container, false);

    // TODO turn text into array so I can refer to each index



    ss = new SpannableString(arr[1]);
    ClickableSpan clickableSpan = new ClickableSpan() {
      @Override
      public void onClick(View textView) {


//        ChooseImageFragment fragment = new ChooseImageFragment();
//        Bundle args = new Bundle();
//        args.putInt(DeckMemberFragment.DECK_ID,
//            getActivity().getIntent().getIntExtra(DeckMemberFragment.DECK_ID, 0));
//        fragment.setArguments(args); // bundle
//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
//

      }

      @Override
      public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
      }
    };



    ss.setSpan(clickableSpan, 0, ss.length(), Spanned.SPAN_COMPOSING);
//    ss.setSpan(clickableSpan, 1, ss.length() - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//
    TextView textView = v.findViewById(R.id.hello);
    textView.setText(ss);
    textView.setMovementMethod(LinkMovementMethod.getInstance());
    textView.setHighlightColor(Color.TRANSPARENT);
    // TODO the back of a card turns into something clickable which will search my api for images/ 1 max for now
    // TODO make sure the chosen keyword gets loaded along with a new fragment

    return v;
  }

}

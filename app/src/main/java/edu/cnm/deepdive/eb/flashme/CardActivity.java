package edu.cnm.deepdive.eb.flashme;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class CardActivity extends AppCompatActivity {

  FrameLayout mainLayout;
  TextView textTitle;
  Button buttonFlip;
  ImageView image;

  // Experimented with flipping text and buttons

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_card);
    textTitle = (TextView)findViewById(R.id.love);
    textTitle.setOnClickListener(MyOnClickListener);
    buttonFlip = (Button)findViewById(R.id.buttonflip);
    buttonFlip.setOnClickListener(MyOnClickListener);


  }

  OnClickListener MyOnClickListener = new OnClickListener(){

    @Override
    public void onClick(View v) {
      flipIt(v);
    }

  };

  private void flipIt(final View viewToFlip) {
    ObjectAnimator flip = ObjectAnimator.ofFloat(viewToFlip, "rotationX", 0f, 360f);
    flip.setDuration(3000);
    flip.start();

  }

}

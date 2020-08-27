package com.example.cashy.designone;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

public class WorkoutActivity extends AppCompatActivity {
    TextView textView;
    TranslateAnimation translation;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        textView = findViewById(R.id.WelcomeBack);
        translation = new TranslateAnimation(-50, 0, 0, 0);
        translation.setDuration(1500);
      //  textView.setAnimation(translation);
        textView.setVisibility(View.VISIBLE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int[] point = new int[2];
        int[] point2 = new int[2];
        textView.getLocationOnScreen(point);
        textView.getLocationInWindow(point2);

        System.out.println(point2[0]);
        System.out.println(point2[1]);

    }
}

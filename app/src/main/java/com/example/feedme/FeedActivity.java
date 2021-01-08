package com.example.feedme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.Time;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;


public class FeedActivity extends AppCompatActivity {


    private long startBoneTime = 1500;
    private long boneTime = 1500;
    private long feedTime = 10000;


    private Button mBtGoBack;
    private ImageView feedImage;

    private TextView testView;
    private TextView pointsView;


    public int g;
    public int punkty = 0;

    private CountDownTimer boneCountdown;
    private CountDownTimer countdown;
    private static final Integer[] mImageIds =
            { R.drawable.bone, R.drawable.gear, };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Intent intent =getIntent();
        int diff = intent.getIntExtra(MainActivity.DIFFICULTY, 0);
        switch(diff){
            case 7:
                startBoneTime = 2250;
                boneTime = 2250;
                feedTime = 30000;
                break;
            case 3:
                startBoneTime = 1500;
                boneTime = 1500;
                feedTime = 15000;
                break;
            case 2:
                startBoneTime = 800;
                boneTime = 800;
                feedTime = 10000;
                break;
        }


        //newLocation();
        startTimer();


        feedImage = (ImageView) findViewById(R.id.feedgame);
        mBtGoBack = (Button) findViewById(R.id.bt_go_back);
        testView = (TextView) findViewById(R.id.test);
        pointsView = (TextView) findViewById(R.id.points);

        final MediaPlayer point_up = MediaPlayer.create(this, R.raw.pointup);
        final MediaPlayer point_down = MediaPlayer.create(this, R.raw.pointdown);


        RelativeLayout game = findViewById(R.id.feed_layout);
        AnimationDrawable a = (AnimationDrawable) game.getBackground();
        a.setEnterFadeDuration(2000);
        a.setExitFadeDuration(4000);
        a.start();


        mBtGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        feedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartTimer();
                Object tag = feedImage.getTag();
                if (mImageIds[0].equals(tag)) {
                    punkty++;
                    point_up.start();
                } else if (mImageIds[1].equals(tag)) {
                    punkty--;
                    point_down.start();
                }
                newLocation();
                updateText();
            }
        });


    }

    private void countDown() {
        countdown = new CountDownTimer(feedTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                feedTime = millisUntilFinished;
                updateText();
            }

            @Override
            public void onFinish() {
                setResult(FeedActivity.RESULT_OK,
                        new Intent().putExtra("punkty", punkty).putExtra("czas", feedTime));
                finish();
            }
        }.start();
    }
    private void startTimer() {
        boneCountdown = new CountDownTimer(boneTime, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                boneTime = millisUntilFinished;

            }

            @Override
            public void onFinish() {
                newLocation();
                restartTimer();

            }
        }.start();
    }
    private void restartTimer() {
        boneCountdown.cancel();
        boneTime = startBoneTime;
        startTimer();

    }
    private void updateText() {
        int time = (int) (feedTime / 1000);
        String timeLeftFormatted = String.format(Locale.getDefault(), "%03d", time);
        String punktyFormatted = String.format(Locale.getDefault(), "%03d", punkty);
        testView.setText(timeLeftFormatted);
        pointsView.setText(punktyFormatted);
    }

    private void newLocation() {
        final DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Random rand = new Random();
        final float dx = rand.nextFloat() * (displaymetrics.widthPixels-250);
        final float dy = rand.nextFloat() * (displaymetrics.heightPixels-250);
        final float dr = rand.nextInt(360);
        g = rand.nextInt(2);
        //final Timer timer = new Timer();
        if (g == 0){
            feedImage.setImageResource(mImageIds[0]);
            feedImage.setTag(mImageIds[0]);
            feedImage.animate()
                    .x(dx)
                    .y(dy)
                    .rotation(dr)
                    .setDuration(0)
                    .start();
        } else if (g == 1){
            feedImage.setImageResource(mImageIds[1]);
            feedImage.setTag(mImageIds[1]);
            feedImage.animate()
                    .x(dx)
                    .y(dy)
                    .rotation(dr)
                    .setDuration(0)
                    .start();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        newLocation();
        countDown();

    }
}
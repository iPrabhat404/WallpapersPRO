package com.prabhat.wallpaperspro.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.prabhat.wallpaperspro.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView cloud1;
    private ImageView cloud2;
    private ImageView cloud3;
    private ImageView sun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        cloud1 = (ImageView)findViewById(R.id.cloud1);
        cloud2 = (ImageView)findViewById(R.id.cloud2);
        cloud3 = (ImageView)findViewById(R.id.cloud3);
        sun = (ImageView)findViewById(R.id.sun);

        Handler handler = new Handler();
        ObjectAnimator cloud1Animator = ObjectAnimator.ofFloat(cloud1, "translationX", 100f);
        ObjectAnimator cloud2Animator = ObjectAnimator.ofFloat(cloud2, "translationX", 100f);
        ObjectAnimator cloud3Animator = ObjectAnimator.ofFloat(cloud3, "translationX", 300f);
        ObjectAnimator sunAnimator = ObjectAnimator.ofFloat(sun, "translationY", -200f);

//        cloud1Animator.ofFloat(cloud1, "translationX", 600f);
//        cloud1Animator.setDuration(3000);
//
//        cloud2Animator.ofFloat(cloud2, "translationX", 900f);
//        cloud2Animator.setDuration(3000);
//
//        cloud3Animator.ofFloat(cloud3, "translationX", 1000f);
//        cloud3Animator.setDuration(3000);
//
//        sunAnimator.ofFloat(sun, "translationY", 1000f);
//        sunAnimator.setDuration(3000);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(cloud1Animator, cloud2Animator, cloud3Animator, sunAnimator);
        animatorSet.setDuration(3500);
        animatorSet.start();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }, 3000);

    }
}

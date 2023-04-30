package com.example.ronifitgo.ronifitgo.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


import com.airbnb.lottie.LottieAnimationView;
import com.example.ronifitgo.R;

public class Activity_goodbye extends AppCompatActivity {


    private LottieAnimationView final_animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodbye);
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 1500);

    }
    }

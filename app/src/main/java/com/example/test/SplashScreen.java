package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("FUTURISTIC");
        setContentView(R.layout.activity_splash_screen);
        int SPLASH_TIMEOUT = 2000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent registerClass = new Intent(getApplicationContext(), login.class);startActivity(registerClass);finish();
            }
        }, SPLASH_TIMEOUT);
    }
}
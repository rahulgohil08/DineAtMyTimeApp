package com.experiments.dineatmytime.ui.splash;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.experiments.dineatmytime.R;
import com.experiments.dineatmytime.ui.login.Login;


public class SplashActivity extends Activity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashfile);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, 1000);

    }
}

package com.nibou.niboucustomer.activitys;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.LocalPrefences;

public class SplashActivity extends BaseActivity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    private void init() {
        handler = new Handler();
        handler.postDelayed(runnable, 1000);
    }

    private Runnable runnable = () -> moveToNextScreen();

    private void moveToNextScreen() {
        if (LocalPrefences.getInstance().getBoolean(this, AppConstant.IS_FIRST_LAUNCH_SUCCESS)) {
            if (LocalPrefences.getInstance().isUserLogin(this)) {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SplashActivity.this, UserCheckActivity.class);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(SplashActivity.this, SliderActivity.class);
            startActivity(intent);
        }
        finish();
    }
}


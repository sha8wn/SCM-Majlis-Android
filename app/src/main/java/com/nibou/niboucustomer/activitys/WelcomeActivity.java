package com.nibou.niboucustomer.activitys;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.LocalPrefences;

public class WelcomeActivity extends AppCompatActivity {

    private ImageView startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            startButton.setImageResource(R.drawable.logo5);
            startButton.setEnabled(false);
            MediaPlayer mPlayer = MediaPlayer.create(WelcomeActivity.this, R.raw.start);
            mPlayer.setOnCompletionListener(mp -> {
                LocalPrefences.getInstance().putBoolean(WelcomeActivity.this, AppConstant.IS_FIRST_LAUNCH_SUCCESS, true);
                Intent intent = new Intent(WelcomeActivity.this, UserCheckActivity.class);
                startActivity(intent);
                finishAffinity();
            });
            mPlayer.start();
        });
    }

    @Override
    public void onBackPressed() {
    }
}

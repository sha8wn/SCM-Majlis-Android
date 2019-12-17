package com.nibou.niboucustomer.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.LocalPrefences;

public class WelcomeActivity extends BaseActivity {

    private ImageView startButton;
    private VideoView view;
    private ImageView videoThumbnail;
    private Uri videoURI;

    private boolean isStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        showVideoAndThumbnail();

        view.setOnCompletionListener(mp -> {
            LocalPrefences.getInstance().putBoolean(WelcomeActivity.this, AppConstant.IS_FIRST_LAUNCH_SUCCESS, true);
            Intent intent = new Intent(WelcomeActivity.this, UserCheckActivity.class);
            startActivity(intent);
            finishAffinity();
        });
    }

    @Override
    public void onBackPressed() {
    }

    public void showVideoAndThumbnail() {

        startButton = findViewById(R.id.startButton);
        view = (VideoView) findViewById(R.id.videoView);
        videoThumbnail = findViewById(R.id.videoThumbnail);

        videoURI = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.start_button_video);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this, videoURI);
        videoThumbnail.setBackground(new BitmapDrawable(getResources(), retriever.getFrameAtTime(100000, MediaMetadataRetriever.OPTION_PREVIOUS_SYNC)));

        view.setVideoURI(videoURI);
        view.requestFocus();
        view.setOnPreparedListener(mp -> {
            if (!isStarted) {
                view.seekTo(1);
                view.pause();
            }
        });

        startButton.setOnClickListener(v -> {
            isStarted = true;
            view.start();
            view.setAlpha(1);
            videoThumbnail.setVisibility(View.INVISIBLE);
        });

        view.setOnErrorListener((mp, what, extra) -> true);
    }
}

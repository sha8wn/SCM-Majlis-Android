package com.nibou.niboucustomer.activitys;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.LocalPrefences;
import com.nibou.niboucustomer.utils.TextureVideoView;

public class WelcomeActivity extends BaseActivity {

    private ImageView startButton;
    private ImageView videoThumbnail;
    private Uri videoURI;

    private boolean isStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        showVideoAndThumbnail();
    }

    @Override
    public void onBackPressed() {
    }

    public void showVideoAndThumbnail() {

        videoURI = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.new_video);

        videoThumbnail = findViewById(R.id.videoThumbnail);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this, videoURI);
        videoThumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
        videoThumbnail.setImageBitmap(retriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_PREVIOUS_SYNC));

        TextureVideoView view = findViewById(R.id.videoView);
        view.setScaleType(TextureVideoView.ScaleType.CENTER_CROP);

        view.setDataSource(this, videoURI);
        view.setListener(new TextureVideoView.MediaPlayerListener() {
            @Override
            public void onVideoPrepared() {
                videoThumbnail.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onVideoEnd() {
                LocalPrefences.getInstance().putBoolean(WelcomeActivity.this, AppConstant.IS_FIRST_LAUNCH_SUCCESS, true);
                Intent intent = new Intent(WelcomeActivity.this, UserCheckActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            view.play();
        });

//        view.setVideoURI(videoURI);
//        view.requestFocus();
//        view.setOnPreparedListener(mp -> {
//            if (!isStarted) {
//                view.seekTo(300);
//                view.pause();
//            }
//        });
//        startButton.setOnClickListener(v -> {
//            isStarted = true;
//            view.start();
//            view.setAlpha(1);
//            videoThumbnail.setVisibility(View.INVISIBLE);
//        });
//
//        view.setOnErrorListener((mp, what, extra) -> true);
//        view.setOnCompletionListener(mp -> {
////            LocalPrefences.getInstance().putBoolean(WelcomeActivity.this, AppConstant.IS_FIRST_LAUNCH_SUCCESS, true);
////            Intent intent = new Intent(WelcomeActivity.this, UserCheckActivity.class);
////            startActivity(intent);
////            finishAffinity();
//        });
    }
}

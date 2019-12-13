package com.nibou.niboucustomer.customviews;

import android.app.Activity;
import android.content.res.Configuration;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.models.ListResponseModel;

import java.util.ArrayList;

public class MyImageViewPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ImageView cross;
    private View rootView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_image_view_pager);
        rootView = findViewById(R.id.rootView);
        cross = findViewById(R.id.imgCross);
        cross.setOnClickListener(view -> onBackPressed());

        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(new MySlidingImageAdapter(MyImageViewPagerActivity.this, ((ArrayList<ListResponseModel.Img>) getIntent().getSerializableExtra("LIST"))));
        viewPager.setCurrentItem(getIntent().getIntExtra("INDEX", 0));

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            cross.setAlpha(0f);
        } else {
            cross.setAlpha(1f);
        }
        handler = new Handler();
    }

    public void handleCrossButton() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fadeInButton();
        }
    }

    private void fadeInButton() {
        if (cross != null && cross.getAlpha() == 0f) {
            cross.animate().alpha(1f).setDuration(1000);
            handler.postDelayed(() -> fadeOutButton(), 2000);
        }
    }

    private void fadeOutButton() {
        if (cross != null && cross.getAlpha() == 1f) {
            cross.animate().alpha(0f).setDuration(1000);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (cross != null) {
                cross.setAlpha(0f);
            }
        }
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (cross != null) {
                cross.setAlpha(1f);
            }
        }
        super.onConfigurationChanged(newConfig);
    }


}
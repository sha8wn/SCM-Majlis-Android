package com.nibou.niboucustomer.customviews;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.models.ListResponseModel;

import java.util.ArrayList;

public class MyImageViewPagerActivity extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener {

//    private static final int SWIPE_MIN_DISTANCE = 120;
//    private static final int SWIPE_MAX_OFF_PATH = 250;
//    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
//    private GestureDetector gestureDetector;


    private ViewPager viewPager;

    private SimpleGestureFilter simpleGestureFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        simpleGestureFilter = new SimpleGestureFilter(this, this);

        setContentView(R.layout.activity_my_image_view_pager);
        findViewById(R.id.imgCross).setOnClickListener(view -> onBackPressed());
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(new MySlidingImageAdapter(MyImageViewPagerActivity.this, ((ArrayList<ListResponseModel.Img>) getIntent().getSerializableExtra("LIST"))));
        viewPager.setCurrentItem(getIntent().getIntExtra("INDEX", 0));

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            findViewById(R.id.imgCross).setVisibility(View.GONE);
        } else {
            findViewById(R.id.imgCross).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (findViewById(R.id.imgCross) != null) {
                findViewById(R.id.imgCross).setVisibility(View.GONE);
            }
        }
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (findViewById(R.id.imgCross) != null) {
                findViewById(R.id.imgCross).setVisibility(View.VISIBLE);
            }
        }

        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        simpleGestureFilter.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction) {
        if (direction == SimpleGestureFilter.SWIPE_DOWN) {
            onBackPressed();
        }
    }

    @Override
    public void onDoubleTap() {

    }


//    private class SwipeDetector extends GestureDetector.SimpleOnGestureListener {
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//
//            // Check movement along the Y-axis. If it exceeds SWIPE_MAX_OFF_PATH,
//            // then dismiss the swipe.
//            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
//                return false;
//
//            // Swipe from left to right.
//            // The swipe needs to exceed a certain distance (SWIPE_MIN_DISTANCE)
//            //  and a certain velocity (SWIPE_THRESHOLD_VELOCITY).
//            if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                finish();
//                return true;
//            }
//
//            return false;
//        }
//    }
}

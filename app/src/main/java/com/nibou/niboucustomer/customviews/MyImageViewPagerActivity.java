package com.nibou.niboucustomer.customviews;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.nibou.niboucustomer.R;

import java.util.ArrayList;
import java.util.List;

public class MyImageViewPagerActivity extends AppCompatActivity {

    ViewPager viewPager;
    List<String> mImageList = new ArrayList<>();
    int currentPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_image_view_pager);
        init();
    }

    private void init() {
        ImageView imgBack = findViewById(R.id.imgCross);
        imgBack.setOnClickListener(view -> onBackPressed());
        if (getIntent().getExtras() != null) {
            mImageList = (List<String>) getIntent().getExtras().getSerializable("LIST");
        }

        if (getIntent().getExtras() != null) {
            currentPos = getIntent().getExtras().getInt("INDEX");
        }

        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(new MySlidingImageAdapter(MyImageViewPagerActivity.this, mImageList));
        viewPager.setCurrentItem(currentPos);
    }
}

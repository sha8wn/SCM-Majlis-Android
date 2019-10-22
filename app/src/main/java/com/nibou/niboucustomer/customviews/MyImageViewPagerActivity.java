package com.nibou.niboucustomer.customviews;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.models.ListResponseModel;

import java.util.ArrayList;

public class MyImageViewPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_image_view_pager);
        findViewById(R.id.imgCross).setOnClickListener(view -> onBackPressed());

        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(new MySlidingImageAdapter(MyImageViewPagerActivity.this, ((ArrayList<ListResponseModel.Img>) getIntent().getSerializableExtra("LIST"))));
        viewPager.setCurrentItem(getIntent().getIntExtra("INDEX", 0));
    }
}

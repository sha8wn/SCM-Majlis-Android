package com.nibou.niboucustomer.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.fragments.PagerFragment;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.LocalPrefences;

public class SliderActivity extends BaseActivity {

    private ViewPager mViewPager;
    private int page = 0;
    private ImageView zero, one, two, three, four, five, mNextBtn;
    private ImageView[] indicators;
    private View back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//        back_button = findViewById(R.id.back_arrow);
//        back_button.setVisibility(View.GONE);
//        back_button.setOnClickListener(v -> {
//            page -= 1;
//            mViewPager.setCurrentItem(page, true);
//        });
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
//        mFinishBtn = findViewById(R.id.intro_btn_finish);
        mNextBtn = findViewById(R.id.intro_btn_next);
        zero = findViewById(R.id.intro_indicator_0);
        one = findViewById(R.id.intro_indicator_1);
        two = findViewById(R.id.intro_indicator_2);
        three = findViewById(R.id.intro_indicator_3);
        four = findViewById(R.id.intro_indicator_4);
        five = findViewById(R.id.intro_indicator_5);

        indicators = new ImageView[]{zero, one, two /*,three, four, five*/};

        mViewPager = (ViewPager) findViewById(R.id.container);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                page = position;
                updateIndicators(page);

//                mNextBtn.setVisibility(position == 2 ? View.VISIBLE : View.VISIBLE);
//                mFinishBtn.setVisibility(position == 2 ? View.GONE : View.GONE);register
//                back_button.setVisibility(position == 0 ? View.GONE : View.VISIBLE);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mNextBtn.setOnClickListener(view -> {
            page += 1;
            mViewPager.setCurrentItem(page, true);
            if (page == 3) {
                Intent intent = new Intent(SliderActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void updateIndicators(int page) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(i == page ? R.drawable.indicator_selected : R.drawable.indicator_unselected);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PagerFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}

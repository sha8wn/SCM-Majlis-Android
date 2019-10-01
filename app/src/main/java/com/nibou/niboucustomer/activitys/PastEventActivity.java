package com.nibou.niboucustomer.activitys;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.adapters.HorizontalListAdapter;
import com.nibou.niboucustomer.databinding.LayoutPastEventsBinding;
import com.nibou.niboucustomer.models.PreviousExpertModel;

public class PastEventActivity extends AppCompatActivity {
    private LayoutPastEventsBinding binding;
    private HorizontalListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.layout_past_events);

        binding.tvLogin.setOnClickListener(view -> startActivity(new Intent(PastEventActivity.this, LoginActivity.class)));

        binding.rvEvents.setLayoutManager(new LinearLayoutManager(this));
        mListAdapter = new HorizontalListAdapter(this, new PreviousExpertModel());
        binding.rvEvents.setAdapter(mListAdapter);

    }
}

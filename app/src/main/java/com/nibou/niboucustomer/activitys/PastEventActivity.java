package com.nibou.niboucustomer.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.adapters.PastEventAdapter;
import com.nibou.niboucustomer.databinding.ActivityScmPastEventsBinding;
import com.nibou.niboucustomer.models.PreviousExpertModel;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;

public class PastEventActivity extends AppCompatActivity {
    private long backPressedClickTime;
    private ActivityScmPastEventsBinding binding;
    private Context context;

    private PastEventAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scm_past_events);
        context = this;
        init();
    }

    private void init() {
        if (getIntent().hasExtra("type")) {
            binding.tvLogin.setVisibility(View.GONE);
            binding.toolbar.setVisibility(View.VISIBLE);
        } else {
            binding.tvLogin.setVisibility(View.VISIBLE);
            binding.toolbar.setVisibility(View.GONE);
        }

        binding.toolbar.findViewById(R.id.back_arrow).setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            finish();
        });

        binding.tvLogin.setOnClickListener(view -> {
            startActivity(new Intent(PastEventActivity.this, UserCheckActivity.class));
            finishAffinity();
        });

        binding.rvEvents.setLayoutManager(new LinearLayoutManager(this));
        mListAdapter = new PastEventAdapter(this, new PreviousExpertModel());
        binding.rvEvents.setAdapter(mListAdapter);
    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("type")) {
            super.onBackPressed();
        } else {
            checkForAppExit();
        }
    }

    private void checkForAppExit() {
        if ((backPressedClickTime + 2000) > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this, getResources().getString(R.string.press_back_message), Toast.LENGTH_SHORT).show();
        }
        backPressedClickTime = System.currentTimeMillis();
    }
}

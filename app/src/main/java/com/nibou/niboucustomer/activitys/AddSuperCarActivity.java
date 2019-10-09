package com.nibou.niboucustomer.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.adapters.AddSuperCarAdapter;
import com.nibou.niboucustomer.databinding.ActivitySupercarsBinding;
import com.nibou.niboucustomer.models.PreviousExpertModel;
import com.nibou.niboucustomer.utils.AppUtil;

public class AddSuperCarActivity extends BaseActivity {

    private ActivitySupercarsBinding binding;
    private AddSuperCarAdapter mListAdapter;
    private Context context;

    private boolean isSettingMenuScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_supercars);
        context = this;
        binding.toolbar.findViewById(R.id.back_arrow).setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            onBackPressed();
        });

        binding.btnNext.setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            Intent intent = new Intent(context, DocumentActivity.class);
            startActivity(intent);
        });

        if (getIntent().hasExtra("type")) {
            isSettingMenuScreen = true;
            binding.signupTitle.setVisibility(View.GONE);
            binding.prevoiusTitle.setVisibility(View.GONE);
            binding.nextTitle.setVisibility(View.GONE);
            binding.screenTitle.setText(getString(R.string.mane_supercar));
            binding.btnSave.setVisibility(View.VISIBLE);
            binding.btnNext.setVisibility(View.GONE);
        } else {
            binding.btnSave.setVisibility(View.GONE);
            binding.btnNext.setVisibility(View.VISIBLE);
            binding.signupTitle.setVisibility(View.VISIBLE);
            binding.prevoiusTitle.setVisibility(View.VISIBLE);
            binding.nextTitle.setVisibility(View.VISIBLE);
            binding.screenTitle.setText(getString(R.string.supercars));
        }

        init();
    }

    private void init() {

        binding.rvCars.setLayoutManager(new LinearLayoutManager(context));
        mListAdapter = new AddSuperCarAdapter(context, isSettingMenuScreen);
        binding.rvCars.setAdapter(mListAdapter);
    }
}

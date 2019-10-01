package com.nibou.niboucustomer.activitys;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.adapters.AddSuperCarAdapter;
import com.nibou.niboucustomer.databinding.ActivitySupercarsBinding;
import com.nibou.niboucustomer.models.PreviousExpertModel;
import com.nibou.niboucustomer.utils.AppUtil;

public class AddSuperCarActivity extends AppCompatActivity {
    private ActivitySupercarsBinding binding;
    private AddSuperCarAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_supercars);
        binding.toolbar.findViewById(R.id.back_arrow).setOnClickListener(v -> {
            AppUtil.hideKeyBoard(this);
            onBackPressed();
        });
        binding.introBtnNext.setOnClickListener(view -> startActivity(new Intent(AddSuperCarActivity.this, LoginActivity.class)));

        binding.tvAddCar.setOnClickListener(view -> Log.e("Add ", "add"));

        binding.rvCars.setLayoutManager(new LinearLayoutManager(this));
        mListAdapter = new AddSuperCarAdapter(this, new PreviousExpertModel());
        binding.rvCars.setAdapter(mListAdapter);


    }
}

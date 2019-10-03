package com.nibou.niboucustomer.activitys;

import android.content.Context;
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

public class AddSuperCarActivity extends BaseActivity {

    private ActivitySupercarsBinding binding;
    private AddSuperCarAdapter mListAdapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_supercars);
        context = this;
        binding.toolbar.findViewById(R.id.back_arrow).setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            onBackPressed();
        });

        init();
    }

    private void init() {

        binding.rvCars.setLayoutManager(new LinearLayoutManager(context));
        mListAdapter = new AddSuperCarAdapter(context, new PreviousExpertModel());
        binding.rvCars.setAdapter(mListAdapter);
    }
}

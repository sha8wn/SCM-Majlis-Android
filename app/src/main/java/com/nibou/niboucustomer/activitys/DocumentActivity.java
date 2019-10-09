package com.nibou.niboucustomer.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.adapters.AddSuperCarAdapter;
import com.nibou.niboucustomer.databinding.ActivityUserDocumentsBinding;
import com.nibou.niboucustomer.utils.AppUtil;

public class DocumentActivity extends BaseActivity {

    private ActivityUserDocumentsBinding binding;
    private AddSuperCarAdapter mListAdapter;
    private Context context;

    private boolean isSettingMenuScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_documents);
        context = this;
        binding.toolbar.findViewById(R.id.back_arrow).setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            onBackPressed();
        });
        binding.btnFinish.setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            AppDialogs.getInstance().showCustomDialog(context, getString(R.string.success), getString(R.string.admin_register_success), getString(R.string.continu), getResources().getColor(R.color.green), status -> {
                Intent intent = new Intent(context, HomeActivity.class);
                startActivity(intent);
                finishAffinity();
            });
        });

        if (getIntent().hasExtra("type")) {
            isSettingMenuScreen = true;
            binding.signupTitle.setVisibility(View.GONE);
            binding.prevoiusTitle.setVisibility(View.GONE);
            binding.screenTitle.setText(getString(R.string.manage_documents));
            binding.btnFinish.setText(getString(R.string.save));
        } else {
            binding.btnFinish.setText(getString(R.string.finish));
            binding.signupTitle.setVisibility(View.VISIBLE);
            binding.prevoiusTitle.setVisibility(View.VISIBLE);
            binding.screenTitle.setText(getString(R.string.documents));
        }

        init();
    }

    private void init() {

    }
}

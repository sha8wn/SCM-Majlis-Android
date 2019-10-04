package com.nibou.niboucustomer.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.adapters.AddSuperCarAdapter;
import com.nibou.niboucustomer.databinding.ActivityUserDocumentsBinding;
import com.nibou.niboucustomer.utils.AppUtil;

public class DocumentActivity extends BaseActivity {

    private ActivityUserDocumentsBinding binding;
    private AddSuperCarAdapter mListAdapter;
    private Context context;

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
                Intent intent = new Intent(context, PastEventActivity.class);
                startActivity(intent);
                finishAffinity();
            });
        });


        init();
    }

    private void init() {

    }
}

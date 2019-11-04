package com.nibou.niboucustomer.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.adapters.PastEventAdapter;
import com.nibou.niboucustomer.api.ApiClient;
import com.nibou.niboucustomer.api.ApiEndPoint;
import com.nibou.niboucustomer.api.ApiHandler;
import com.nibou.niboucustomer.databinding.ActivityScmPastEventsBinding;
import com.nibou.niboucustomer.models.ListResponseModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;

public class PastEventActivity extends AppCompatActivity {
    private long backPressedClickTime;

    private ActivityScmPastEventsBinding binding;
    private Context context;
    private PastEventAdapter mListAdapter;
    private ListResponseModel eventResponseModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scm_past_events);
        context = this;

        binding.toolbar.findViewById(R.id.back_arrow).setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            finish();
        });

        binding.tvLogin.setOnClickListener(view -> {
            startActivity(new Intent(context, UserCheckActivity.class));
            finishAffinity();
        });

        init();
    }

    private void init() {
        if (getIntent().hasExtra(AppConstant.SCREEN_FLOW_FLAG)) {
            binding.tvLogin.setVisibility(View.GONE);
            binding.toolbar.setVisibility(View.VISIBLE);
        } else {
            binding.tvLogin.setVisibility(View.VISIBLE);
            binding.toolbar.setVisibility(View.GONE);
        }

        if (AppUtil.isInternetAvailable(context)) {
            getPastEventsNetworkCall(1);
        } else {
            AppUtil.showToast(context, getString(R.string.internet_error));
        }
    }

    public void getPastEventsNetworkCall(int pageNumber) {
        AppDialogs.getInstance().showProgressBar(context, null, true);
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).getPastEventsNetworkCall(20, pageNumber), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    eventResponseModel = (ListResponseModel) data;
                    binding.rvEvents.setLayoutManager(new LinearLayoutManager(context));
                    mListAdapter = new PastEventAdapter(context, eventResponseModel);
                    binding.rvEvents.setAdapter(mListAdapter);
                } else {
                    AppDialogs.getInstance().showCustomDialog(context, context.getString(R.string.error).toUpperCase(), String.valueOf(data), context.getString(R.string.OK),context.getResources().getColor(R.color.colorPrimary), null);
                }
            }

            @Override
            public void failed() {
                AppDialogs.getInstance().showProgressBar(context, null, false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra(AppConstant.SCREEN_FLOW_FLAG)) {
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

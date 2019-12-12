package com.nibou.niboucustomer.activitys;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.api.ApiClient;
import com.nibou.niboucustomer.api.ApiEndPoint;
import com.nibou.niboucustomer.api.ApiHandler;
import com.nibou.niboucustomer.application.SCMApplication;
import com.nibou.niboucustomer.databinding.ActivityFeedbackBinding;
import com.nibou.niboucustomer.models.ErrorResponseModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;

import java.util.HashMap;

public class FeedbackActivity extends BaseActivity {

    private ActivityFeedbackBinding binding;
    private Context context;

    private void setToolbar() {
        binding.toolbar.findViewById(R.id.back_arrow).setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            onBackPressed();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SCMApplication)getApplication()).getFirebaseAnalytics().logEvent(AppConstant.CONTACT_EVENT, new Bundle());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);
        context = this;
        setToolbar();

        binding.btnSave.setOnClickListener(v -> {
            if (AppUtil.isInternetAvailable(context)) {
                if (screenValidate()) {
                    saveFeedbackNetworkCall();
                }
            } else {
                AppUtil.showToast(context, getString(R.string.internet_error));
            }
        });
    }

    private void saveFeedbackNetworkCall() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("subject", binding.subject.getText().toString().trim());
        parameters.put("text", binding.feedbackMsg.getText().toString().trim());

        AppDialogs.getInstance().showProgressBar(context, null, true);
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).feedbackNetworkCall(LocalPrefences.getInstance().getString(context, AppConstant.TOKEN), parameters), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    AppDialogs.getInstance().showCustomDialog(context, getString(R.string.success).toUpperCase(), getString(R.string.feedback_success_alert), getString(R.string.OK), getResources().getColor(R.color.green), status -> onBackPressed());
                } else {
                    AppDialogs.getInstance().showCustomDialog(context, getString(R.string.error).toUpperCase(), String.valueOf(data), getString(R.string.OK), getResources().getColor(R.color.colorPrimary), null);
                }
            }

            @Override
            public void failed() {
                AppDialogs.getInstance().showProgressBar(context, null, false);
            }
        });
    }


    private boolean screenValidate() {
        if (TextUtils.isEmpty(binding.subject.getText())) {
            AppUtil.showToast(this, getResources().getString(R.string.enter_subject));
            return false;
        } else if (TextUtils.isEmpty(binding.feedbackMsg.getText())) {
            AppUtil.showToast(this, getResources().getString(R.string.enter_feedback));
            return false;
        }
        return true;
    }
}

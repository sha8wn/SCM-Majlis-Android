package com.nibou.niboucustomer.activitys;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;

import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.api.ApiClient;
import com.nibou.niboucustomer.api.ApiEndPoint;
import com.nibou.niboucustomer.api.ApiHandler;
import com.nibou.niboucustomer.databinding.ActivityPrivacyPolicyBinding;
import com.nibou.niboucustomer.models.ListResponseModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;

public class PrivacyPolicyActivity extends BaseActivity {

    private ActivityPrivacyPolicyBinding binding;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy);
        context = this;
        binding.toolbar.findViewById(R.id.back_arrow).setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            onBackPressed();
        });
        if (getIntent().getStringExtra(AppConstant.SCREEN_FLOW_FLAG).equals(AppConstant.TEXT_PRIVACY)) {
            binding.titleHeader.setText(context.getString(R.string.privacy_policy));
        } else if (getIntent().getStringExtra(AppConstant.SCREEN_FLOW_FLAG).equals(AppConstant.TEXT_TERMS)) {
            binding.titleHeader.setText(context.getString(R.string.term_condition));
        }

        if (AppUtil.isInternetAvailable(context)) {
            getTextNetworkCall();
        } else {
            AppUtil.showToast(context, getString(R.string.internet_error));
        }
    }

    private void getTextNetworkCall() {
        AppDialogs.getInstance().showProgressBar(context, null, true);
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).getTermAndPrivacyNetworkCall(getIntent().getStringExtra(AppConstant.SCREEN_FLOW_FLAG)), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    if (binding != null && binding.text != null) {
                        ListResponseModel listResponseModel = (ListResponseModel) data;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            binding.text.setText(Html.fromHtml(listResponseModel.getPages().getList().get(0).getText(), Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            binding.text.setText(Html.fromHtml(listResponseModel.getPages().getList().get(0).getText()));
                        }
                    }
                }
            }

            @Override
            public void failed() {
                AppDialogs.getInstance().showProgressBar(context, null, false);
            }
        });
    }

}

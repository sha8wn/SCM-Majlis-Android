package com.nibou.niboucustomer.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.api.ApiClient;
import com.nibou.niboucustomer.api.ApiEndPoint;
import com.nibou.niboucustomer.api.ApiHandler;
import com.nibou.niboucustomer.databinding.ActivityPrivacyPolicyBinding;
import com.nibou.niboucustomer.databinding.ActivityUserCheckBinding;
import com.nibou.niboucustomer.models.ProfileModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;

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
        if (getIntent().getStringExtra("type").equals("privacy")) {
            binding.titleHeader.setText(context.getString(R.string.privacy_policy));
        } else if (getIntent().getStringExtra("type").equals("terms")) {
            binding.titleHeader.setText(context.getString(R.string.term_condition));
        }
    }

    private void getTextNetworkCall() {
        AppDialogs.getInstance().showProgressBar(context, null, true);
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).getTextRequest(LocalPrefences.getInstance().getString(this, AppConstant.APP_LANGUAGE), getIntent().getStringExtra("type")), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    if (binding != null && binding.text != null) {
                        ProfileModel profileModel = (ProfileModel) data;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            binding.text.setText(Html.fromHtml(profileModel.getData().getAttributes().getText(), Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            binding.text.setText(Html.fromHtml(profileModel.getData().getAttributes().getText()));
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

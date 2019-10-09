package com.nibou.niboucustomer.activitys;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.api.ApiClient;
import com.nibou.niboucustomer.api.ApiEndPoint;
import com.nibou.niboucustomer.api.ApiHandler;
import com.nibou.niboucustomer.databinding.ActivityChangePasswordBinding;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;

import java.util.HashMap;

public class ChangePasswordActivity extends BaseActivity {
    private ActivityChangePasswordBinding binding;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        context = this;
        binding.toolbar.findViewById(R.id.back_arrow).setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            onBackPressed();
        });

        binding.btnSave.setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            if (AppUtil.isInternetAvailable(context)) {
                if (screenValidate()) {
                }
            } else {
                AppUtil.showToast(context, getString(R.string.internet_error));
            }
        });

        binding.eyeCurrentPassword.setOnClickListener(v -> {
            if (binding.etCurrentPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                binding.etCurrentPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                binding.eyeCurrentPassword.setImageResource(R.drawable.eye_icon);
            } else {
                binding.eyeCurrentPassword.setImageResource(R.drawable.invisible_eye);
                binding.etCurrentPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
            binding.etCurrentPassword.setSelection(binding.etCurrentPassword.getText().length());
        });


        binding.eyeNewPassword.setOnClickListener(v -> {
            if (binding.etNewPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                binding.etNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                binding.eyeNewPassword.setImageResource(R.drawable.eye_icon);
            } else {
                binding.eyeNewPassword.setImageResource(R.drawable.invisible_eye);
                binding.etNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
            binding.etNewPassword.setSelection(binding.etNewPassword.getText().length());
        });

        binding.eyeRepeatPassword.setOnClickListener(v -> {
            if (binding.etRepeatPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                binding.etRepeatPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                binding.eyeRepeatPassword.setImageResource(R.drawable.eye_icon);
            } else {
                binding.eyeRepeatPassword.setImageResource(R.drawable.invisible_eye);
                binding.etRepeatPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
            binding.etRepeatPassword.setSelection(binding.etRepeatPassword.getText().length());
        });


    }

    private void changePasswordNetworkCall() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("old_password", binding.etCurrentPassword.getText().toString());
        parameters.put("password", binding.etNewPassword.getText().toString());
        parameters.put("password_confirmation", binding.etRepeatPassword.getText().toString());

        AppDialogs.getInstance().showProgressBar(context, null, true);
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).changePassword(LocalPrefences.getInstance().getString(this, AppConstant.APP_LANGUAGE), AppConstant.BEARER + LocalPrefences.getInstance().getLocalAccessTokenModel(context).getAccessToken(), parameters), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    AppDialogs.getInstance().showInfoCustomDialog(context, getString(R.string.success).toUpperCase(), getString(R.string.pwd_change_success_alert), getString(R.string.OK), status -> onBackPressed());
                } else {
                    AppDialogs.getInstance().showInfoCustomDialog(context, getString(R.string.error).toUpperCase(), getString(R.string.pwd_change_failed_alert), getString(R.string.OK), null);
                }
            }

            @Override
            public void failed() {
                AppDialogs.getInstance().showProgressBar(context, null, false);
            }
        });
    }

    private boolean screenValidate() {
        if (TextUtils.isEmpty(binding.etCurrentPassword.getText())) {
            AppUtil.showToast(this, getResources().getString(R.string.currentpwd_empty_alert));
            return false;
        } else if (TextUtils.isEmpty(binding.etNewPassword.getText())) {
            AppUtil.showToast(this, getResources().getString(R.string.newpwd_empty_alert));
            return false;
        } else if (binding.etNewPassword.getText().toString().length() < 6) {
            AppUtil.showToast(this, getResources().getString(R.string.pwd_length_alert));
            return false;
        } else if (TextUtils.isEmpty(binding.etRepeatPassword.getText())) {
            AppUtil.showToast(this, getResources().getString(R.string.repeatpwd_empty_alert));
            return false;
        } else if (!TextUtils.equals(binding.etNewPassword.getText(), binding.etRepeatPassword.getText())) {
            AppUtil.showToast(this, getResources().getString(R.string.pwd_mismatch_alert));
            return false;
        }
        return true;
    }
}

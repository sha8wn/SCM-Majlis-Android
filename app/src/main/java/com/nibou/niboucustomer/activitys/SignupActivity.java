package com.nibou.niboucustomer.activitys;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.iid.FirebaseInstanceId;
import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.actioncable.ActionSessionHandler;
import com.nibou.niboucustomer.api.ApiClient;
import com.nibou.niboucustomer.api.ApiEndPoint;
import com.nibou.niboucustomer.api.ApiHandler;
import com.nibou.niboucustomer.callbacks.AppCallBack;
import com.nibou.niboucustomer.databinding.ActivitySignupBinding;
import com.nibou.niboucustomer.models.AccessTokenModel;
import com.nibou.niboucustomer.models.ListResponseModel;
import com.nibou.niboucustomer.models.ProfileModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.DateFormatUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;

import java.util.Calendar;
import java.util.HashMap;

public class SignupActivity extends BaseActivity {

    private ActivitySignupBinding binding;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        context = this;
        binding.toolbar.findViewById(R.id.back_arrow).setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            onBackPressed();
        });

        if (getIntent().hasExtra(AppConstant.ADMIN_SIGNUP)) {
            binding.tvPassword.setVisibility(View.VISIBLE);
            binding.etPassword.setVisibility(View.VISIBLE);
            binding.tvBrand.setVisibility(View.GONE);
            binding.tvModel.setVisibility(View.GONE);
            binding.etBrand.setVisibility(View.GONE);
            binding.etModel.setVisibility(View.GONE);
            binding.btnSignup.setVisibility(View.GONE);
            binding.btnNext.setVisibility(View.VISIBLE);
            binding.nextTitle.setVisibility(View.VISIBLE);
        } else {
            binding.tvPassword.setVisibility(View.GONE);
            binding.etPassword.setVisibility(View.GONE);
            binding.tvBrand.setVisibility(View.VISIBLE);
            binding.tvModel.setVisibility(View.VISIBLE);
            binding.etBrand.setVisibility(View.VISIBLE);
            binding.etModel.setVisibility(View.VISIBLE);
            binding.btnSignup.setVisibility(View.VISIBLE);
            binding.btnNext.setVisibility(View.GONE);
            binding.nextTitle.setVisibility(View.GONE);
        }

        binding.etBrand.setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            AppDialogs.getInstance().openListDialog(getString(R.string.brand), binding.etBrand.getTag(), context, new AppCallBack() {
                @Override
                public void onSelect(ListResponseModel.ModelList modelList) {
                    if (modelList != null) {
                        binding.etBrand.setTag(modelList);
                        binding.etBrand.setText(modelList.getName());
                    }
                }
            });
        });

        binding.etModel.setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            AppDialogs.getInstance().openListDialog(getString(R.string.model), binding.etModel.getTag(), context, new AppCallBack() {
                @Override
                public void onSelect(ListResponseModel.ModelList modelList) {
                    if (modelList != null) {
                        binding.etModel.setTag(modelList);
                        binding.etModel.setText(modelList.getName());
                    }
                }
            });
        });

        binding.btnSignup.setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            if (AppUtil.isInternetAvailable(context)) {
                if (screenValidate()) {
                    AppDialogs.getInstance().showProgressBar(context, null, true);
//                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
//                        registerUserNetworkCall("testing");
//                    });
                    registerUserNetworkCall("testing");
                }
            } else {
                AppUtil.showToast(context, getString(R.string.internet_error));
            }
        });
        binding.btnNext.setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            Intent intent = new Intent(context, AddSuperCarActivity.class);
            startActivity(intent);
        });

    }

    private void registerUserNetworkCall(String deviceToken) {

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", binding.etName.getText().toString());
        parameters.put("email", binding.etEmail.getText().toString());
        parameters.put("phone", "00971" + binding.etPhone.getText().toString());
        parameters.put("brand", ((ListResponseModel.ModelList) binding.etBrand.getTag()).getId());
        parameters.put("model", ((ListResponseModel.ModelList) binding.etModel.getTag()).getId());
        //parameters.put("uid", deviceToken);

        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).registerUserNetworkCall(parameters), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    AppDialogs.getInstance().showCustomDialog(context, getString(R.string.thank_you),
                            getString(R.string.thank_you_desc), getString(R.string.continu),
                            getResources().getColor(R.color.white), status -> {
                                try {
                                    Intent intent = new Intent(context, PastEventActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
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

    private void profileNetworkCall(AccessTokenModel accessTokenModel) {
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).getMyProfile(LocalPrefences.getInstance().getString(this, AppConstant.APP_LANGUAGE), AppConstant.BEARER + accessTokenModel.getAccessToken(), "languages"), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    LocalPrefences.getInstance().saveLocalAccessTokenModel(context, accessTokenModel);
                    LocalPrefences.getInstance().saveLocalProfileModel(context, (ProfileModel) data);
                    ActionSessionHandler.getActionSessionHandler(context).connectWS();
                    Intent intent = new Intent(context, SignupSuccessActivity.class);
                    intent.putExtra(AppConstant.EMAIL, binding.etEmail.getText().toString());
                    startActivity(intent);
                    finishAffinity();
                } else {
                    AppUtil.showToast(context, context.getResources().getString(R.string.wrong_with_backend));
                }
            }

            @Override
            public void failed() {
                AppDialogs.getInstance().showProgressBar(context, null, false);
            }
        });
    }


    private boolean screenValidate() {
        if (TextUtils.isEmpty(binding.etName.getText())) {
            AppUtil.showToast(this, getResources().getString(R.string.name_empty_alert));
            return false;
        } else if (TextUtils.isEmpty(binding.etEmail.getText())) {
            AppUtil.showToast(this, getResources().getString(R.string.email_empty_alert));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.getText().toString()).matches()) {
            AppUtil.showToast(this, getResources().getString(R.string.email_invalid_alert));
            return false;
        } else if (TextUtils.isEmpty(binding.etPhone.getText())) {
            AppUtil.showToast(this, getResources().getString(R.string.phone_empty_alert));
            return false;
        } else if (TextUtils.isEmpty(binding.etBrand.getText())) {
            AppUtil.showToast(this, getResources().getString(R.string.brand_empty_alert));
            return false;
        } else if (TextUtils.isEmpty(binding.etModel.getText())) {
            AppUtil.showToast(this, getResources().getString(R.string.model_empty_alert));
            return false;
        }
        return true;
    }
}

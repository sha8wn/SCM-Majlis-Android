package com.nibou.niboucustomer.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.api.ApiClient;
import com.nibou.niboucustomer.api.ApiEndPoint;
import com.nibou.niboucustomer.api.ApiHandler;
import com.nibou.niboucustomer.application.SCMApplication;
import com.nibou.niboucustomer.callbacks.AppCallBack;
import com.nibou.niboucustomer.databinding.ActivitySignupBinding;
import com.nibou.niboucustomer.models.ErrorResponseModel;
import com.nibou.niboucustomer.models.ListResponseModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;

import java.util.HashMap;

public class SignupActivity extends BaseActivity {

    private ActivitySignupBinding binding;
    private Context context;
    private String userId;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SCMApplication)getApplication()).getFirebaseAnalytics().logEvent(AppConstant.REGISTER_EVENT, new Bundle());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        context = this;
        binding.toolbar.findViewById(R.id.back_arrow).setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            onBackPressed();
        });

        if (getIntent().getData() != null && getIntent().getData().getQueryParameterNames() != null) {
            token = getIntent().getData().getQuery().replace("token=", "");
            getIntent().putExtra(AppConstant.ADMIN_SIGNUP, true);

            if (AppUtil.isInternetAvailable(context)) {
                getUserDetailsNetworkCall();
            } else {
                AppUtil.showToast(context, getString(R.string.internet_error));
            }
        }

        if (getIntent().hasExtra(AppConstant.ADMIN_SIGNUP)) {
            binding.toolbar.findViewById(R.id.back_arrow).setVisibility(View.INVISIBLE);
            binding.tvPassword.setVisibility(View.VISIBLE);
            binding.etPassword.setVisibility(View.VISIBLE);
            binding.tvBrand.setVisibility(View.GONE);
            binding.tvModel.setVisibility(View.GONE);
            binding.etBrand.setVisibility(View.GONE);
            binding.etModel.setVisibility(View.GONE);
            binding.btnSignup.setVisibility(View.GONE);
            binding.btnNext.setVisibility(View.VISIBLE);
            binding.nextTitle.setVisibility(View.VISIBLE);

            if (getIntent().hasExtra(AppConstant.NORMAL_SIGNUP)) {
                binding.toolbar.findViewById(R.id.back_arrow).setVisibility(View.VISIBLE);
                binding.signupTitle.setText(getString(R.string.new_user));
                binding.tvPassword.setVisibility(View.GONE);
                binding.etPassword.setVisibility(View.GONE);
            }
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
            AppDialogs.getInstance().openListDialog(getString(R.string.brand), binding.etBrand.getTag(), null, context, new AppCallBack() {
                @Override
                public void onSelect(ListResponseModel.ModelList modelList) {
                    if (modelList != null) {
                        binding.etBrand.setTag(modelList.getId());
                        binding.etBrand.setText(modelList.getName());

                        binding.etModel.setTag(null);
                        binding.etModel.setText("");
                    }
                }
            });
        });

        binding.etModel.setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            if (binding.etBrand.getText().toString().equals("")) {
                AppUtil.showToast(context, getResources().getString(R.string.brand_first_empty_alert));
            } else {
                AppDialogs.getInstance().openListDialog(getString(R.string.model), binding.etModel.getTag(), binding.etBrand.getTag(), context, new AppCallBack() {
                    @Override
                    public void onSelect(ListResponseModel.ModelList modelList) {
                        if (modelList != null) {
                            binding.etModel.setTag(modelList.getId());
                            binding.etModel.setText(modelList.getName());
                        }
                    }
                });
            }
        });

        binding.btnSignup.setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            if (AppUtil.isInternetAvailable(context)) {
                if (screenValidate()) {
                    AppDialogs.getInstance().showProgressBar(context, null, true);
                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
                        registerUserNetworkCall(instanceIdResult.getToken());
                    });
                }
            } else {
                AppUtil.showToast(context, getString(R.string.internet_error));
            }
        });
        binding.btnNext.setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            if (getIntent().hasExtra(AppConstant.NORMAL_SIGNUP)) {
                if (AppUtil.isInternetAvailable(context)) {
                    if (screenValidate()) {
                        AppDialogs.getInstance().showProgressBar(context, null, true);
                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
                            registerUserNetworkCall(instanceIdResult.getToken());
                        });
                    }
                } else {
                    AppUtil.showToast(context, getString(R.string.internet_error));
                }
            } else {
                if (AppUtil.isInternetAvailable(context)) {
                    if (screenAdminValidate()) {
                        saveUserPasswordNetworkCall();
                    }
                } else {
                    AppUtil.showToast(context, getString(R.string.internet_error));
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra(AppConstant.ADMIN_SIGNUP)) {
            if (getIntent().hasExtra(AppConstant.NORMAL_SIGNUP)) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void showAdminApproveUserDetails(ListResponseModel.ModelList user) {
        binding.etName.setText(user.getName());
        binding.etEmail.setText(user.getEmail());
        binding.etPhone.setText(user.getPhone().substring(5, user.getPhone().length()));
        userId = user.getId();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getStringExtra(AppConstant.REFRESH_TOKEN) != null) {
            token = data.getStringExtra(AppConstant.REFRESH_TOKEN);
        }
    }

    private void getUserDetailsNetworkCall() {
        AppDialogs.getInstance().showProgressBar(context, null, true);
        ApiHandler.requestLinkService(context, ApiClient.getClient().create(ApiEndPoint.class).getUserDetailsNetworkCall(token), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    ListResponseModel listResponseModel = (ListResponseModel) data;
                    if (listResponseModel.getUsers().getList() != null && listResponseModel.getUsers().getList().size() > 0)
                        showAdminApproveUserDetails(listResponseModel.getUsers().getList().get(0));
                } else {
                    AppDialogs.getInstance().showCustomDialog(context, getString(R.string.error).toUpperCase(), getString(R.string.link_expired), getString(R.string.OK), getResources().getColor(R.color.colorPrimary), status -> {
                        try {
                            Intent intent = new Intent(context, UserCheckActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void failed() {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                finish();
            }
        });
    }

    private void saveUserPasswordNetworkCall() {

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", binding.etName.getText().toString().trim());
        parameters.put("email", binding.etEmail.getText().toString().trim());
        parameters.put("phone", "00971" + binding.etPhone.getText().toString().trim());
        parameters.put("password", binding.etPassword.getText().toString());
        AppDialogs.getInstance().showProgressBar(context, null, true);
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).updateUserDetailNetworkCall(userId, token, parameters), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    LocalPrefences.getInstance().putString(context, AppConstant.TOKEN, ((ListResponseModel) data).getToken());
                    LocalPrefences.getInstance().putString(context, AppConstant.PASSWORD, binding.etPassword.getText().toString());
                    Intent intent = new Intent(context, AddSuperCarActivity.class);
                    intent.putExtra(AppConstant.ADMIN_SIGNUP, true);
                    startActivityForResult(intent, 100);
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

    private void registerUserNetworkCall(String deviceToken) {

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", binding.etName.getText().toString());
        parameters.put("email", binding.etEmail.getText().toString());
        parameters.put("phone", "00971" + binding.etPhone.getText().toString());
        parameters.put("uid", deviceToken);

        // parameters.put("brand", binding.etBrand.getTag());
        // parameters.put("model", binding.etModel.getTag());

        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).registerUserNetworkCall(parameters), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    LocalPrefences.getInstance().putString(context, AppConstant.TOKEN, ((ErrorResponseModel) data).getToken());
                    Intent intent = new Intent(context, AddSuperCarActivity.class);
                    intent.putExtra(AppConstant.ADMIN_SIGNUP, true);
                    intent.putExtra(AppConstant.NORMAL_SIGNUP, true);
                    startActivity(intent);
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
        if (TextUtils.isEmpty(binding.etName.getText())) {
            AppUtil.showToast(context, getResources().getString(R.string.name_empty_alert));
            return false;
        } else if (TextUtils.isEmpty(binding.etEmail.getText())) {
            AppUtil.showToast(context, getResources().getString(R.string.email_empty_alert));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.getText().toString()).matches()) {
            AppUtil.showToast(context, getResources().getString(R.string.email_invalid_alert));
            return false;
        } else if (TextUtils.isEmpty(binding.etPhone.getText())) {
            AppUtil.showToast(context, getResources().getString(R.string.phone_empty_alert));
            return false;
        }
//        else if (TextUtils.isEmpty(binding.etBrand.getText())) {
//            AppUtil.showToast(context, getResources().getString(R.string.brand_empty_alert));
//            return false;
//        } else if (TextUtils.isEmpty(binding.etModel.getText())) {
//            AppUtil.showToast(context, getResources().getString(R.string.model_empty_alert));
//            return false;
//        }
        return true;
    }

    private boolean screenAdminValidate() {
        if (TextUtils.isEmpty(binding.etName.getText())) {
            AppUtil.showToast(context, getResources().getString(R.string.name_empty_alert));
            return false;
        } else if (TextUtils.isEmpty(binding.etEmail.getText())) {
            AppUtil.showToast(context, getResources().getString(R.string.email_empty_alert));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.getText().toString()).matches()) {
            AppUtil.showToast(context, getResources().getString(R.string.email_invalid_alert));
            return false;
        } else if (TextUtils.isEmpty(binding.etPassword.getText())) {
            AppUtil.showToast(context, getResources().getString(R.string.password_empty_alert));
            return false;
        } else if (TextUtils.isEmpty(binding.etPhone.getText())) {
            AppUtil.showToast(context, getResources().getString(R.string.phone_empty_alert));
            return false;
        }
        return true;
    }
}

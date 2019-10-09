package com.nibou.niboucustomer.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.api.ApiClient;
import com.nibou.niboucustomer.api.ApiEndPoint;
import com.nibou.niboucustomer.api.ApiHandler;
import com.nibou.niboucustomer.databinding.ActivityEditProfileBinding;
import com.nibou.niboucustomer.databinding.ActivityPaymentCardBinding;
import com.nibou.niboucustomer.databinding.FragmentProfileBinding;
import com.nibou.niboucustomer.models.AccessTokenModel;
import com.nibou.niboucustomer.models.ProfileModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import java.util.HashMap;

public class EditProfileActivity extends BaseActivity {
    private FragmentProfileBinding binding;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_profile);
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
    }



    private void updateProfileNetworkCall() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", binding.etName.getText().toString());
        parameters.put("country", binding.etCountry.getText().toString());

        AppDialogs.getInstance().showProgressBar(context, null, true);
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).updateMyProfile(LocalPrefences.getInstance().getString(this, AppConstant.APP_LANGUAGE), AppConstant.BEARER + LocalPrefences.getInstance().getLocalAccessTokenModel(context).getAccessToken(), parameters), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    LocalPrefences.getInstance().saveLocalProfileModel(context, (ProfileModel) data);
                    AppDialogs.getInstance().showInfoCustomDialog(context, getString(R.string.success).toUpperCase(), getString(R.string.profile_change_success_alert), getString(R.string.OK), status -> onBackPressed());
                } else {
                    AppDialogs.getInstance().showInfoCustomDialog(context, getString(R.string.error).toUpperCase(), getString(R.string.wrong_with_backend), getString(R.string.OK), null);
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
        } else if (TextUtils.isEmpty(binding.etCountry.getText())) {
            AppUtil.showToast(context, getResources().getString(R.string.country_empty_alert));
            return false;
        }
        return true;
    }
}

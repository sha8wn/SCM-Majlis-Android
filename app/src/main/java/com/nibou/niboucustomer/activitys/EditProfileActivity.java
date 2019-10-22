package com.nibou.niboucustomer.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.model.Image;
import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.api.ApiClient;
import com.nibou.niboucustomer.api.ApiEndPoint;
import com.nibou.niboucustomer.api.ApiHandler;
import com.nibou.niboucustomer.databinding.ActivityEditProfileBinding;
import com.nibou.niboucustomer.databinding.ActivityPaymentCardBinding;
import com.nibou.niboucustomer.databinding.FragmentProfileBinding;
import com.nibou.niboucustomer.models.AccessTokenModel;
import com.nibou.niboucustomer.models.ListResponseModel;
import com.nibou.niboucustomer.models.ProfileModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.DateFormatUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;
import com.nibou.niboucustomer.utils.MediaUtil;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import java.util.ArrayList;
import java.util.HashMap;

public class EditProfileActivity extends BaseActivity {
    private FragmentProfileBinding binding;
    private Context context;
    private MediaUtil mediaUtil;
    private String profileLocalPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_profile);
        context = this;
        mediaUtil = new MediaUtil(this);
        binding.toolbar.findViewById(R.id.back_arrow).setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            onBackPressed();
        });

        if (AppUtil.isInternetAvailable(context)) {
            getUserDetailsNetworkCall();
        } else {
            AppUtil.showToast(context, getString(R.string.internet_error));
        }


        binding.btnSave.setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            if (AppUtil.isInternetAvailable(context)) {
                if (screenValidate()) {
                    updateProfileNetworkCall();
                }
            } else {
                AppUtil.showToast(context, getString(R.string.internet_error));
            }
        });


        binding.ivProfile.setOnClickListener(view -> {
            mediaUtil.openSingleImageGallery((isCamera, imageResult) -> {
                ArrayList<Image> imagesList = (ArrayList<Image>) imageResult;
                if (imagesList != null && imagesList.size() > 0) {
                    mediaUtil.compressMultipleImages(imagesList, AppConstant.COMPRESSION_RATIO, (bitmap, fileName, path) -> {
                        if (path != null) {
                            ((Activity) context).runOnUiThread(() -> {
                                profileLocalPath = path;
                                loadImage(path);
                            });
                        }
                    });
                }
            });
        });
    }

    private void showData(ListResponseModel.ModelList modelList) {
        binding.etName.setText(modelList.getName());
        binding.etEmail.setText(modelList.getEmail());
        if (modelList.getPhone() != null && !modelList.getPhone().isEmpty())
            binding.etPhone.setText(modelList.getPhone().substring(5, modelList.getPhone().length()));
        loadImage(modelList.getImg());
    }

    private void loadImage(String url) {
        if (url != null && !url.isEmpty())
            Glide.with(context).load(url).centerCrop().placeholder(R.drawable.place_holder).error(R.drawable.place_holder).into(binding.ivProfile);
    }


    private void getUserDetailsNetworkCall() {
        AppDialogs.getInstance().showProgressBar(context, null, true);
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).getUserDetailsNetworkCall(LocalPrefences.getInstance().getString(context, AppConstant.TOKEN)), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    ListResponseModel listResponseModel = (ListResponseModel) data;
                    if (listResponseModel.getUsers().getList() != null && listResponseModel.getUsers().getList().size() > 0) {
                        showData(listResponseModel.getUsers().getList().get(0));
                    }
                }
            }

            @Override
            public void failed() {
                AppDialogs.getInstance().showProgressBar(context, null, false);
            }
        });
    }


    private void updateProfileNetworkCall() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", binding.etName.getText().toString().trim());
        parameters.put("email", binding.etEmail.getText().toString().trim());
        parameters.put("phone", "00971" + binding.etPhone.getText().toString().trim());

        if (profileLocalPath != null)
            parameters.put("img_add", MediaUtil.getBase64FromPath(profileLocalPath));

        AppDialogs.getInstance().showProgressBar(context, null, true);
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).updateProfileNetworkCall(LocalPrefences.getInstance().getString(context, AppConstant.TOKEN), LocalPrefences.getInstance().getString(context, AppConstant.USER_ID), parameters), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    AppDialogs.getInstance().showCustomDialog(context, getString(R.string.success).toUpperCase(), getString(R.string.profile_change_success_alert), getString(R.string.OK), getResources().getColor(R.color.green), status -> onBackPressed());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mediaUtil != null)
            mediaUtil.onActivityResult(requestCode, resultCode, data);
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
        return true;
    }
}

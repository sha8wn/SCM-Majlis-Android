package com.nibou.niboucustomer.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.adapters.AddSuperCarAdapter;
import com.nibou.niboucustomer.adapters.EventDetailsAdapter;
import com.nibou.niboucustomer.adapters.RSVPCarAdapter;
import com.nibou.niboucustomer.api.ApiClient;
import com.nibou.niboucustomer.api.ApiEndPoint;
import com.nibou.niboucustomer.api.ApiHandler;
import com.nibou.niboucustomer.databinding.ActivityEventDetailsBinding;
import com.nibou.niboucustomer.databinding.ActivityReserveSpotBinding;
import com.nibou.niboucustomer.models.ErrorResponseModel;
import com.nibou.niboucustomer.models.ListResponseModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;

import java.util.HashMap;

public class RSVPSpotActivity extends BaseActivity {
    private ActivityReserveSpotBinding binding;
    private Context context;

    private ListResponseModel.ModelList modelList;
    private String selectedCarId;
    private String guest = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reserve_spot);
        context = this;
        binding.toolbar.findViewById(R.id.back_arrow).setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            onBackPressed();
        });

        binding.revpMember.setOnClickListener(v -> {
            binding.revpMember.setAlpha(1);
            binding.revpGuest.setAlpha(0.5f);
            binding.revpMember.setBackground(ContextCompat.getDrawable(context, R.drawable.rsvp_red_corner_drawable));
            binding.revpGuest.setBackground(null);
            guest = "1";
        });

        binding.revpGuest.setOnClickListener(v -> {
            binding.revpMember.setAlpha(0.5f);
            binding.revpGuest.setAlpha(1);
            binding.revpGuest.setBackground(ContextCompat.getDrawable(context, R.drawable.rsvp_red_guest_corner_drawable));
            binding.revpMember.setBackground(null);
            guest = "2";
        });

        binding.btnReserveSpot.setOnClickListener(v -> {
            if (AppUtil.isInternetAvailable(context)) {
                if (screenValidate())
                    reserveSpotNetworkCall();
            } else {
                AppUtil.showToast(context, getString(R.string.internet_error));
            }
        });

        modelList = (ListResponseModel.ModelList) getIntent().getSerializableExtra(AppConstant.EVENT_MODEL);
        binding.tvTitle.setText(modelList.getName());


        if (AppUtil.isInternetAvailable(context)) {
            getCarsDetailsNetworkCall();
        } else {
            AppUtil.showToast(context, getString(R.string.internet_error));
        }
    }

    public void carSelectedCallBack(boolean isSelected, String selectedCarId) {
        this.selectedCarId = selectedCarId;
        if (isSelected) {
            binding.btnReserveSpot.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_gradient));
        } else {
            binding.btnReserveSpot.setBackground(ContextCompat.getDrawable(context, R.drawable.grey_btn_gradient));
        }
    }

    private void reserveSpotNetworkCall() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("car", selectedCarId);
        parameters.put("event", modelList.getId());
        parameters.put("guests", guest);

        AppDialogs.getInstance().showProgressBar(context, null, true);
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).reserveSpotNetworkCall(LocalPrefences.getInstance().getString(context, AppConstant.TOKEN), parameters), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    AppDialogs.getInstance().showCustomDialog(context, getString(R.string.success).toUpperCase(), getString(R.string.reserve_spot_alert) + " " + modelList.getName(), getString(R.string.OK), getResources().getColor(R.color.green), status -> {
                        setResult(RESULT_OK, new Intent());
                        finish();
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


    private void getCarsDetailsNetworkCall() {
        AppDialogs.getInstance().showProgressBar(context, null, true);
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).getCarsDetailsNetworkCall(LocalPrefences.getInstance().getString(context, AppConstant.TOKEN)), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    ListResponseModel listResponseModel = (ListResponseModel) data;
                    if (listResponseModel.getCars() != null && listResponseModel.getCars().getList() != null && listResponseModel.getCars().getList().size() > 0) {
                        binding.rvList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        binding.rvList.setAdapter(new RSVPCarAdapter(context, listResponseModel.getCars().getList()));
                        carSelectedCallBack(true, listResponseModel.getCars().getList().get(0).getId());
                    }
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
        if (selectedCarId == null) {
            AppUtil.showToast(this, getResources().getString(R.string.car_select_error));
            return false;
        }
        return true;
    }

}

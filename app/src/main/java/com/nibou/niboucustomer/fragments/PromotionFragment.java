package com.nibou.niboucustomer.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.activitys.RedeemActivity;
import com.nibou.niboucustomer.adapters.EventAdapter;
import com.nibou.niboucustomer.adapters.PromotionAdapter;
import com.nibou.niboucustomer.api.ApiClient;
import com.nibou.niboucustomer.api.ApiEndPoint;
import com.nibou.niboucustomer.api.ApiHandler;
import com.nibou.niboucustomer.application.SCMApplication;
import com.nibou.niboucustomer.callbacks.AppCallBack;
import com.nibou.niboucustomer.databinding.FragmentPromotionBinding;
import com.nibou.niboucustomer.models.ListResponseModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;

public class PromotionFragment extends Fragment implements AppCallBack {

    private FragmentPromotionBinding binding;
    private Context context;
    private PromotionAdapter promotionAdapter;
    private Dialog dialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_promotion, container, false);
        context = getActivity();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((SCMApplication) getActivity().getApplication()).getFirebaseAnalytics().logEvent(AppConstant.PROMOTION_EVENT, new Bundle());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dialog == null || !dialog.isShowing()) {
            if (AppUtil.isInternetAvailable(context)) {
                getPromotionsNetworkCall(1);
            } else {
                AppUtil.showToast(context, getString(R.string.internet_error));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            if (dialog != null)
                dialog.cancel();
        }
    }

    public void getPromotionsNetworkCall(int pageNumber) {
        AppDialogs.getInstance().showProgressBar(context, null, true);
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).getPromotionNetworkCall(LocalPrefences.getInstance().getString(context, AppConstant.TOKEN), 20, pageNumber, 1), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    ListResponseModel listResponseModel = (ListResponseModel) data;
                    if (listResponseModel.getPromotions() != null && listResponseModel.getPromotions().getList() != null && listResponseModel.getPromotions().getList().size() > 0) {
                        binding.rvPromotion.setLayoutManager(new LinearLayoutManager(getActivity()));
                        promotionAdapter = new PromotionAdapter(getActivity(), listResponseModel.getPromotions().getList(), PromotionFragment.this);
                        binding.rvPromotion.setAdapter(promotionAdapter);
                        binding.errorMessage.setVisibility(View.GONE);
                    } else {
                        binding.errorMessage.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.errorMessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failed() {
                AppDialogs.getInstance().showProgressBar(context, null, false);
            }
        });
    }

    @Override
    public void onSelect(ListResponseModel.ModelList modelList) {
        dialog = new Dialog(context, R.style.FullScreenDialogStyle);
        dialog.setContentView(R.layout.dialog_promotion_details);
        dialog.findViewById(R.id.back_arrow).setOnClickListener(view -> dialog.dismiss());
        dialog.findViewById(R.id.redeem).setOnClickListener(view -> {
            try {
                Intent intent = new Intent(context, RedeemActivity.class);
                intent.putExtra(AppConstant.PROMOTION_ID, modelList.getId());
                startActivityForResult(intent, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        final TextView label = dialog.findViewById(R.id.label);
        label.setText(modelList.getPartner_name());
        TextView des = dialog.findViewById(R.id.tvDesc);
        des.setText(modelList.getName());
        TextView location = dialog.findViewById(R.id.location);
        location.setText(modelList.getPartner_location());
        TextView number = dialog.findViewById(R.id.number);
        number.setText(modelList.getPartner_phone());
        ImageView icon = dialog.findViewById(R.id.icon);
        loadImage(icon, modelList.getPartner_img());
        TextView text = dialog.findViewById(R.id.text);
        text.setText(modelList.getText());
        TextView term_text = dialog.findViewById(R.id.term_text);
        term_text.setText(modelList.getTerms());


        final TextView center_label = dialog.findViewById(R.id.center_label);
        center_label.setAlpha(0f);
        center_label.setText(modelList.getPartner_name());
        dialog.findViewById(R.id.scrollView).getViewTreeObserver().addOnScrollChangedListener(() -> {
            int scrollY = dialog.findViewById(R.id.scrollView).getScrollY();
            if (scrollY > 100) {
                center_label.animate().alpha(1f).setDuration(400);
            } else {
                center_label.animate().alpha(0f).setDuration(100);
            }
        });

        dialog.show();
    }

    private void loadImage(ImageView imageView, String url) {
        if (url != null && !url.isEmpty())
            Glide.with(context).load(url).dontAnimate().centerCrop().placeholder(R.drawable.default_placeholder).error(R.drawable.default_placeholder).into(imageView);
    }

}
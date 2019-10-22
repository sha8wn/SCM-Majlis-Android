package com.nibou.niboucustomer.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.*;

import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.actioncable.ActionSessionHandler;
import com.nibou.niboucustomer.activitys.*;
import com.nibou.niboucustomer.adapters.ChatAdapter;
import com.nibou.niboucustomer.api.ApiClient;
import com.nibou.niboucustomer.api.ApiEndPoint;
import com.nibou.niboucustomer.api.ApiHandler;
import com.nibou.niboucustomer.databinding.FragmentSettingBinding;
import com.nibou.niboucustomer.models.AccessTokenModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;
import com.nibou.niboucustomer.utils.MediaUtil;

import java.util.HashMap;
import java.util.Locale;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;

    public SettingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        binding.logout.setOnClickListener(v -> {
            AppDialogs.getInstance().showConfirmCustomDialog(getActivity(), getActivity().getString(R.string.logout), getActivity().getString(R.string.logout_message), getActivity().getString(R.string.CANCEL).toUpperCase(), getActivity().getString(R.string.OK).toUpperCase(), getResources().getColor(R.color.white), new AppDialogs.DialogCallback() {
                @Override
                public void response(boolean status) {
                    if (status) {
                        clearLocalData();
                    }
                }
            });
        });

        binding.updateProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        binding.changePwd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
        });

        binding.maneSupercars.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddSuperCarActivity.class);
            startActivity(intent);
        });

        binding.manageDocuments.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DocumentActivity.class);
            startActivity(intent);
        });

        binding.tnc.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PrivacyPolicyActivity.class);
            intent.putExtra(AppConstant.SCREEN_FLOW_FLAG, AppConstant.TEXT_TERMS);
            startActivity(intent);
        });

        binding.socialMedia.setOnClickListener(v -> {
            try {
                startActivity(getActivity().getPackageManager().getLaunchIntentForPackage("com.instagram.android"));
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/supercarsmajlis/?hl=en")));
            }
        });
    }

    private void clearLocalData() {
        ActionSessionHandler.getActionSessionHandler(getActivity()).disconnectWS();
        String language = LocalPrefences.getInstance().getString(getActivity(), AppConstant.APP_LANGUAGE);
        boolean firstLaunch = LocalPrefences.getInstance().getBoolean(getActivity(), AppConstant.IS_FIRST_LAUNCH_SUCCESS);
        LocalPrefences.getInstance().clearSharePreference(getActivity());
        LocalPrefences.getInstance().putString(getActivity(), AppConstant.APP_LANGUAGE, language);
        LocalPrefences.getInstance().putBoolean(getActivity(), AppConstant.IS_FIRST_LAUNCH_SUCCESS, firstLaunch);
        Intent intent = new Intent(getActivity(), UserCheckActivity.class);
        startActivity(intent);
        getActivity().finishAffinity();
    }
}

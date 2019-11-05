package com.nibou.niboucustomer.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.api.ApiClient;
import com.nibou.niboucustomer.api.ApiEndPoint;
import com.nibou.niboucustomer.api.ApiHandler;
import com.nibou.niboucustomer.databinding.ActivityPrivacyPolicyBinding;
import com.nibou.niboucustomer.databinding.ActivityRedeemBinding;
import com.nibou.niboucustomer.models.ListResponseModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;

import java.util.HashMap;

public class RedeemActivity extends BaseActivity implements TextWatcher, TextView.OnEditorActionListener, View.OnKeyListener {

    private ActivityRedeemBinding binding;
    private Context context;
    private TextView one, two, three, four;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_redeem);
        context = this;
        binding.toolbar.findViewById(R.id.back_arrow).setVisibility(View.INVISIBLE);
        binding.toolbar.findViewById(R.id.close_icon).setVisibility(View.VISIBLE);
        binding.toolbar.findViewById(R.id.close_icon).setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            onBackPressed();
        });

        registerListener();
    }

    private void registerListener() {

        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);

        one.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        two.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        three.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        four.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        one.requestFocus();

        one.addTextChangedListener(this);
        two.addTextChangedListener(this);
        three.addTextChangedListener(this);
        four.addTextChangedListener(this);
        one.setOnEditorActionListener(this);
        two.setOnEditorActionListener(this);
        three.setOnEditorActionListener(this);
        four.setOnEditorActionListener(this);
        one.setOnKeyListener(this);
        two.setOnKeyListener(this);
        three.setOnKeyListener(this);
        four.setOnKeyListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        setFocus();
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
            redeemNetworkCall();
        }
        return false;
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            handleBackPressed();
        }
        return false;
    }

    private void setFocus() {
        if (one.getText().toString().trim().length() == 0) {
            one.requestFocus();
            return;
        } else if (two.getText().toString().trim().length() == 0) {
            two.requestFocus();
            return;
        } else if (three.getText().toString().trim().length() == 0) {
            three.requestFocus();
            return;
        } else if (four.getText().toString().trim().length() == 0) {
            four.requestFocus();
            return;
        }
        redeemNetworkCall();
    }

    private void handleBackPressed() {
        if (!three.getText().toString().trim().isEmpty()) {
            three.requestFocus();
            three.setText("");
        } else if (!two.getText().toString().trim().isEmpty()) {
            two.requestFocus();
            two.setText("");
            three.setText("");
        } else if (!one.getText().toString().trim().isEmpty()) {
            one.requestFocus();
            two.setText("");
            three.setText("");
            one.setText("");
        }
    }

    private void redeemNetworkCall() {
        if (one.getText().toString().trim().length() == 0 || two.getText().toString().trim().length() == 0 || three.getText().toString().trim().length() == 0 || four.getText().toString().trim().length() == 0) {
            AppUtil.showToast(this, getResources().getString(R.string.redeem_code_alert));
        } else {
            if (AppUtil.isInternetAvailable(context)) {
                AppDialogs.getInstance().showProgressBar(context, null, true);
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("promotion", getIntent().getStringExtra(AppConstant.PROMOTION_ID));
                parameters.put("code", one.getText().toString() + two.getText().toString() + three.getText().toString() + four.getText().toString());
                ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).redeemNetworkCall(LocalPrefences.getInstance().getString(context, AppConstant.TOKEN), parameters), new ApiHandler.CallBack() {
                    @Override
                    public void success(boolean isSuccess, Object data) {
                        AppDialogs.getInstance().showProgressBar(context, null, false);
                        if (isSuccess) {
                            AppDialogs.getInstance().showCustomDialog(context, getString(R.string.success).toUpperCase(), getString(R.string.redeem_success_alert), getString(R.string.continu), getResources().getColor(R.color.green), status -> {
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                            });
                        } else {
                            AppDialogs.getInstance().showCustomDialog(context, getString(R.string.error).toUpperCase(), getString(R.string.redeem_failed_alert), getString(R.string.OK), getResources().getColor(R.color.colorPrimary), status -> finish());
                        }
                    }

                    @Override
                    public void failed() {
                        AppDialogs.getInstance().showProgressBar(context, null, false);
                    }
                });
            } else {
                AppUtil.showToast(context, getString(R.string.internet_error));
            }
        }
    }
}

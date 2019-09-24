package com.nibou.niboucustomer.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.adapters.SurveyAdapter;
import com.nibou.niboucustomer.api.ApiClient;
import com.nibou.niboucustomer.api.ApiEndPoint;
import com.nibou.niboucustomer.api.ApiHandler;
import com.nibou.niboucustomer.databinding.ActivitySurveyBinding;
import com.nibou.niboucustomer.models.ProfileModel;
import com.nibou.niboucustomer.models.SurveyModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;

import java.util.ArrayList;

public class SurveyActivity extends BaseActivity {

    private ActivitySurveyBinding binding;
    private Context context;
    private SurveyModel surveyModel;
    private SurveyAdapter surveyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_survey);
        context = this;
        binding.btnContinue.setVisibility(View.GONE);
        binding.toolbar.findViewById(R.id.back_arrow).setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            onBackPressed();
        });

        if (getIntent().hasExtra("login")) {
            binding.toolbar.findViewById(R.id.back_arrow).setVisibility(View.INVISIBLE);
        }

        binding.btnContinue.setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            if (surveyAdapter.isOptionsSelected()) {
                Intent intent = new Intent(context, SurveyPreferenceActivity.class);
                if (getIntent().getExtras() != null) {
                    intent.putExtras(getIntent().getExtras());
                }
                intent.putExtra(AppConstant.SURVEY_MODEL, surveyModel);
                startActivity(intent);
            } else {
                AppUtil.showToast(context, getString(R.string.survey_selection_alert));
            }
        });

        if (AppUtil.isInternetAvailable(context)) {
            getSurveyNetworkCall();
        } else {
            AppUtil.showToast(context, getString(R.string.internet_error));
        }
    }

    private void setSurveyAdapter() {
        binding.recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        surveyAdapter = new SurveyAdapter(context, surveyModel);
        binding.recyclerview.setAdapter(surveyAdapter);
    }

    public void showContinueButton(boolean isShow) {
        if (isShow) {
            binding.btnContinue.setVisibility(View.VISIBLE);
        } else {
            binding.btnContinue.setVisibility(View.GONE);
        }
    }

    private void getSurveyNetworkCall() {
        AppDialogs.getInstance().showProgressBar(context, null, true);
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).getExpertise(LocalPrefences.getInstance().getString(this, AppConstant.APP_LANGUAGE), AppConstant.BEARER + LocalPrefences.getInstance().getLocalAccessTokenModel(context).getAccessToken()), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    surveyModel = (SurveyModel) data;
                    setSurveyAdapter();
                }
            }

            @Override
            public void failed() {
                AppDialogs.getInstance().showProgressBar(context, null, false);
            }
        });
    }
}

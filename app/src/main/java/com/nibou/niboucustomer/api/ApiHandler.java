package com.nibou.niboucustomer.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.activitys.LoginActivity;
import com.nibou.niboucustomer.models.AccessTokenModel;
import com.nibou.niboucustomer.models.ErrorResponseModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ApiHandler {

    public static boolean SUCCESS = true;
    public static boolean FAILED = false;

    public interface CallBack {
        void success(boolean isSucess, Object data);

        void failed();
    }

    public static <T> void requestService(Context context, final Call<T> requestCall, CallBack callBack) {
        requestCall.clone().enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                try {
                    ErrorResponseModel errorResponseModel = (ErrorResponseModel) response.body();
                    if (errorResponseModel.getError() == 0) {
                        if (callBack != null) {
                            callBack.success(SUCCESS, response.body());
                        }
                    } else {
                        if (callBack != null) {
                            callBack.success(FAILED, errorResponseModel.getError_text());
                        }
                    }
                } catch (Exception e) {
                    AppUtil.showToast(context, context.getResources().getString(R.string.wrong_with_backend));
                    if (callBack != null) {
                        callBack.failed();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                if (t instanceof TimeoutException) {
                    AppUtil.showToast(context, context.getResources().getString(R.string.timeout));
                } else {
                    AppUtil.showToast(context, context.getResources().getString(R.string.server_faliure));
                }
                if (callBack != null) {
                    callBack.failed();
                }
            }
        });
    }

    public static <T> void requestRefreshTokenService(Context context, CallBack callBack, Call lastApiCall, Call<T> call) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                if (response != null && (response.code() == 200 || response.code() == 201)) { //success
                    LocalPrefences.getInstance().saveLocalAccessTokenModel(context, (AccessTokenModel) response.body());
                    requestService(context, lastApiCall, callBack);
                } else if (response != null && response.code() == 400) { //success
                    AppUtil.showToast(context, context.getResources().getString(R.string.refresh_token_error_alert));
                    logout(context);
                } else {
                    if (callBack != null) {
                        AppUtil.showToast(context, context.getResources().getString(R.string.wrong_with_backend));
                        callBack.failed();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                if (t instanceof TimeoutException) {
                    AppUtil.showToast(context, context.getResources().getString(R.string.timeout));
                } else {
                    AppUtil.showToast(context, context.getResources().getString(R.string.server_faliure));
                }
                if (callBack != null) {
                    callBack.failed();
                }
            }
        });
    }

    private static void logout(Context context) {
        String language = LocalPrefences.getInstance().getString(context, AppConstant.APP_LANGUAGE);
        boolean firstLaunch = LocalPrefences.getInstance().getBoolean(context, AppConstant.IS_FIRST_LAUNCH_SUCCESS);
        LocalPrefences.getInstance().clearSharePreference(context);
        LocalPrefences.getInstance().putString(context, AppConstant.APP_LANGUAGE, language);
        LocalPrefences.getInstance().putBoolean(context, AppConstant.IS_FIRST_LAUNCH_SUCCESS, firstLaunch);
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        ((Activity) context).finishAffinity();
    }

}

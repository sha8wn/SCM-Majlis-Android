package com.nibou.niboucustomer.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.activitys.UserCheckActivity;
import com.nibou.niboucustomer.models.ErrorResponseModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;

import org.apache.http.conn.ConnectTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.SocketTimeoutException;
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
                    } else if (errorResponseModel.getError() == 11 || errorResponseModel.getError() == 12) {
                        if (callBack != null) {
                            callBack.failed();
                        }
                        AppUtil.showToast(context, errorResponseModel.getError_text());
                        logout(context);
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
                if (t instanceof TimeoutException || t instanceof SocketTimeoutException || t instanceof ConnectTimeoutException) {
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


    public static <T> void requestLinkService(Context context, final Call<T> requestCall, CallBack callBack) {
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
                if (t instanceof TimeoutException || t instanceof SocketTimeoutException || t instanceof ConnectTimeoutException) {
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
        boolean firstLaunch = LocalPrefences.getInstance().getBoolean(context, AppConstant.IS_FIRST_LAUNCH_SUCCESS);
        LocalPrefences.getInstance().clearSharePreference(context);
        LocalPrefences.getInstance().putBoolean(context, AppConstant.IS_FIRST_LAUNCH_SUCCESS, firstLaunch);
        Intent intent = new Intent(context, UserCheckActivity.class);
        context.startActivity(intent);
        ((Activity) context).finishAffinity();
    }

}

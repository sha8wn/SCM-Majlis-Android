package com.nibou.niboucustomer.Dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.adapters.GoingToAdapter;
import com.nibou.niboucustomer.adapters.ListAdapter;
import com.nibou.niboucustomer.api.ApiClient;
import com.nibou.niboucustomer.api.ApiEndPoint;
import com.nibou.niboucustomer.api.ApiHandler;
import com.nibou.niboucustomer.application.SCMApplication;
import com.nibou.niboucustomer.callbacks.AppCallBack;
import com.nibou.niboucustomer.models.ListResponseModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;

public class AppDialogs implements Serializable {

    private Dialog progressBar;
    private static AppDialogs instance = null;

    public interface DialogCallback {
        void response(boolean status);
    }

    public interface InputDialogCallback {
        void response(String result);
    }

    public interface ProgressDialogDissmissListener {
        void progressBarDismiss();

        void progressDialogDismiss();
    }

    private AppDialogs() {
    }

    public static AppDialogs getInstance() {
        if (instance == null) {
            instance = new AppDialogs();
        }
        return instance;
    }


    public void showProgressBar(Context context, final ProgressDialogDissmissListener progressDialogDissmissListener, boolean isShow) {
        try {

            if (progressBar != null && progressBar.isShowing())
                progressBar.cancel();

            if (context != null && !((AppCompatActivity) context).isFinishing()) {
                if (isShow) {
                    progressBar = new Dialog(context, android.R.style.Theme_Material_Dialog);
                    progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    progressBar.setContentView(R.layout.progress_bar_view);
                    progressBar.setCanceledOnTouchOutside(false);
                    progressBar.setCancelable(false);
                    ProgressBar progress = (ProgressBar) progressBar.findViewById(R.id.progressBar);
                    progress.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                }
                if (progressBar != null) {
                    progressBar.setOnDismissListener(dialog -> {
                        if (progressDialogDissmissListener != null) {
                            progressDialogDissmissListener.progressBarDismiss();
                        }
                    });
                }
                try {
                    if (isShow) {
                        if (progressBar != null && !progressBar.isShowing())
                            progressBar.show();
                    } else {
                        if (progressBar != null && progressBar.isShowing())
                            progressBar.dismiss();
                    }
                } catch (Exception e) {
                    if (isShow) {
                        progressBar = null;
                        showProgressBar(context, progressDialogDissmissListener, isShow);
                    } else {
                        progressBar = null;
                    }
                }
            } else {
                progressBar = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showConfirmCustomDialog(final Context context, String title, String message, String b1, String b2, int color, final DialogCallback dialogCallback) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.findViewById(R.id.message).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.edittext).setVisibility(View.GONE);
        dialog.findViewById(R.id.button).setVisibility(View.GONE);
        dialog.findViewById(R.id.button1).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.button2).setVisibility(View.VISIBLE);

        dialog.findViewById(R.id.v1).setBackgroundColor(color);

        TextView titletext = dialog.findViewById(R.id.title);
        titletext.setText(title);
        if (title == null)
            titletext.setVisibility(View.INVISIBLE);

        TextView messagetext = dialog.findViewById(R.id.message);
        messagetext.setText(message);

        TextView button1 = dialog.findViewById(R.id.button1);
        button1.setText(b1);
        button1.setOnClickListener(v -> {
            dialog.dismiss();
            if (dialogCallback != null) {
                dialogCallback.response(false);
            }
        });

        TextView button2 = dialog.findViewById(R.id.button2);
        button2.setText(b2);
        button2.setOnClickListener(v -> {
            dialog.dismiss();
            if (dialogCallback != null) {
                dialogCallback.response(true);
            }
        });
        dialog.show();
    }

    public void showCustomDialog(final Context context, String title, String message, String buttonText, int color, final DialogCallback dialogCallback) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.findViewById(R.id.v1).setBackgroundColor(color);
        dialog.findViewById(R.id.message).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.edittext).setVisibility(View.GONE);
        dialog.findViewById(R.id.button1).setVisibility(View.GONE);
        dialog.findViewById(R.id.button2).setVisibility(View.GONE);
        dialog.findViewById(R.id.button).setVisibility(View.VISIBLE);

        TextView titletext = dialog.findViewById(R.id.title);
        titletext.setText(title);
        TextView messagetext = dialog.findViewById(R.id.message);
        messagetext.setText(message);
        TextView button = dialog.findViewById(R.id.button);
        button.setText(buttonText);
        button.setOnClickListener(v -> {
            dialog.dismiss();
            if (dialogCallback != null) {
                dialogCallback.response(true);
            }
        });
        dialog.show();
    }

    public void openListDialog(String title, Object selectedId, Object brandId, Context context, AppCallBack appCallBack) {
        Dialog dialog = new Dialog(context, R.style.FullScreenDialogStyle);
        dialog.setContentView(R.layout.brand_list);
        ((TextView) dialog.findViewById(R.id.tvTitle)).setText(title);
        dialog.findViewById(R.id.back_arrow).findViewById(R.id.back_arrow).setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            dialog.dismiss();
        });
        RecyclerView rvBrandList = dialog.findViewById(R.id.rvList);
        rvBrandList.setLayoutManager(new LinearLayoutManager(context));
        SearchView ivSearch = dialog.findViewById(R.id.ivSearch);
        ivSearch.setVisibility(View.VISIBLE);
        dialog.show();

        if (AppUtil.isInternetAvailable(context)) {
            Call requestCall = null;
            if (title.equals(context.getString(R.string.brand))) {
                requestCall = ApiClient.getClient().create(ApiEndPoint.class).getBrandNetworkCall(1000, 1);
            } else if (title.equals(context.getString(R.string.model))) {
                requestCall = ApiClient.getClient().create(ApiEndPoint.class).getModelNetworkCall(1000, 1, Integer.parseInt(brandId.toString()));
            } else if (title.equals(context.getString(R.string.color))) {
                requestCall = ApiClient.getClient().create(ApiEndPoint.class).getColorNetworkCall(LocalPrefences.getInstance().getString(context, AppConstant.TOKEN), 1000, 1);
            }
            if (requestCall != null) {
                AppDialogs.getInstance().showProgressBar(context, null, true);
                ApiHandler.requestService(context, requestCall, new ApiHandler.CallBack() {
                    @Override
                    public void success(boolean isSuccess, Object data) {
                        AppDialogs.getInstance().showProgressBar(context, null, false);
                        if (isSuccess) {
                            ListResponseModel.Model model = null;
                            ListResponseModel listResponseModel = (ListResponseModel) data;
                            if (title.equals(context.getString(R.string.brand))) {
                                model = listResponseModel.getBrands();
                            } else if (title.equals(context.getString(R.string.model))) {
                                model = listResponseModel.getModels();
                            } else if (title.equals(context.getString(R.string.color))) {
                                model = listResponseModel.getColors();
                            }
                            rvBrandList.setAdapter(new ListAdapter(context, model, selectedId, modelList -> {
                                dialog.dismiss();
                                if (appCallBack != null)
                                    appCallBack.onSelect(modelList);
                            }));

                        } else {
                            AppDialogs.getInstance().showCustomDialog(context, context.getString(R.string.error).toUpperCase(), String.valueOf(data), context.getString(R.string.OK), context.getResources().getColor(R.color.colorPrimary), null);
                        }
                    }

                    @Override
                    public void failed() {
                        AppDialogs.getInstance().showProgressBar(context, null, false);
                    }
                });
            }
        } else {
            AppUtil.showToast(context, context.getString(R.string.internet_error));
        }
    }

    public void openGoingToListScreen(String type, ArrayList<ListResponseModel.ArrayItem> mList, Context context, AppCallBack appCallBack) {
        Dialog dialog = new Dialog(context, R.style.FullScreenDialogStyle);
        dialog.setContentView(R.layout.dialog_going_to_view);

        TextView title = dialog.findViewById(R.id.tvTitle);
        title.setText(type);

        TextView sub_title = dialog.findViewById(R.id.sub_title);
        sub_title.setText(mList.size() + " members going");

        ImageView back_arrow = dialog.findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(view -> dialog.dismiss());

        RecyclerView rvBrandList = dialog.findViewById(R.id.rvList);
        rvBrandList.setLayoutManager(new LinearLayoutManager(context));
        rvBrandList.setAdapter(new GoingToAdapter(context, mList, item -> {
        }));
        dialog.show();
        ((SCMApplication) ((AppCompatActivity) context).getApplication()).getFirebaseAnalytics().logEvent(AppConstant.GOING_TO_EVENT, new Bundle());
    }

}

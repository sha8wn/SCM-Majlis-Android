package com.nibou.niboucustomer.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.adapters.AddSuperCarAdapter;
import com.nibou.niboucustomer.api.ApiClient;
import com.nibou.niboucustomer.api.ApiEndPoint;
import com.nibou.niboucustomer.api.ApiHandler;
import com.nibou.niboucustomer.databinding.ActivitySupercarsBinding;
import com.nibou.niboucustomer.models.ListResponseModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;
import com.nibou.niboucustomer.utils.MediaUtil;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

public class AddSuperCarActivity extends BaseActivity {

    private ActivitySupercarsBinding binding;
    private AddSuperCarAdapter mListAdapter;
    private Context context;

    private boolean isSettingMenuScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_supercars);
        context = this;
        binding.toolbar.findViewById(R.id.back_arrow).setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            onBackPressed();
        });

        binding.rvCars.setLayoutManager(new LinearLayoutManager(context));

        if (AppUtil.isInternetAvailable(context)) {
            getCarsDetailsNetworkCall();
        } else {
            AppUtil.showToast(context, getString(R.string.internet_error));
        }

        if (getIntent().hasExtra(AppConstant.ADMIN_SIGNUP)) {
            isSettingMenuScreen = false;
            binding.btnSave.setVisibility(View.GONE);
            binding.btnNext.setVisibility(View.VISIBLE);
            binding.signupTitle.setVisibility(View.VISIBLE);
            binding.prevoiusTitle.setVisibility(View.VISIBLE);
            binding.nextTitle.setVisibility(View.VISIBLE);
            binding.screenTitle.setText(getString(R.string.supercars));
        } else {
            isSettingMenuScreen = true;
            binding.signupTitle.setVisibility(View.GONE);
            binding.prevoiusTitle.setVisibility(View.GONE);
            binding.nextTitle.setVisibility(View.GONE);
            binding.screenTitle.setText(getString(R.string.mane_supercar));
            binding.btnSave.setVisibility(View.VISIBLE);
            binding.btnNext.setVisibility(View.GONE);
        }

        binding.btnNext.setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            if (AppUtil.isInternetAvailable(context)) {
                if (screenValidate()) {
                    AppDialogs.getInstance().showProgressBar(context, null, true);
                    updateCarDetailNetworkCall(mListAdapter.getModelListArrayList());
                }
            } else {
                AppUtil.showToast(context, getString(R.string.internet_error));
            }
        });

        binding.btnSave.setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            if (AppUtil.isInternetAvailable(context)) {
                if (screenValidate()) {
                    AppDialogs.getInstance().showProgressBar(context, null, true);
                    updateCarDetailNetworkCall(mListAdapter.getModelListArrayList());
                }
            } else {
                AppUtil.showToast(context, getString(R.string.internet_error));
            }
        });
    }

    private void updateCarDetailNetworkCall(ArrayList<ListResponseModel.ModelList> modelListArrayList) {

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("brand", modelListArrayList.get(modelListArrayList.size() - 1).getBrand());
        parameters.put("model", modelListArrayList.get(modelListArrayList.size() - 1).getModel());
        parameters.put("color", modelListArrayList.get(modelListArrayList.size() - 1).getColor());

        ArrayList<String> carregistration = new ArrayList<>();
        if (modelListArrayList.get(modelListArrayList.size() - 1).getDocs().get(0).getImg() != null && !modelListArrayList.get(modelListArrayList.size() - 1).getDocs().get(0).getImg().isEmpty() && !modelListArrayList.get(modelListArrayList.size() - 1).getDocs().get(0).getImg().startsWith("http")) {
            carregistration.add(MediaUtil.getBase64FromPath(modelListArrayList.get(modelListArrayList.size() - 1).getDocs().get(0).getImg()));
        }
        if (modelListArrayList.get(modelListArrayList.size() - 1).getDocs().get(1).getImg() != null && !modelListArrayList.get(modelListArrayList.size() - 1).getDocs().get(1).getImg().isEmpty() && !modelListArrayList.get(modelListArrayList.size() - 1).getDocs().get(1).getImg().startsWith("http")) {
            carregistration.add(MediaUtil.getBase64FromPath(modelListArrayList.get(modelListArrayList.size() - 1).getDocs().get(1).getImg()));
        }
        if (carregistration.size() > 0)
            parameters.put("docs_add", carregistration);


        String dec_deleted_ids = "";
        if ((modelListArrayList.get(modelListArrayList.size() - 1).getDocs().get(0).getImg() == null || (modelListArrayList.get(modelListArrayList.size() - 1).getDocs().get(0).getImg() != null && !modelListArrayList.get(modelListArrayList.size() - 1).getDocs().get(0).getImg().startsWith("http"))) && modelListArrayList.get(modelListArrayList.size() - 1).getDocs().get(0).getN() != 0) {
            dec_deleted_ids = dec_deleted_ids + modelListArrayList.get(modelListArrayList.size() - 1).getDocs().get(0).getN();
        }
        if ((modelListArrayList.get(modelListArrayList.size() - 1).getDocs().get(1).getImg() == null || (modelListArrayList.get(modelListArrayList.size() - 1).getDocs().get(1).getImg() != null && !modelListArrayList.get(modelListArrayList.size() - 1).getDocs().get(1).getImg().startsWith("http"))) && modelListArrayList.get(modelListArrayList.size() - 1).getDocs().get(1).getN() != 0) {
            if (dec_deleted_ids.length() > 0)
                dec_deleted_ids = dec_deleted_ids + "," + modelListArrayList.get(modelListArrayList.size() - 1).getDocs().get(1).getN();
            else
                dec_deleted_ids = dec_deleted_ids + modelListArrayList.get(modelListArrayList.size() - 1).getDocs().get(1).getN();
        }
        if (!dec_deleted_ids.isEmpty())
            parameters.put("docs_del", dec_deleted_ids);


        ArrayList<String> carimage = new ArrayList<>();
        if (modelListArrayList.get(modelListArrayList.size() - 1).getImgs().get(0).getImg() != null && !modelListArrayList.get(modelListArrayList.size() - 1).getImgs().get(0).getImg().isEmpty() && !modelListArrayList.get(modelListArrayList.size() - 1).getImgs().get(0).getImg().startsWith("http")) {
            carimage.add(MediaUtil.getBase64FromPath(modelListArrayList.get(modelListArrayList.size() - 1).getImgs().get(0).getImg()));
        }
        if (carimage.size() > 0)
            parameters.put("imgs_add", carimage);


        String img_deleted_ids = "";
        if ((modelListArrayList.get(modelListArrayList.size() - 1).getImgs().get(0).getImg() == null || (modelListArrayList.get(modelListArrayList.size() - 1).getImgs().get(0).getImg() != null && !modelListArrayList.get(modelListArrayList.size() - 1).getImgs().get(0).getImg().startsWith("http"))) && modelListArrayList.get(modelListArrayList.size() - 1).getImgs().get(0).getN() != 0) {
            img_deleted_ids = img_deleted_ids + modelListArrayList.get(modelListArrayList.size() - 1).getImgs().get(0).getN();
        }
        if (!img_deleted_ids.isEmpty())
            parameters.put("imgs_del", img_deleted_ids);


        Call requestCall = null;
        if (modelListArrayList.get(modelListArrayList.size() - 1).getId() != null) {
            requestCall = ApiClient.getClient().create(ApiEndPoint.class).updateCarDetailNetworkCall(modelListArrayList.get(modelListArrayList.size() - 1).getId(), LocalPrefences.getInstance().getString(context, AppConstant.TOKEN), parameters);
        } else {
            requestCall = ApiClient.getClient().create(ApiEndPoint.class).addCarDetailNetworkCall(LocalPrefences.getInstance().getString(context, AppConstant.TOKEN), parameters);
        }
        ApiHandler.requestService(context, requestCall, new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                if (isSuccess) {
                    modelListArrayList.remove(modelListArrayList.size() - 1);
                    if (modelListArrayList.size() == 0) {
                        AppDialogs.getInstance().showProgressBar(context, null, false);
                        if (isSettingMenuScreen) {
                            AppDialogs.getInstance().showCustomDialog(context, getString(R.string.success), getString(R.string.car_save_alert), getString(R.string.continu), getResources().getColor(R.color.green), status -> {
                                finish();
                            });
                        } else {
                            Intent intent = new Intent(context, DocumentActivity.class);
                            intent.putExtra(AppConstant.ADMIN_SIGNUP, true);
                            startActivityForResult(intent, 1000);
                        }
                    } else {
                        updateCarDetailNetworkCall(modelListArrayList);
                    }
                } else {
                    AppDialogs.getInstance().showProgressBar(context, null, false);
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
                    if (listResponseModel.getCars() != null && listResponseModel.getCars().getList() != null) {
                        mListAdapter = new AddSuperCarAdapter(context, isSettingMenuScreen, listResponseModel.getCars().getList());
                        binding.rvCars.setAdapter(mListAdapter);
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

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra(AppConstant.ADMIN_SIGNUP)) {
            Intent intent = new Intent();
            intent.putExtra(AppConstant.REFRESH_TOKEN, LocalPrefences.getInstance().getString(context, AppConstant.TOKEN));
            setResult(RESULT_OK, intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mListAdapter != null && mListAdapter.getMediaUtil() != null)
            mListAdapter.getMediaUtil().onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (AppUtil.isInternetAvailable(context)) {
                getCarsDetailsNetworkCall();
            } else {
                AppUtil.showToast(context, getString(R.string.internet_error));
            }
        }
    }

    private boolean screenValidate() {
        if (mListAdapter != null && mListAdapter.getModelListArrayList() != null && mListAdapter.getModelListArrayList().size() > 0) {
            for (int i = 0; i < mListAdapter.getModelListArrayList().size(); i++) {
                if (!(mListAdapter.getModelListArrayList().get(i).getBrand() != null && !mListAdapter.getModelListArrayList().get(i).getBrand().isEmpty() && !mListAdapter.getModelListArrayList().get(i).getBrand().equals("0"))) {
                    AppUtil.showToast(context, getResources().getString(R.string.brand_empty_alert));
                    return false;
                } else if (!(mListAdapter.getModelListArrayList().get(i).getModel() != null && !mListAdapter.getModelListArrayList().get(i).getModel().isEmpty() && !mListAdapter.getModelListArrayList().get(i).getModel().equals("0"))) {
                    AppUtil.showToast(context, getResources().getString(R.string.model_empty_alert));
                    return false;
                } else if (!(mListAdapter.getModelListArrayList().get(i).getColor() != null && !mListAdapter.getModelListArrayList().get(i).getColor().isEmpty() && !mListAdapter.getModelListArrayList().get(i).getColor().equals("0"))) {
                    AppUtil.showToast(context, getResources().getString(R.string.color_empty_alert));
                    return false;
                }
//                else if (mListAdapter.getModelListArrayList().get(i).getDocs().get(0).getImg() == null && mListAdapter.getModelListArrayList().get(i).getDocs().get(1).getImg() != null) {
//                    AppUtil.showToast(context, getResources().getString(R.string.car_front_registration_empty_alert));
//                    return false;
//                } else if ((mListAdapter.getModelListArrayList().get(i).getDocs().get(0).getImg() != null && mListAdapter.getModelListArrayList().get(i).getDocs().get(1).getImg() == null)) {
//                    AppUtil.showToast(context, getResources().getString(R.string.car_back_registration_empty_alert));
//                    return false;
//                }
            }
            return true;
        }
        return false;
    }


}

package com.nibou.niboucustomer.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.model.Image;
import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.adapters.AddSuperCarAdapter;
import com.nibou.niboucustomer.api.ApiClient;
import com.nibou.niboucustomer.api.ApiEndPoint;
import com.nibou.niboucustomer.api.ApiHandler;
import com.nibou.niboucustomer.databinding.ActivityUserDocumentsBinding;
import com.nibou.niboucustomer.models.ListResponseModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;
import com.nibou.niboucustomer.utils.MediaUtil;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

public class DocumentActivity extends BaseActivity {

    private ActivityUserDocumentsBinding binding;
    private Context context;
    private MediaUtil mediaUtil;

    private boolean isSettingMenuScreen;
    private String userId;
    private String imageEmiratesFrontUrl, imageEmiratesBackUrl, imageDriverFrontUrl, imageDriverBackUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_documents);
        context = this;
        mediaUtil = new MediaUtil(context);

        binding.toolbar.findViewById(R.id.back_arrow).setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            onBackPressed();
        });

        if (AppUtil.isInternetAvailable(context)) {
            getUserDetailsNetworkCall();
        } else {
            AppUtil.showToast(context, getString(R.string.internet_error));
        }

        if (getIntent().hasExtra(AppConstant.ADMIN_SIGNUP)) {
            binding.btnFinish.setText(getString(R.string.finish));
            binding.signupTitle.setVisibility(View.VISIBLE);
            binding.prevoiusTitle.setVisibility(View.VISIBLE);
            binding.screenTitle.setText(getString(R.string.documents));
            isSettingMenuScreen = false;
        } else {
            isSettingMenuScreen = true;
            binding.signupTitle.setVisibility(View.GONE);
            binding.prevoiusTitle.setVisibility(View.GONE);
            binding.screenTitle.setText(getString(R.string.manage_documents));
            binding.btnFinish.setText(getString(R.string.save));
        }

        binding.imageEmiratesFront.setOnClickListener(view -> {
            mediaUtil.openSingleImageGallery((isCamera, imageResult) -> {
                ArrayList<Image> imagesList = (ArrayList<Image>) imageResult;
                if (imagesList != null && imagesList.size() > 0) {
                    mediaUtil.compressMultipleImages(imagesList, AppConstant.COMPRESSION_RATIO, (bitmap, fileName, path) -> {
                        if (path != null) {
                            ((Activity) context).runOnUiThread(() -> {
                                loadImage(binding.imageEmiratesFront, path);
                                imageEmiratesFrontUrl = path;
                                binding.imageEmiratesFrontCross.setVisibility(View.VISIBLE);
                            });
                        }
                    });
                }
            });
        });
        binding.imageEmiratesFrontCross.setOnClickListener(v -> {
            binding.imageEmiratesFront.setImageResource(R.drawable.dashed_bg);
            imageEmiratesFrontUrl = null;
            binding.imageEmiratesFrontCross.setVisibility(View.GONE);
        });

        binding.imageEmiratesBack.setOnClickListener(view -> {
            mediaUtil.openSingleImageGallery((isCamera, imageResult) -> {
                ArrayList<Image> imagesList = (ArrayList<Image>) imageResult;
                if (imagesList != null && imagesList.size() > 0) {
                    mediaUtil.compressMultipleImages(imagesList, AppConstant.COMPRESSION_RATIO, (bitmap, fileName, path) -> {
                        if (path != null) {
                            ((Activity) context).runOnUiThread(() -> {
                                loadImage(binding.imageEmiratesBack, path);
                                imageEmiratesBackUrl = path;
                                binding.imageEmiratesBackCross.setVisibility(View.VISIBLE);
                            });
                        }
                    });
                }
            });
        });

        binding.imageEmiratesBackCross.setOnClickListener(v -> {
            binding.imageEmiratesBack.setImageResource(R.drawable.dashed_bg);
            imageEmiratesBackUrl = null;
            binding.imageEmiratesBackCross.setVisibility(View.GONE);
        });


        binding.imageDriverFront.setOnClickListener(view -> {
            mediaUtil.openSingleImageGallery((isCamera, imageResult) -> {
                ArrayList<Image> imagesList = (ArrayList<Image>) imageResult;
                if (imagesList != null && imagesList.size() > 0) {
                    mediaUtil.compressMultipleImages(imagesList, AppConstant.COMPRESSION_RATIO, (bitmap, fileName, path) -> {
                        if (path != null) {
                            ((Activity) context).runOnUiThread(() -> {
                                loadImage(binding.imageDriverFront, path);
                                imageDriverFrontUrl = path;
                                binding.imageDriverFrontCross.setVisibility(View.VISIBLE);
                            });
                        }
                    });
                }
            });
        });

        binding.imageDriverFrontCross.setOnClickListener(v -> {
            binding.imageDriverFront.setImageResource(R.drawable.dashed_bg);
            imageDriverFrontUrl = null;
            binding.imageDriverFrontCross.setVisibility(View.GONE);
        });

        binding.imageDriverBack.setOnClickListener(view -> {
            mediaUtil.openSingleImageGallery((isCamera, imageResult) -> {
                ArrayList<Image> imagesList = (ArrayList<Image>) imageResult;
                if (imagesList != null && imagesList.size() > 0) {
                    mediaUtil.compressMultipleImages(imagesList, AppConstant.COMPRESSION_RATIO, (bitmap, fileName, path) -> {
                        if (path != null) {
                            ((Activity) context).runOnUiThread(() -> {
                                loadImage(binding.imageDriverBack, path);
                                imageDriverBackUrl = path;
                                binding.imageDriverBackCross.setVisibility(View.VISIBLE);
                            });
                        }
                    });
                }
            });
        });

        binding.imageDriverBackCross.setOnClickListener(v -> {
            binding.imageDriverBack.setImageResource(R.drawable.dashed_bg);
            imageDriverBackUrl = null;
            binding.imageDriverBackCross.setVisibility(View.GONE);
        });

        binding.btnFinish.setOnClickListener(v -> {
            if (AppUtil.isInternetAvailable(context)) {
                if (screenValidate()) {
                    updateUserDocumentNetworkCall();
                }
            } else {
                AppUtil.showToast(context, getString(R.string.internet_error));
            }
        });
    }

    private void showAdminApproveUserDetails(ListResponseModel.ModelList user) {
        userId = user.getId();

        if (user.getDocs().size() > 0)
            loadImage(binding.imageEmiratesFront, user.getDocs().get(0).getImg());

        if (user.getDocs().size() > 1)
            loadImage(binding.imageEmiratesBack, user.getDocs().get(1).getImg());

        if (user.getDocs().size() > 0)
            loadImage(binding.imageDriverFront, user.getLicenses().get(0).getImg());

        if (user.getDocs().size() > 1)
            loadImage(binding.imageDriverBack, user.getLicenses().get(1).getImg());
    }

    private void movedToNextScreen() {
        if (isSettingMenuScreen) {
            AppDialogs.getInstance().showCustomDialog(context, getString(R.string.success), getString(R.string.document_upload_alert), getString(R.string.continu), getResources().getColor(R.color.green), status -> {
                finish();
            });
        } else {
            AppDialogs.getInstance().showCustomDialog(context, getString(R.string.success), getString(R.string.admin_register_success), getString(R.string.continu), getResources().getColor(R.color.green), status -> {
                LocalPrefences.getInstance().putString(context, AppConstant.USER_ID, userId);
                Intent intent = new Intent(context, HomeActivity.class);
                startActivity(intent);
                finishAffinity();
            });
        }
    }


    private void updateUserDocumentNetworkCall() {

        HashMap<String, Object> parameters = new HashMap<>();

        ArrayList<String> emiratesImages = new ArrayList<>();
        if (imageEmiratesFrontUrl != null)
            emiratesImages.add(MediaUtil.getBase64FromPath(imageEmiratesFrontUrl));
        if (imageEmiratesBackUrl != null)
            emiratesImages.add(MediaUtil.getBase64FromPath(imageEmiratesBackUrl));
        if (emiratesImages.size() > 0)
            parameters.put("docs_add", emiratesImages);

        ArrayList<String> licenseImages = new ArrayList<>();
        if (imageDriverFrontUrl != null)
            licenseImages.add(MediaUtil.getBase64FromPath(imageDriverFrontUrl));
        if (imageDriverBackUrl != null)
            licenseImages.add(MediaUtil.getBase64FromPath(imageDriverBackUrl));
        if (licenseImages.size() > 0)
            parameters.put("licenses_add", licenseImages);

        if (parameters.size() == 0) {
            movedToNextScreen();
            return;
        }

        AppDialogs.getInstance().showProgressBar(context, null, true);
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).updateUserDocumentNetworkCall(userId, LocalPrefences.getInstance().getString(context, AppConstant.TOKEN), parameters), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    movedToNextScreen();
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

    private void getUserDetailsNetworkCall() {
        AppDialogs.getInstance().showProgressBar(context, null, true);
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).getUserDetailsNetworkCall(LocalPrefences.getInstance().getString(context, AppConstant.TOKEN)), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    ListResponseModel listResponseModel = (ListResponseModel) data;
                    if (listResponseModel.getUsers().getList() != null && listResponseModel.getUsers().getList().size() > 0)
                        showAdminApproveUserDetails(listResponseModel.getUsers().getList().get(0));
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
        mediaUtil.onActivityResult(requestCode, resultCode, data);
    }

    private void loadImage(ImageView imageView, String url) {
        Glide.with(context).load(url).centerCrop().into(imageView);
    }

    private boolean screenValidate() {
        if (imageEmiratesFrontUrl == null && imageEmiratesBackUrl != null) {
            AppUtil.showToast(context, getResources().getString(R.string.emirates_front_empty_alert));
            return false;
        } else if (imageEmiratesFrontUrl != null && imageEmiratesBackUrl == null) {
            AppUtil.showToast(context, getResources().getString(R.string.emirates_back_empty_alert));
            return false;
        } else if (imageDriverFrontUrl == null && imageDriverBackUrl != null) {
            AppUtil.showToast(context, getResources().getString(R.string.driver_front_empty_alert));
            return false;
        } else if (imageDriverFrontUrl != null && imageDriverBackUrl == null) {
            AppUtil.showToast(context, getResources().getString(R.string.driver_back_empty_alert));
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra(AppConstant.ADMIN_SIGNUP)) {
        } else {
            super.onBackPressed();
        }
    }
}

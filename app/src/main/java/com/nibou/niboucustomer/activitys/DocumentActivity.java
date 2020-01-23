package com.nibou.niboucustomer.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import com.nibou.niboucustomer.application.SCMApplication;
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
    private int imageEmiratesFrontId, imageEmiratesBackId, imageDriverFrontId, imageDriverBackId;
    private ListResponseModel.ModelList modelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SCMApplication) getApplication()).getFirebaseAnalytics().logEvent(AppConstant.MANAGE_DOCUMENT_EVENT, new Bundle());
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
            binding.backgroundView.setBackground(ContextCompat.getDrawable(context, R.drawable.new_bg));
            binding.btnFinish.setText(getString(R.string.finish));
            binding.signupTitle.setVisibility(View.VISIBLE);
            binding.prevoiusTitle.setVisibility(View.VISIBLE);
            binding.screenTitle.setText(getString(R.string.documents));
            isSettingMenuScreen = false;

            if (getIntent().hasExtra(AppConstant.NORMAL_SIGNUP)) {
                binding.signupTitle.setText(getString(R.string.new_user));
            }
        } else {
            isSettingMenuScreen = true;
            binding.backgroundView.setBackgroundColor(ContextCompat.getColor(context, R.color.app_theme_color));
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
                    if (isSettingMenuScreen) {
                        updateUserDocumentNetworkCall();
                    } else {
                        if (getIntent().hasExtra(AppConstant.NORMAL_SIGNUP)) {
                            AppDialogs.getInstance().showConfirmCustomDialog(context, getString(R.string.alert), getString(R.string.registeration_confirmation_alert), getString(R.string.CANCEL), getString(R.string.register_your_interest), getResources().getColor(R.color.green), status -> {
                                if (status) {
                                    updateUserDocumentNetworkCall();
                                }
                            });
                        } else {
                            updateUserDocumentNetworkCall();
                        }
                    }
                }
            } else {
                AppUtil.showToast(context, getString(R.string.internet_error));
            }
        });
    }

    private void showAdminApproveUserDetails(ListResponseModel.ModelList user) {
        userId = user.getId();

        if (user.getDocs().size() > 0) {
            imageEmiratesFrontUrl = user.getDocs().get(0).getImg();
            imageEmiratesFrontId = user.getDocs().get(0).getN();
            binding.imageEmiratesFrontCross.setVisibility(View.VISIBLE);
            loadImage(binding.imageEmiratesFront, imageEmiratesFrontUrl);
        } else {
            binding.imageEmiratesFrontCross.setVisibility(View.GONE);
        }

        if (user.getDocs().size() > 1) {
            imageEmiratesBackUrl = user.getDocs().get(1).getImg();
            imageEmiratesBackId = user.getDocs().get(1).getN();
            binding.imageEmiratesBackCross.setVisibility(View.VISIBLE);
            loadImage(binding.imageEmiratesBack, imageEmiratesBackUrl);
        } else {
            binding.imageEmiratesBackCross.setVisibility(View.GONE);
        }

        if (user.getLicenses().size() > 0) {
            imageDriverFrontUrl = user.getLicenses().get(0).getImg();
            imageDriverFrontId = user.getLicenses().get(0).getN();
            binding.imageDriverFrontCross.setVisibility(View.VISIBLE);
            loadImage(binding.imageDriverFront, imageDriverFrontUrl);
        } else {
            binding.imageDriverFrontCross.setVisibility(View.GONE);
        }

        if (user.getLicenses().size() > 1) {
            imageDriverBackUrl = user.getLicenses().get(1).getImg();
            imageDriverBackId = user.getLicenses().get(1).getN();
            binding.imageDriverBackCross.setVisibility(View.VISIBLE);
            loadImage(binding.imageDriverBack, imageDriverBackUrl);
        } else {
            binding.imageDriverBackCross.setVisibility(View.GONE);
        }
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
        if (imageEmiratesFrontUrl != null && !imageEmiratesFrontUrl.startsWith("http"))
            emiratesImages.add(MediaUtil.getBase64FromPath(imageEmiratesFrontUrl));
        if (imageEmiratesBackUrl != null && !imageEmiratesBackUrl.startsWith("http"))
            emiratesImages.add(MediaUtil.getBase64FromPath(imageEmiratesBackUrl));
        if (emiratesImages.size() > 0)
            parameters.put("docs_add", emiratesImages);

        ArrayList<String> licenseImages = new ArrayList<>();
        if (imageDriverFrontUrl != null && !imageDriverFrontUrl.startsWith("http"))
            licenseImages.add(MediaUtil.getBase64FromPath(imageDriverFrontUrl));
        if (imageDriverBackUrl != null && !imageDriverBackUrl.startsWith("http"))
            licenseImages.add(MediaUtil.getBase64FromPath(imageDriverBackUrl));
        if (licenseImages.size() > 0)
            parameters.put("licenses_add", licenseImages);


        String dec_deleted_ids = "";
        if ((imageEmiratesFrontUrl == null || (imageEmiratesFrontUrl != null && !imageEmiratesFrontUrl.startsWith("http"))) && imageEmiratesFrontId != 0) {
            dec_deleted_ids = dec_deleted_ids + imageEmiratesFrontId;
        }
        if ((imageEmiratesBackUrl == null || (imageEmiratesBackUrl != null && !imageEmiratesBackUrl.startsWith("http"))) && imageEmiratesBackId != 0) {
            if (dec_deleted_ids.length() > 0)
                dec_deleted_ids = dec_deleted_ids + "," + imageEmiratesBackId;
            else
                dec_deleted_ids = dec_deleted_ids + imageEmiratesBackId;
        }
        if (!dec_deleted_ids.isEmpty())
            parameters.put("docs_del", dec_deleted_ids);

        String license_deleted_ids = "";
        if ((imageDriverFrontUrl == null || (imageDriverFrontUrl != null && !imageDriverFrontUrl.startsWith("http"))) && imageDriverFrontId != 0) {
            license_deleted_ids = license_deleted_ids + imageDriverFrontId;
        }
        if ((imageDriverBackUrl == null || (imageDriverBackUrl != null && !imageDriverBackUrl.startsWith("http"))) && imageDriverBackId != 0) {
            if (license_deleted_ids.length() > 0)
                license_deleted_ids = license_deleted_ids + "," + imageDriverBackId;
            else
                license_deleted_ids = license_deleted_ids + imageDriverBackId;
        }
        if (!license_deleted_ids.isEmpty())
            parameters.put("licenses_del", license_deleted_ids);

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
                    if (getIntent().hasExtra(AppConstant.NORMAL_SIGNUP)) {
                        AppDialogs.getInstance().showCustomDialog(context, getString(R.string.thank_you),
                                getString(R.string.thank_you_desc), getString(R.string.continu),
                                getResources().getColor(R.color.white), status -> {
                                    try {
                                        Intent intent = new Intent(context, PastEventActivity.class);
                                        startActivity(intent);
                                        finishAffinity();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                    } else {
                        movedToNextScreen();
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

    private void getUserDetailsNetworkCall() {
        AppDialogs.getInstance().showProgressBar(context, null, true);
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).getSettingScreenNetworkCall(LocalPrefences.getInstance().getString(context, AppConstant.TOKEN), LocalPrefences.getInstance().getString(context, AppConstant.USER_ID)), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    ListResponseModel listResponseModel = (ListResponseModel) data;
                    if (listResponseModel.getUsers().getList() != null && listResponseModel.getUsers().getList().size() > 0) {
                        modelList = listResponseModel.getUsers().getList().get(0);
                        showAdminApproveUserDetails(listResponseModel.getUsers().getList().get(0));
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaUtil.onActivityResult(requestCode, resultCode, data);
    }

    private void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).centerCrop().placeholder(R.drawable.dashed_bg).error(R.drawable.dashed_bg).dontAnimate().into(imageView);
    }

    private boolean screenValidate() {
        if (getIntent().hasExtra(AppConstant.NORMAL_SIGNUP)) {
            if (imageEmiratesFrontUrl == null && imageEmiratesBackUrl == null) {
                AppUtil.showToast(context, getResources().getString(R.string.emirates_id_empty_alert));
                return false;
            } else if (imageDriverFrontUrl == null && imageDriverBackUrl == null) {
                AppUtil.showToast(context, getResources().getString(R.string.driver_license_empty_alert));
                return false;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra(AppConstant.NORMAL_SIGNUP)) {
            super.onBackPressed();
        } else {
            if (getIntent().hasExtra(AppConstant.ADMIN_SIGNUP)) {
                super.onBackPressed();
            } else {
                super.onBackPressed();
            }
        }
    }
}

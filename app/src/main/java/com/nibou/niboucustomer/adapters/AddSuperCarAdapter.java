package com.nibou.niboucustomer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.esafirm.imagepicker.model.Image;
import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.activitys.DocumentActivity;
import com.nibou.niboucustomer.activitys.HomeActivity;
import com.nibou.niboucustomer.api.ApiClient;
import com.nibou.niboucustomer.api.ApiEndPoint;
import com.nibou.niboucustomer.api.ApiHandler;
import com.nibou.niboucustomer.callbacks.AppCallBack;
import com.nibou.niboucustomer.models.ListResponseModel;
import com.nibou.niboucustomer.models.PreviousExpertModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;
import com.nibou.niboucustomer.utils.MediaUtil;
import com.nibou.niboucustomer.utils.RoundedCornersTransformation;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;


public class AddSuperCarAdapter extends RecyclerView.Adapter<AddSuperCarAdapter.MyViewHolder> {

    private Context context;
    private boolean isSettingMenuScreen;

    public ArrayList<ListResponseModel.ModelList> modelListArrayList;
    public MediaUtil mediaUtil;

    public MediaUtil getMediaUtil() {
        return mediaUtil;
    }

    public void setMediaUtil(MediaUtil mediaUtil) {
        this.mediaUtil = mediaUtil;
    }

    public ArrayList<ListResponseModel.ModelList> getModelListArrayList() {
        return modelListArrayList;
    }

    public void setModelListArrayList(ArrayList<ListResponseModel.ModelList> modelListArrayList) {
        this.modelListArrayList = modelListArrayList;
    }

    public AddSuperCarAdapter(Context context, boolean isSettingMenuScreen, ArrayList<ListResponseModel.ModelList> modelListArrayList) {
        this.context = context;
        this.isSettingMenuScreen = isSettingMenuScreen;
        this.modelListArrayList = modelListArrayList;
        mediaUtil = new MediaUtil(context);

        for (int i = 0; i < modelListArrayList.size(); i++) {
            if (modelListArrayList.get(i).getDocs().size() != 2) {
                ArrayList<ListResponseModel.Img> imgArrayList = new ArrayList<>();
                imgArrayList.add(new ListResponseModel.Img());
                imgArrayList.add(new ListResponseModel.Img());
                modelListArrayList.get(i).setDocs(imgArrayList);
            }
            if (modelListArrayList.get(i).getImgs().size() != 1) {
                ArrayList<ListResponseModel.Img> imgArrayList = new ArrayList<>();
                imgArrayList.add(new ListResponseModel.Img());
                modelListArrayList.get(i).setImgs(imgArrayList);
            }
        }
    }

    private void addNewCarView() {
        ListResponseModel.ModelList modelList = new ListResponseModel.ModelList();
        ArrayList<ListResponseModel.Img> docImage = new ArrayList<>();
        docImage.add(new ListResponseModel.Img());
        docImage.add(new ListResponseModel.Img());
        modelList.setDocs(docImage);
        ArrayList<ListResponseModel.Img> carImage = new ArrayList<>();
        carImage.add(new ListResponseModel.Img());
        modelList.setImgs(carImage);
        modelListArrayList.add(modelList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_add_supercar, viewGroup, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        if (position == getItemCount() - 1) {
            myViewHolder.tv_add_car.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.tv_add_car.setVisibility(View.GONE);
        }

        if (getItemCount() == 1) {
            myViewHolder.delete_car.setVisibility(View.GONE);
        } else {
            myViewHolder.delete_car.setVisibility(View.VISIBLE);
        }


        myViewHolder.etBrand.setText(modelListArrayList.get(position).getBrandName());
        myViewHolder.etModel.setText(modelListArrayList.get(position).getModelName());
        myViewHolder.etColor.setText(modelListArrayList.get(position).getColorName());

        if (modelListArrayList.get(position).getDocs().get(0).getImg() != null && !modelListArrayList.get(position).getDocs().get(0).getImg().isEmpty()) {
            loadImage(myViewHolder.image_car_front, modelListArrayList.get(position).getDocs().get(0).getImg());
            myViewHolder.image_car_front_cross.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.image_car_front.setImageResource(R.drawable.dashed_bg);
            myViewHolder.image_car_front_cross.setVisibility(View.GONE);
        }
        if (modelListArrayList.get(position).getDocs().get(1).getImg() != null && !modelListArrayList.get(position).getDocs().get(1).getImg().isEmpty()) {
            loadImage(myViewHolder.image_car_back, modelListArrayList.get(position).getDocs().get(1).getImg());
            myViewHolder.image_car_back_cross.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.image_car_back.setImageResource(R.drawable.dashed_bg);
            myViewHolder.image_car_back_cross.setVisibility(View.GONE);
        }
        if (modelListArrayList.get(position).getImgs().get(0).getImg() != null && !modelListArrayList.get(position).getImgs().get(0).getImg().isEmpty()) {
            loadImage(myViewHolder.ivCar, modelListArrayList.get(position).getImgs().get(0).getImg());
            myViewHolder.imgCross.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.ivCar.setImageResource(R.drawable.dashed_bg);
            myViewHolder.imgCross.setVisibility(View.GONE);
        }
    }

    private void loadImage(ImageView imageView, String url) {
        Glide.with(context).load(url).dontAnimate().centerCrop().into(imageView);
    }


    @Override
    public int getItemCount() {
        if (modelListArrayList != null)
            return modelListArrayList.size();
        else return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_add_car, delete_car;
        private EditText etBrand, etModel, etColor;
        private ImageView image_car_front, image_car_front_cross, image_car_back, image_car_back_cross, ivCar, imgCross;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            etBrand = itemView.findViewById(R.id.et_brand);
            etModel = itemView.findViewById(R.id.et_model);
            etColor = itemView.findViewById(R.id.et_color);
            ivCar = itemView.findViewById(R.id.ivCar);
            image_car_front = itemView.findViewById(R.id.image_car_front);
            image_car_front_cross = itemView.findViewById(R.id.image_car_front_cross);
            image_car_back = itemView.findViewById(R.id.image_car_back);
            image_car_back_cross = itemView.findViewById(R.id.image_car_back_cross);
            imgCross = itemView.findViewById(R.id.imgCross);
            tv_add_car = itemView.findViewById(R.id.tv_add_car);
            delete_car = itemView.findViewById(R.id.delete_car);

            etBrand.setOnClickListener(view -> {
                AppDialogs.getInstance().openListDialog(context.getString(R.string.brand), modelListArrayList.get(getAdapterPosition()).getBrand(), null, context, new AppCallBack() {
                    @Override
                    public void onSelect(ListResponseModel.ModelList modelList) {
                        if (modelList != null) {
                            modelListArrayList.get(getAdapterPosition()).setBrand(modelList.getId());
                            modelListArrayList.get(getAdapterPosition()).setBrandName(modelList.getName());

                            modelListArrayList.get(getAdapterPosition()).setModel(null);
                            modelListArrayList.get(getAdapterPosition()).setModelName(null);
                            notifyDataSetChanged();

                        }
                    }
                });
            });


            etModel.setOnClickListener(view -> {
                if (modelListArrayList.get(getAdapterPosition()).getBrandName() != null && !modelListArrayList.get(getAdapterPosition()).getBrandName().equals("")) {
                    AppDialogs.getInstance().openListDialog(context.getString(R.string.model), modelListArrayList.get(getAdapterPosition()).getModel(), modelListArrayList.get(getAdapterPosition()).getBrand(), context, new AppCallBack() {
                        @Override
                        public void onSelect(ListResponseModel.ModelList modelList) {
                            if (modelList != null) {
                                modelListArrayList.get(getAdapterPosition()).setModel(modelList.getId());
                                modelListArrayList.get(getAdapterPosition()).setModelName(modelList.getName());
                                notifyDataSetChanged();
                            }
                        }
                    });
                } else {
                    AppUtil.showToast(context, context.getResources().getString(R.string.brand_first_empty_alert));
                }
            });


            etColor.setOnClickListener(view -> {
                AppDialogs.getInstance().openListDialog(context.getString(R.string.color), modelListArrayList.get(getAdapterPosition()).getColor(), null, context, new AppCallBack() {
                    @Override
                    public void onSelect(ListResponseModel.ModelList modelList) {
                        if (modelList != null) {
                            modelListArrayList.get(getAdapterPosition()).setColor(modelList.getId());
                            modelListArrayList.get(getAdapterPosition()).setColorName(modelList.getName());
                            notifyDataSetChanged();
                        }
                    }
                });
            });

            image_car_front.setOnClickListener(view -> {
                mediaUtil.openSingleImageGallery((isCamera, imageResult) -> {
                    ArrayList<Image> imagesList = (ArrayList<Image>) imageResult;
                    if (imagesList != null && imagesList.size() > 0) {
                        mediaUtil.compressMultipleImages(imagesList, AppConstant.COMPRESSION_RATIO, (bitmap, fileName, path) -> {
                            if (path != null) {
                                ((Activity) context).runOnUiThread(() -> {
                                    modelListArrayList.get(getAdapterPosition()).getDocs().get(0).setImg(path);
                                    notifyDataSetChanged();
                                });
                            }
                        });
                    }
                });
            });

            image_car_front_cross.setOnClickListener(view -> {
                modelListArrayList.get(getAdapterPosition()).getDocs().get(0).setImg(null);
                notifyDataSetChanged();
            });


            image_car_back.setOnClickListener(view -> {
                mediaUtil.openSingleImageGallery((isCamera, imageResult) -> {
                    ArrayList<Image> imagesList = (ArrayList<Image>) imageResult;
                    if (imagesList != null && imagesList.size() > 0) {
                        mediaUtil.compressMultipleImages(imagesList, AppConstant.COMPRESSION_RATIO, (bitmap, fileName, path) -> {
                            if (path != null) {
                                ((Activity) context).runOnUiThread(() -> {
                                    modelListArrayList.get(getAdapterPosition()).getDocs().get(1).setImg(path);
                                    notifyDataSetChanged();
                                });
                            }
                        });
                    }
                });
            });

            image_car_back_cross.setOnClickListener(view -> {
                modelListArrayList.get(getAdapterPosition()).getDocs().get(1).setImg(null);
                notifyDataSetChanged();
            });

            ivCar.setOnClickListener(view -> {
                mediaUtil.openSingleImageGallery((isCamera, imageResult) -> {
                    ArrayList<Image> imagesList = (ArrayList<Image>) imageResult;
                    if (imagesList != null && imagesList.size() > 0) {
                        mediaUtil.compressMultipleImages(imagesList, AppConstant.COMPRESSION_RATIO, (bitmap, fileName, path) -> {
                            if (path != null) {
                                ((Activity) context).runOnUiThread(() -> {
                                    modelListArrayList.get(getAdapterPosition()).getImgs().get(0).setImg(path);
                                    notifyDataSetChanged();
                                });
                            }
                        });
                    }
                });
            });

            imgCross.setOnClickListener(view -> {
                modelListArrayList.get(getAdapterPosition()).getImgs().get(0).setImg(null);
                notifyDataSetChanged();
            });

            tv_add_car.setOnClickListener(v -> addNewCarView());

            delete_car.setOnClickListener(v -> {
                AppDialogs.getInstance().showConfirmCustomDialog(context, context.getString(R.string.alert), context.getString(R.string.car_delete_alert), context.getString(R.string.CANCEL).toUpperCase(), context.getString(R.string.OK).toUpperCase(), context.getResources().getColor(R.color.white), new AppDialogs.DialogCallback() {
                    @Override
                    public void response(boolean status) {
                        if (status) {
                            if (modelListArrayList.get(getAdapterPosition()).getId() != null) {
                                if (AppUtil.isInternetAvailable(context)) {
                                    deleteCarNetworkCall(getAdapterPosition());
                                } else {
                                    AppUtil.showToast(context, context.getString(R.string.internet_error));
                                }
                            } else {
                                modelListArrayList.remove(getAdapterPosition());
                                notifyDataSetChanged();
                            }
                        }
                    }
                });
            });
        }
    }

    private void deleteCarNetworkCall(int position) {
        AppDialogs.getInstance().showProgressBar(context, null, true);
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).deleteCarNetworkCall(modelListArrayList.get(position).getId(), LocalPrefences.getInstance().getString(context, AppConstant.TOKEN)), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    AppDialogs.getInstance().showCustomDialog(context, context.getString(R.string.success), context.getString(R.string.car_success_delete_alert), context.getString(R.string.OK), context.getResources().getColor(R.color.green), status -> {
                        modelListArrayList.remove(position);
                        notifyDataSetChanged();
                    });
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
}

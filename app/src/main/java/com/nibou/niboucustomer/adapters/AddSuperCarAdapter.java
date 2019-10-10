package com.nibou.niboucustomer.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.activitys.HomeActivity;
import com.nibou.niboucustomer.callbacks.AppCallBack;
import com.nibou.niboucustomer.models.PreviousExpertModel;
import com.nibou.niboucustomer.utils.AppUtil;

import java.util.ArrayList;


public class AddSuperCarAdapter extends RecyclerView.Adapter<AddSuperCarAdapter.MyViewHolder> {

    private Context context;
    private boolean isSettingMenuScreen;

    private ArrayList<String> arrayList = new ArrayList<>();

    public AddSuperCarAdapter(Context context, boolean isSettingMenuScreen) {
        this.context = context;
        this.isSettingMenuScreen = isSettingMenuScreen;
        arrayList.add("");
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

        myViewHolder.etBrand.setOnClickListener(view -> {
            AppUtil.hideKeyBoard(context);
            AppDialogs.getInstance().openBrandListDialog("Brand", context, new AppCallBack() {
                @Override
                public void onSelect(String item) {
                    myViewHolder.etBrand.setText(item);
                }
            });
        });
        myViewHolder.etModel.setOnClickListener(view -> {
            AppUtil.hideKeyBoard(context);
            AppDialogs.getInstance().openBrandListDialog("Model", context, new AppCallBack() {
                @Override
                public void onSelect(String item) {
                    myViewHolder.etModel.setText(item);
                }
            });
        });
        myViewHolder.etColor.setOnClickListener(view -> {
            AppUtil.hideKeyBoard(context);
            AppDialogs.getInstance().openBrandListDialog("Color", context, new AppCallBack() {
                @Override
                public void onSelect(String item) {
                    myViewHolder.etColor.setText(item);
                }
            });
        });
    }

    private void showImage(ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .placeholder(R.drawable.default_placeholder)
                .error(R.drawable.default_placeholder)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvFront, tvBack, tv_add_car, delete_car;
        private EditText etBrand, etModel, etColor;
        private ImageView ivBackDoc, ivFrontDoc, ivCar, imgCross;
        private RecyclerView rvEvents;
        private ConstraintLayout item;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            etBrand = itemView.findViewById(R.id.et_brand);
            etModel = itemView.findViewById(R.id.et_model);
            etColor = itemView.findViewById(R.id.et_color);
            ivCar = itemView.findViewById(R.id.ivCar);
            imgCross = itemView.findViewById(R.id.imgCross);
            tv_add_car = itemView.findViewById(R.id.tv_add_car);
            tv_add_car.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrayList.add("");
                    notifyDataSetChanged();
                }
            });
            delete_car = itemView.findViewById(R.id.delete_car);
            if (isSettingMenuScreen) {
                delete_car.setVisibility(View.VISIBLE);
            } else {
                delete_car.setVisibility(View.GONE);
            }
            delete_car.setOnClickListener(v -> {
                AppDialogs.getInstance().showConfirmCustomDialog(context, context.getString(R.string.alert), context.getString(R.string.car_delete_alert), context.getString(R.string.CANCEL).toUpperCase(), context.getString(R.string.OK).toUpperCase(), context.getResources().getColor(R.color.white), new AppDialogs.DialogCallback() {
                    @Override
                    public void response(boolean status) {
                        if (status) {
                            if (arrayList.size() > 0) {
                                arrayList.remove(getAdapterPosition());
                                notifyDataSetChanged();
                            }
                        }
                    }
                });
            });
        }
    }
}

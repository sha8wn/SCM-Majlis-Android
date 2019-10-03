package com.nibou.niboucustomer.adapters;

import android.content.Context;
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
import com.nibou.niboucustomer.callbacks.AppCallBack;
import com.nibou.niboucustomer.models.PreviousExpertModel;
import com.nibou.niboucustomer.utils.AppUtil;


public class AddSuperCarAdapter extends RecyclerView.Adapter<AddSuperCarAdapter.MyViewHolder> {

    private Context context;
    private PreviousExpertModel previousExpertModel;
    private PastEventListAdapter mListAdapter;

    public AddSuperCarAdapter(Context context, PreviousExpertModel previousExpertModel) {
        this.context = context;
        this.previousExpertModel = previousExpertModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_add_supercar, viewGroup, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
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

        showImage(myViewHolder.ivCar, "");

        if (true) {
            myViewHolder.tvFront.setVisibility(View.VISIBLE);
            showImage(myViewHolder.ivFrontDoc, "");
        } else {
            myViewHolder.tvFront.setVisibility(View.GONE);
            showImage(myViewHolder.ivFrontDoc, "");
        }

        if (true) {
            myViewHolder.tvBack.setVisibility(View.VISIBLE);
            showImage(myViewHolder.ivBackDoc, "");
        } else {
            myViewHolder.tvBack.setVisibility(View.GONE);
            showImage(myViewHolder.ivBackDoc, "");
        }
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
        return 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvFront, tvBack;
        private EditText etBrand, etModel, etColor;
        private ImageView ivBackDoc, ivFrontDoc, ivCar,imgCross;
        private RecyclerView rvEvents;
        private ConstraintLayout item;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            etBrand = itemView.findViewById(R.id.et_brand);
            etModel = itemView.findViewById(R.id.et_model);
            etColor = itemView.findViewById(R.id.et_color);

            tvFront = itemView.findViewById(R.id.tv_front);
            tvBack = itemView.findViewById(R.id.tv_back);

            ivFrontDoc = itemView.findViewById(R.id.ivFrontDoc);
            ivBackDoc = itemView.findViewById(R.id.ivBackDoc);
            ivCar = itemView.findViewById(R.id.ivCar);
            imgCross = itemView.findViewById(R.id.imgCross);
        }
    }
}

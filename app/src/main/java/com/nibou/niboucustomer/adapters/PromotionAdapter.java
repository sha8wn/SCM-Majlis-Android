package com.nibou.niboucustomer.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.activitys.RedeemActivity;
import com.nibou.niboucustomer.activitys.SignupActivity;
import com.nibou.niboucustomer.api.ApiClient;
import com.nibou.niboucustomer.api.ApiEndPoint;
import com.nibou.niboucustomer.api.ApiHandler;
import com.nibou.niboucustomer.callbacks.AppCallBack;
import com.nibou.niboucustomer.models.ListResponseModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.CustomTypefaceSpan;
import com.nibou.niboucustomer.utils.LocalPrefences;

import java.util.ArrayList;


public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<ListResponseModel.ModelList> modelList;
    private AppCallBack appCallBack;

    public PromotionAdapter(Context context, ArrayList<ListResponseModel.ModelList> modelList, AppCallBack appCallBack) {
        this.context = context;
        this.modelList = modelList;
        this.appCallBack = appCallBack;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.promotion_item, viewGroup, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.title.setText(modelList.get(position).getName());
        myViewHolder.tvName.setText(modelList.get(position).getPartner_name() + " , " + modelList.get(position).getPartner_location());
        loadImage(myViewHolder.icon, modelList.get(position).getPartner_img());
    }

    private void loadImage(ImageView imageView, String url) {
        if (url != null && !url.isEmpty())
            Glide.with(context).load(url).dontAnimate().centerCrop().placeholder(R.drawable.default_placeholder).error(R.drawable.default_placeholder).into(imageView);
    }


    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, title;
        private ImageView icon;
        private View item;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            tvName = itemView.findViewById(R.id.tvName);
            item = itemView.findViewById(R.id.item);
            item.setOnClickListener(v -> {
                if (appCallBack != null)
                    appCallBack.onSelect(modelList.get(getAdapterPosition()));
            });
        }
    }
}

package com.nibou.niboucustomer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.activitys.RSVPSpotActivity;
import com.nibou.niboucustomer.models.BrandModel;
import com.nibou.niboucustomer.utils.AppUtil;

import java.util.ArrayList;


public class RSVPCarAdapter extends RecyclerView.Adapter<RSVPCarAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<BrandModel> mList;

    private int selectedPosition = -1;

    public RSVPCarAdapter(Context context, ArrayList<BrandModel> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_rsvp_item, viewGroup, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        if (selectedPosition == position) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) myViewHolder.brand_card.getLayoutParams();
            layoutParams.height = (int) AppUtil.convertDpToPixel(270, context);
            myViewHolder.brand_card.setLayoutParams(layoutParams);
            myViewHolder.card.setBackground(ContextCompat.getDrawable(context, R.drawable.car_selected_drawable));
            RelativeLayout.LayoutParams iconLayoutParams = (RelativeLayout.LayoutParams) myViewHolder.icon.getLayoutParams();
            iconLayoutParams.height = (int) AppUtil.convertDpToPixel(180, context);
            iconLayoutParams.width = (int) AppUtil.convertDpToPixel(120, context);
            myViewHolder.icon.setLayoutParams(iconLayoutParams);
        } else {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) myViewHolder.brand_card.getLayoutParams();
            layoutParams.height = (int) AppUtil.convertDpToPixel(250, context);
            myViewHolder.brand_card.setLayoutParams(layoutParams);
            myViewHolder.card.setBackground(ContextCompat.getDrawable(context, R.drawable.car_unselected_drawable));
            RelativeLayout.LayoutParams iconLayoutParams = (RelativeLayout.LayoutParams) myViewHolder.icon.getLayoutParams();
            iconLayoutParams.height = (int) AppUtil.convertDpToPixel(150, context);
            iconLayoutParams.width = (int) AppUtil.convertDpToPixel(100, context);
            myViewHolder.icon.setLayoutParams(iconLayoutParams);
        }
        showImage(myViewHolder.icon, "https://images.financialexpress.com/2018/06/Brezza-1.png?w=420&h=280&imflag=true");

    }

    private void showImage(ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .fitCenter()
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name, model;
        private ImageView icon;
        private View card, brand_card;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            model = itemView.findViewById(R.id.model);
            icon = itemView.findViewById(R.id.icon);
            card = itemView.findViewById(R.id.card);
            brand_card = itemView.findViewById(R.id.brand_card);
            brand_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPosition == getAdapterPosition()) {
                        selectedPosition = -1;
                        if (context instanceof RSVPSpotActivity) {
                            ((RSVPSpotActivity) context).carSelectedCallBack(false, selectedPosition);
                        }
                    } else {
                        selectedPosition = getAdapterPosition();
                        if (context instanceof RSVPSpotActivity) {
                            ((RSVPSpotActivity) context).carSelectedCallBack(true, selectedPosition);
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }
}

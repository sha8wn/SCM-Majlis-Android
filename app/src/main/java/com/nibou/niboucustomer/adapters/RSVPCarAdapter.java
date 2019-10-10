package com.nibou.niboucustomer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.models.BrandModel;

import java.util.ArrayList;


public class RSVPCarAdapter extends RecyclerView.Adapter<RSVPCarAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<BrandModel> mList;

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
        if (position == 0) {
            myViewHolder.card.setBackground(ContextCompat.getDrawable(context, R.drawable.car_unselected_drawable));
        } else {
            myViewHolder.card.setBackground(ContextCompat.getDrawable(context, R.drawable.car_unselected_drawable));
        }
        showImage(myViewHolder.icon, "https://scmajlis.ae/files/past_events/4/imgs/1/dr1.jpeg");
    }

    private void showImage(ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .centerCrop()
                .placeholder(R.drawable.default_placeholder)
                .error(R.drawable.default_placeholder)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name, model;
        private ImageView icon;
        private View card;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            model = itemView.findViewById(R.id.model);
            icon = itemView.findViewById(R.id.icon);
            card = itemView.findViewById(R.id.card);
        }
    }
}

package com.nibou.niboucustomer.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.customviews.MyImageViewPagerActivity;

import java.util.ArrayList;


public class PastEventListAdapter extends RecyclerView.Adapter<PastEventListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> mImageList;

    public PastEventListAdapter(Context context, ArrayList<String> mImageList) {
        this.context = context;
        this.mImageList = mImageList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_item, viewGroup, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        if (position == 0) {
            myViewHolder.ivEventImage.setVisibility(View.GONE);
            myViewHolder.ivEvent.setVisibility(View.VISIBLE);
            showImage(myViewHolder.ivEvent, mImageList.get(position));
        } else {
            myViewHolder.ivEventImage.setVisibility(View.VISIBLE);
            myViewHolder.ivEvent.setVisibility(View.GONE);
            showImage(myViewHolder.ivEventImage, mImageList.get(position));
        }

        myViewHolder.ivEventImage.setOnClickListener(view -> {
            Intent mIntent = new Intent(context, MyImageViewPagerActivity.class);
            mIntent.putExtra("LIST", mImageList);
            mIntent.putExtra("INDEX", position);
            context.startActivity(mIntent);
        });
        myViewHolder.ivEvent.setOnClickListener(view -> {
            Intent mIntent = new Intent(context, MyImageViewPagerActivity.class);
            mIntent.putExtra("LIST", mImageList);
            mIntent.putExtra("INDEX", position);
            context.startActivity(mIntent);
        });
//        myViewHolder.item.setTag(position);
//        myViewHolder.item.setOnClickListener(v -> {
//            if (context instanceof ExpertListActivity) {
//                ((ExpertListActivity) context).onAdapterClick(Integer.parseInt(v.getTag().toString()));
//            }
//        });
//        myViewHolder.expert_name.setText(previousExpertModel.getData().get(position).getAttributes().getName());
//        showImage(myViewHolder.circleImageView, AppConstant.FILE_BASE_URL + previousExpertModel.getData().get(position).getAttributes().getAvatar().getUrl());
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
        return 5;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDate, tvTime, tvName, tvLoc, tvPerson;
        private ImageView ivEvent, ivEventImage;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivEvent = itemView.findViewById(R.id.ivEvent);
            ivEventImage = itemView.findViewById(R.id.ivEventImage);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvName = itemView.findViewById(R.id.tvName);
            tvLoc = itemView.findViewById(R.id.tvLoc);
            tvPerson = itemView.findViewById(R.id.tvPerson);
        }
    }
}

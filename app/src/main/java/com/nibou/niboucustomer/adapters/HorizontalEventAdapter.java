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


public class HorizontalEventAdapter extends RecyclerView.Adapter<HorizontalEventAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> mImageList;

    public HorizontalEventAdapter(Context context, ArrayList<String> mImageList) {
        this.context = context;
        this.mImageList = mImageList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.past_event_item, viewGroup, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        showImage(myViewHolder.image, mImageList.get(position));

        if (position == 0) {
            myViewHolder.ivEvent.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.ivEvent.setVisibility(View.INVISIBLE);
        }
    }

    private void showImage(ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
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
        private ImageView image;
        private View ivEvent, card;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            ivEvent = itemView.findViewById(R.id.ivEvent);
            image = itemView.findViewById(R.id.image);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvName = itemView.findViewById(R.id.tvName);
            tvLoc = itemView.findViewById(R.id.tvLoc);
            tvPerson = itemView.findViewById(R.id.tvPerson);

            card.setOnClickListener(view -> {
                Intent mIntent = new Intent(context, MyImageViewPagerActivity.class);
                mIntent.putExtra("LIST", mImageList);
                mIntent.putExtra("INDEX", getAdapterPosition());
                context.startActivity(mIntent);
            });
        }
    }
}

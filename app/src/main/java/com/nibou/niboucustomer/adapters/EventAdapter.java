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
import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.activitys.AddSuperCarActivity;
import com.nibou.niboucustomer.activitys.EventDetailActivity;
import com.nibou.niboucustomer.callbacks.AppCallBack;
import com.nibou.niboucustomer.customviews.MyImageViewPagerActivity;
import com.nibou.niboucustomer.utils.AppUtil;

import java.util.ArrayList;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private Context context;

    public EventAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scm_event_item, viewGroup, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        showImage(myViewHolder.ivEventImage, "https://scmajlis.ae/files/past_events/1/imgs/2/past_2.jpeg");
    }

    private void showImage(ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .centerCrop()
                .placeholder(R.drawable.empty_image_drawable)
                .error(R.drawable.empty_image_drawable)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDate, tvTime, tvName, tvLoc, tvPerson;
        private ImageView ivEventImage;
        private View goingView, card;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivEventImage = itemView.findViewById(R.id.ivEventImage);

            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvName = itemView.findViewById(R.id.tvName);
            tvLoc = itemView.findViewById(R.id.tvLoc);
            tvPerson = itemView.findViewById(R.id.tvPerson);

            goingView = itemView.findViewById(R.id.goingView);
            goingView.setOnClickListener(v -> AppDialogs.getInstance().openGoingToListScreen("Drag Race", context, item -> {
            }));

            card = itemView.findViewById(R.id.card);
            card.setOnClickListener(v -> {
                Intent intent = new Intent(context, EventDetailActivity.class);
                intent.putExtra("type", "setting");
                context.startActivity(intent);
            });

        }
    }
}

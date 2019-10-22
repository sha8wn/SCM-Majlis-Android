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
import com.nibou.niboucustomer.activitys.EventDetailActivity;
import com.nibou.niboucustomer.models.ListResponseModel;
import com.nibou.niboucustomer.utils.DateFormatUtil;

import java.util.ArrayList;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<ListResponseModel.ModelList> modelList;

    public EventAdapter(Context context, ArrayList<ListResponseModel.ModelList> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scm_event_item, viewGroup, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        loadImage(myViewHolder.ivEventImage, modelList.get(position).getImg());
        myViewHolder.tvName.setText(modelList.get(position).getName());
        myViewHolder.tvLoc.setText(modelList.get(position).getLocation());
        myViewHolder.tvDate.setText(DateFormatUtil.getRequiredDateFormat(DateFormatUtil.getMilliesFromServerDate(modelList.get(position).getDate()), "dd MMMM yy"));
        myViewHolder.tvTime.setText(modelList.get(position).getStart() + " - " + modelList.get(position).getEnd());

        if (modelList.get(position).getFee() != null && !modelList.get(position).getFee().equals("0")) {
            myViewHolder.tvCost.setText(modelList.get(position).getFee() + " AED");
        } else {
            myViewHolder.tvCost.setText(context.getString(R.string.free));
        }

        if (modelList.get(position).getStatus() != null && !modelList.get(position).getStatus().equalsIgnoreCase("closed")) {
            myViewHolder.ivLive.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.ivLive.setVisibility(View.INVISIBLE);
        }

        if (modelList.get(position).getLimit_guests() != null && !modelList.get(position).getLimit_guests().equals("0")) {
            myViewHolder.tvPerson.setVisibility(View.VISIBLE);
            myViewHolder.tvPerson.setText(getLeftSpot(modelList.get(position)) + " " + context.getString(R.string.spots_remaining));
        } else {
            myViewHolder.tvPerson.setVisibility(View.INVISIBLE);
        }

        if (modelList.get(position).getUsers() != null && modelList.get(position).getUsers().size() > 0) {
            myViewHolder.goingView.setVisibility(View.VISIBLE);
            myViewHolder.tvGoing.setText(modelList.get(position).getUsers().size() + " " + context.getString(R.string.going));
        } else {
            myViewHolder.goingView.setVisibility(View.INVISIBLE);
        }
    }

    private String getLeftSpot(ListResponseModel.ModelList modelList) {
        if (modelList.getUsers() != null && modelList.getUsers().size() > 0) {
            int guest = 0;
            for (int i = 0; i < modelList.getUsers().size(); i++) {
                if (modelList.getUsers().get(i).getGuests() != null && !modelList.getUsers().get(i).getGuests().isEmpty())
                    guest = guest + Integer.parseInt(modelList.getUsers().get(i).getGuests());
            }
            return String.valueOf(Integer.parseInt(modelList.getLimit_guests()) - guest);
        } else {
            return modelList.getLimit_guests();
        }
    }

    private void loadImage(ImageView imageView, String url) {
        Glide.with(context).load(url).centerCrop().placeholder(R.drawable.place_holder).error(R.drawable.place_holder).into(imageView);
    }

    @Override
    public int getItemCount() {
        if (modelList != null)
            return modelList.size();
        else return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDate, tvTime, tvName, tvLoc, tvPerson, tvCost, tvGoing;
        private ImageView ivEventImage, ivLive;
        private View goingView, card;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivEventImage = itemView.findViewById(R.id.ivEventImage);

            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvName = itemView.findViewById(R.id.tvName);
            tvLoc = itemView.findViewById(R.id.tvLoc);
            tvPerson = itemView.findViewById(R.id.tvPerson);
            tvCost = itemView.findViewById(R.id.tvCost);
            ivLive = itemView.findViewById(R.id.ivLive);
            tvGoing = itemView.findViewById(R.id.tvGoing);

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

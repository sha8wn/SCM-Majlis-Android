package com.nibou.niboucustomer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.activitys.PastEventActivity;
import com.nibou.niboucustomer.models.EventResponseModel;
import com.nibou.niboucustomer.models.PreviousExpertModel;

import java.util.ArrayList;


public class PastEventAdapter extends RecyclerView.Adapter<PastEventAdapter.MyViewHolder> {

    private Context context;
    private EventResponseModel eventResponseModel;

    public PastEventAdapter(Context context, EventResponseModel eventResponseModel) {
        this.context = context;
        this.eventResponseModel = eventResponseModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recyclerview, viewGroup, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        if (position == getItemCount() - 1) {
            if (eventResponseModel.getPast_events().getN() < Integer.parseInt(eventResponseModel.getPast_events().getNumRows())) {
                //call pagination api
//                if (context instanceof PastEventActivity) {
//                    ((PastEventActivity) context).getPastEventsNetworkCall((eventResponseModel.getPast_events().getN() + 1));
//                }
            }
        }
        myViewHolder.rvEvents.setAdapter(new HorizontalEventAdapter(context, eventResponseModel.getPast_events().getList().get(position)));
    }

    @Override
    public int getItemCount() {
        if (eventResponseModel != null && eventResponseModel.getPast_events() != null)
            return eventResponseModel.getPast_events().getList().size();
        else
            return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView rvEvents;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rvEvents = itemView.findViewById(R.id.rvEvents);
            rvEvents.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        }
    }
}

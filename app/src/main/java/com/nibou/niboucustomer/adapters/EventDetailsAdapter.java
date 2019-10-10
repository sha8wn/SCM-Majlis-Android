package com.nibou.niboucustomer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.callbacks.AppCallBack;
import com.nibou.niboucustomer.models.BrandModel;

import java.util.ArrayList;


public class EventDetailsAdapter extends RecyclerView.Adapter<EventDetailsAdapter.MyViewHolder> {

    private Context context;
    private boolean isCheckpointAdapter;
    private ArrayList<BrandModel> mList;

    public EventDetailsAdapter(Context context, boolean isCheckpointAdapter, ArrayList<BrandModel> mList) {
        this.context = context;
        this.isCheckpointAdapter = isCheckpointAdapter;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_detail_item, viewGroup, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        if (isCheckpointAdapter) {
            myViewHolder.brand_view.setVisibility(View.GONE);
            myViewHolder.checkpoint_view.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.brand_view.setVisibility(View.VISIBLE);
            myViewHolder.checkpoint_view.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (isCheckpointAdapter) {
            return 2;
        } else {
            return 10;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name, checkpoint_name, time, directions;
        private View checkpoint_view, brand_view;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            checkpoint_view = itemView.findViewById(R.id.checkpointView);
            brand_view = itemView.findViewById(R.id.brandView);
            checkpoint_name = itemView.findViewById(R.id.checkpoint_name);
            time = itemView.findViewById(R.id.time);
            directions = itemView.findViewById(R.id.directions);
        }
    }
}

package com.nibou.niboucustomer.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.callbacks.AppCallBack;
import com.nibou.niboucustomer.models.BrandModel;
import com.nibou.niboucustomer.models.ListResponseModel;
import com.nibou.niboucustomer.utils.AppUtil;

import java.util.ArrayList;


public class EventDetailsAdapter extends RecyclerView.Adapter<EventDetailsAdapter.MyViewHolder> {

    private Context context;
    private boolean isCheckpointAdapter;
    private ArrayList<ListResponseModel.ArrayItem> mList;
    private ArrayList<ListResponseModel.ModelList> modelLists;

    public EventDetailsAdapter(Context context, boolean isCheckpointAdapter, Object data) {
        this.context = context;
        this.isCheckpointAdapter = isCheckpointAdapter;
        if (isCheckpointAdapter) {
            modelLists = (ArrayList<ListResponseModel.ModelList>) data;
        } else {
            mList = (ArrayList<ListResponseModel.ArrayItem>) data;
        }
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
            myViewHolder.checkpoint_name.setText(modelLists.get(position).getName());
            myViewHolder.time.setText(modelLists.get(position).getHours() + ":" + modelLists.get(position).getMinutes());
        } else {
            myViewHolder.brand_view.setVisibility(View.VISIBLE);
            myViewHolder.checkpoint_view.setVisibility(View.GONE);
            myViewHolder.name.setText(mList.get(position).getName());
            loadImage(myViewHolder.icon, mList.get(position).getImg());
        }
    }

    private void loadImage(ImageView imageView, String url) {
        if (url != null && !url.isEmpty())
            Glide.with(context).load(url).dontAnimate().centerCrop().placeholder(R.drawable.logo).error(R.drawable.logo).into(imageView);
    }

    @Override
    public int getItemCount() {
        if (isCheckpointAdapter) {
            return modelLists.size();
        } else {
            return mList.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name, checkpoint_name, time, directions;
        private View checkpoint_view, brand_view;
        private ImageView icon;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            brand_view = itemView.findViewById(R.id.brandView);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);

            checkpoint_view = itemView.findViewById(R.id.checkpointView);
            checkpoint_name = itemView.findViewById(R.id.checkpoint_name);
            time = itemView.findViewById(R.id.time);
            directions = itemView.findViewById(R.id.directions);

            directions.setOnClickListener(v -> {
                Uri gmmIntentUri = Uri.parse("geo:" + modelLists.get(getAdapterPosition()).getLat() + "," + modelLists.get(getAdapterPosition()).getLng());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mapIntent);
                } else {
                    AppUtil.showToast(context, context.getString(R.string.google_map_error));
                }
            });
        }
    }
}

package com.nibou.niboucustomer.adapters;

import android.content.Context;
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
import com.nibou.niboucustomer.models.ListResponseModel;

import java.util.ArrayList;


public class GoingToAdapter extends RecyclerView.Adapter<GoingToAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<ListResponseModel.ArrayItem> mList;
    private AppCallBack mAppCallBack;

    public GoingToAdapter(Context context, ArrayList<ListResponseModel.ArrayItem> mList, AppCallBack mAppCallBack) {
        this.context = context;
        this.mList = mList;
        this.mAppCallBack = mAppCallBack;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.going_to_item, viewGroup, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        myViewHolder.title.setText(mList.get(position).getBrandName() + " - " + mList.get(position).getModelName());
        myViewHolder.tvName.setText(mList.get(position).getName());
        loadImage(myViewHolder.icon, mList.get(position).getImg());


    }

    private void loadImage(ImageView imageView, String url) {
        if (url != null && !url.isEmpty())
            Glide.with(context).load(url).dontAnimate().centerCrop().placeholder(R.drawable.default_placeholder).error(R.drawable.default_placeholder).into(imageView);
    }


    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        else return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, title;
        private ImageView icon;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}

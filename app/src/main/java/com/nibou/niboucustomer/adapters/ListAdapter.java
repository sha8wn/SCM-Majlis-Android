package com.nibou.niboucustomer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.callbacks.AppCallBack;
import com.nibou.niboucustomer.models.BrandModel;

import java.util.ArrayList;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<BrandModel> mList;
    int selectedItem = 0;
    private AppCallBack mAppCallBack;

    public ListAdapter(Context context, ArrayList<BrandModel> mList, AppCallBack mAppCallBack) {
        this.context = context;
        this.mList = mList;
        this.mAppCallBack = mAppCallBack;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.brand_list_item, viewGroup, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        myViewHolder.ivTick.setTag(position);
        myViewHolder.cvBrand.setOnClickListener(view -> {
            selectedItem = position;
            mAppCallBack.onSelect(mList.get(position).getName());
            notifyDataSetChanged();
        });

        if (selectedItem == position) {
            myViewHolder.tvName.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            myViewHolder.ivTick.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.tvName.setTextColor(context.getResources().getColor(R.color.white));
            myViewHolder.ivTick.setVisibility(View.GONE);
        }
        myViewHolder.tvName.setText(mList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private ConstraintLayout cvBrand;
        private ImageView ivTick;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTick = itemView.findViewById(R.id.ivTick);
            tvName = itemView.findViewById(R.id.tvName);
            cvBrand = itemView.findViewById(R.id.cvBrand);
        }
    }
}

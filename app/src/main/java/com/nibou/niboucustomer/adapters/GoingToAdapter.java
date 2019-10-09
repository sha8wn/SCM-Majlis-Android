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


public class GoingToAdapter extends RecyclerView.Adapter<GoingToAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<BrandModel> mList;
    private AppCallBack mAppCallBack;

    public GoingToAdapter(Context context, ArrayList<BrandModel> mList, AppCallBack mAppCallBack) {
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

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}

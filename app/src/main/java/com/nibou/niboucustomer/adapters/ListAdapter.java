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
import com.nibou.niboucustomer.models.EventResponseModel;
import com.nibou.niboucustomer.models.ListResponseModel;

import java.util.ArrayList;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private Context context;
    private ListResponseModel.Model listResponseModel;
    private AppCallBack mAppCallBack;
    private ListResponseModel.ModelList selectedId;

    public ListAdapter(Context context, ListResponseModel.Model listResponseModel, ListResponseModel.ModelList selectedId, AppCallBack mAppCallBack) {
        this.context = context;
        this.listResponseModel = listResponseModel;
        this.mAppCallBack = mAppCallBack;
        this.selectedId = selectedId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.brand_list_item, viewGroup, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        if(selectedId!=null&&selectedId.getId().equals(listResponseModel.getList().get(position).getId())){
            myViewHolder.tvName.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            myViewHolder.ivTick.setVisibility(View.VISIBLE);
        }else{
            myViewHolder.tvName.setTextColor(context.getResources().getColor(R.color.white));
            myViewHolder.ivTick.setVisibility(View.GONE);
        }
        myViewHolder.tvName.setText(listResponseModel.getList().get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (listResponseModel != null && listResponseModel.getList() != null)
            return listResponseModel.getList().size();
        return 0;
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
            cvBrand.setOnClickListener(view -> {
                mAppCallBack.onSelect(listResponseModel.getList().get(getAdapterPosition()));
            });
        }
    }
}

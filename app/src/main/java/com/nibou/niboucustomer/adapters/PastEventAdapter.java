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
import com.nibou.niboucustomer.models.PreviousExpertModel;

import java.util.ArrayList;


public class PastEventAdapter extends RecyclerView.Adapter<PastEventAdapter.MyViewHolder> {

    private Context context;
    private PreviousExpertModel previousExpertModel;
    private HorizontalEventAdapter mListAdapter;

    public PastEventAdapter(Context context, PreviousExpertModel previousExpertModel) {
        this.context = context;
        this.previousExpertModel = previousExpertModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recyclerview, viewGroup, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        ArrayList<String> mImageList = new ArrayList<>();
        mImageList.add("http://homepages.cae.wisc.edu/~ece533/images/airplane.png");
        mImageList.add("http://homepages.cae.wisc.edu/~ece533/images/arctichare.png");
        mImageList.add("http://homepages.cae.wisc.edu/~ece533/images/boat.png");
        mImageList.add("http://homepages.cae.wisc.edu/~ece533/images/fruits.png");
        mImageList.add("http://homepages.cae.wisc.edu/~ece533/images/pool.png");

        myViewHolder.rvEvents.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        mListAdapter = new HorizontalEventAdapter(context, mImageList);
        myViewHolder.rvEvents.setAdapter(mListAdapter);

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView rvEvents;
        private ConstraintLayout item;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            rvEvents = itemView.findViewById(R.id.rvEvents);
        }
    }
}

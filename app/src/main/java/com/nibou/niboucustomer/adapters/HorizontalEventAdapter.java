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
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.customviews.MyImageViewPagerActivity;
import com.nibou.niboucustomer.models.ListResponseModel;
import com.nibou.niboucustomer.utils.DateFormatUtil;


public class HorizontalEventAdapter extends RecyclerView.Adapter<HorizontalEventAdapter.MyViewHolder> {

    private Context context;
    private ListResponseModel.ModelList eventList;

    public HorizontalEventAdapter(Context context, ListResponseModel.ModelList eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.past_event_item, viewGroup, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        if (position < eventList.getImgs().size())
            showImage(myViewHolder.image, eventList.getImgs().get(position).getImg());
        if (position == 0) {
            myViewHolder.ivEvent.setVisibility(View.VISIBLE);
            myViewHolder.tvName.setText(eventList.getName());
            myViewHolder.tvLoc.setText(eventList.getLocation());
            myViewHolder.tvPerson.setText(eventList.getParticipants() + " participants");
            myViewHolder.tvDate.setText(DateFormatUtil.getRequiredDateFormat(DateFormatUtil.getMilliesFromServerDate(eventList.getDate()), "dd MMMM yy"));
            myViewHolder.tvTime.setText(DateFormatUtil.getRequiredDateFormat(DateFormatUtil.getMilliesFromServerDate(eventList.getDate()), "hh:mm a").replace(".", ""));
        } else {
            myViewHolder.ivEvent.setVisibility(View.INVISIBLE);
        }
    }

    private void showImage(ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.place_holder)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        if (eventList.getImgs() != null && eventList.getImgs().size() > 0)
            return eventList.getImgs().size();
        else
            return 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDate, tvTime, tvName, tvLoc, tvPerson;
        private ImageView image;
        private View ivEvent, card;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            ivEvent = itemView.findViewById(R.id.ivEvent);
            image = itemView.findViewById(R.id.image);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvName = itemView.findViewById(R.id.tvName);
            tvLoc = itemView.findViewById(R.id.tvLoc);
            tvPerson = itemView.findViewById(R.id.tvPerson);

            card.setOnClickListener(view -> {
                Intent mIntent = new Intent(context, MyImageViewPagerActivity.class);
                mIntent.putExtra("LIST", eventList.getImgs());
                mIntent.putExtra("INDEX", getAdapterPosition());
                context.startActivity(mIntent);
            });
        }
    }
}

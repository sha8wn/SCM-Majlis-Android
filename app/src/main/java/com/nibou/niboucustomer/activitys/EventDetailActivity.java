package com.nibou.niboucustomer.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.adapters.EventDetailsAdapter;
import com.nibou.niboucustomer.adapters.GoingToAdapter;
import com.nibou.niboucustomer.api.ApiClient;
import com.nibou.niboucustomer.api.ApiEndPoint;
import com.nibou.niboucustomer.api.ApiHandler;
import com.nibou.niboucustomer.databinding.ActivityEventDetailsBinding;
import com.nibou.niboucustomer.databinding.FragmentProfileBinding;
import com.nibou.niboucustomer.models.ProfileModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;

import java.util.HashMap;

public class EventDetailActivity extends BaseActivity {
    private ActivityEventDetailsBinding binding;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_details);
        context = this;
        binding.toolbar.findViewById(R.id.back_arrow).setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            onBackPressed();
        });

        binding.goingView.setOnClickListener(v -> AppDialogs.getInstance().openGoingToListScreen("Drag Race", context, item -> {
        }));

        binding.btnReserveSpot.setOnClickListener(v -> {
            Intent intent = new Intent(context, RSVPSpotActivity.class);
            context.startActivity(intent);
        });

        showEventData();
    }

    private void showEventData() {
        showImage(binding.ivEventImage, "https://scmajlis.ae/files/past_events/1/imgs/2/past_2.jpeg");

        binding.rvCheckpoints.setLayoutManager(new LinearLayoutManager(context));
        binding.rvBrands.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        binding.rvPartners.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        binding.rvCheckpoints.setAdapter(new EventDetailsAdapter(context, true, null));
        binding.rvBrands.setAdapter(new EventDetailsAdapter(context, false, null));
        binding.rvPartners.setAdapter(new EventDetailsAdapter(context, false, null));

    }

    private void showImage(ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .centerCrop()
                .placeholder(R.drawable.default_placeholder)
                .error(R.drawable.default_placeholder)
                .into(imageView);
    }
}

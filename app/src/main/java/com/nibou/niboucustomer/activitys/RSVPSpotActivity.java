package com.nibou.niboucustomer.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.adapters.EventDetailsAdapter;
import com.nibou.niboucustomer.adapters.RSVPCarAdapter;
import com.nibou.niboucustomer.databinding.ActivityEventDetailsBinding;
import com.nibou.niboucustomer.databinding.ActivityReserveSpotBinding;
import com.nibou.niboucustomer.utils.AppUtil;

public class RSVPSpotActivity extends BaseActivity {
    private ActivityReserveSpotBinding binding;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reserve_spot);
        context = this;
        binding.toolbar.findViewById(R.id.back_arrow).setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            onBackPressed();
        });

        binding.revpMember.setOnClickListener(v -> {
            binding.revpMember.setAlpha(1);
            binding.revpGuest.setAlpha(0.5f);
            binding.revpMember.setBackground(ContextCompat.getDrawable(context, R.drawable.rsvp_red_corner_drawable));
            binding.revpGuest.setBackground(null);

        });

        binding.revpGuest.setOnClickListener(v -> {
            binding.revpMember.setAlpha(0.5f);
            binding.revpGuest.setAlpha(1);
            binding.revpGuest.setBackground(ContextCompat.getDrawable(context, R.drawable.rsvp_red_guest_corner_drawable));
            binding.revpMember.setBackground(null);
        });

        binding.rvList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        binding.rvList.setAdapter(new RSVPCarAdapter(context, null));
    }

    public void carSelectedCallBack(boolean isSelected, int position) {
        if (isSelected) {
            binding.btnReserveSpot.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_gradient));
        } else {
            binding.btnReserveSpot.setBackground(ContextCompat.getDrawable(context, R.drawable.grey_btn_gradient));
        }
    }
}

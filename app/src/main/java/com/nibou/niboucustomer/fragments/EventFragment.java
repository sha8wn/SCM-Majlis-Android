package com.nibou.niboucustomer.fragments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.*;

import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.activitys.AddSuperCarActivity;
import com.nibou.niboucustomer.activitys.PastEventActivity;
import com.nibou.niboucustomer.adapters.EventAdapter;
import com.nibou.niboucustomer.databinding.FragmentScmEventsBinding;

public class EventFragment extends Fragment {

    private FragmentScmEventsBinding binding;
    private Context context;

    private EventAdapter eventAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scm_events, container, false);
        context = getActivity();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {
        binding.tvCheckIn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PastEventActivity.class);
            intent.putExtra("type", "setting");
            startActivity(intent);
        });


        binding.rvEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventAdapter = new EventAdapter(getActivity());
        binding.rvEvents.setAdapter(eventAdapter);
    }
}
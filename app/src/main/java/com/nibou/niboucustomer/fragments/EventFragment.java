package com.nibou.niboucustomer.fragments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.*;

import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.activitys.PastEventActivity;
import com.nibou.niboucustomer.adapters.EventAdapter;
import com.nibou.niboucustomer.adapters.PastEventAdapter;
import com.nibou.niboucustomer.api.ApiClient;
import com.nibou.niboucustomer.api.ApiEndPoint;
import com.nibou.niboucustomer.api.ApiHandler;
import com.nibou.niboucustomer.databinding.FragmentScmEventsBinding;
import com.nibou.niboucustomer.models.ListResponseModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;

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

        binding.tvCheckIn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PastEventActivity.class);
            intent.putExtra(AppConstant.SCREEN_FLOW_FLAG, true);
            startActivity(intent);
        });

        if (AppUtil.isInternetAvailable(context)) {
            getEventNetworkCall(1);
        } else {
            AppUtil.showToast(context, getString(R.string.internet_error));
        }
    }

    public void getEventNetworkCall(int pageNumber) {
        AppDialogs.getInstance().showProgressBar(context, null, true);
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).getEventNetworkCall(LocalPrefences.getInstance().getString(context, AppConstant.TOKEN), 20, pageNumber, 1), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    ListResponseModel listResponseModel = (ListResponseModel) data;
                    if (listResponseModel.getEvents() != null && listResponseModel.getEvents().getList() != null && listResponseModel.getEvents().getList().size() > 0) {
                        binding.rvEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
                        eventAdapter = new EventAdapter(getActivity(), listResponseModel.getEvents().getList());
                        binding.rvEvents.setAdapter(eventAdapter);
                    }
                } else {
                    AppDialogs.getInstance().showInfoCustomDialog(context, context.getString(R.string.error).toUpperCase(), String.valueOf(data), context.getString(R.string.OK), null);
                }
            }

            @Override
            public void failed() {
                AppDialogs.getInstance().showProgressBar(context, null, false);
            }
        });
    }
}
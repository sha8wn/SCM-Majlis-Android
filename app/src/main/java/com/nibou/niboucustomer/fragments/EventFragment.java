package com.nibou.niboucustomer.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.*;

import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.HashMap;

public class EventFragment extends Fragment implements LocationListener {

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
            if (binding.tvCheckIn.getText().toString().equalsIgnoreCase(getString(R.string.check_in))) {
                if (AppUtil.isInternetAvailable(context)) {
                    getLocation();
                } else {
                    AppUtil.showToast(context, getString(R.string.internet_error));
                }
            } else {
                Intent intent = new Intent(getActivity(), PastEventActivity.class);
                intent.putExtra(AppConstant.SCREEN_FLOW_FLAG, true);
                startActivity(intent);
            }
        });


        if (AppUtil.isInternetAvailable(context)) {
            getEventNetworkCall(1);
        } else {
            AppUtil.showToast(context, getString(R.string.internet_error));
        }

    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
            return;
        }

        if (!isLocationEnabled()) {
            showSettingsAlert();
            return;
        }

        try {
            AppDialogs.getInstance().showProgressBar(context, null, true);
            Location location = null;
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isNetworkEnabled) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    locationReteriveSuccessfully(location);
                } else {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, this);
                }
            }
            if (isGPSEnabled) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    locationReteriveSuccessfully(location);
                } else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 50, this);
                }
            }
        } catch (Exception e) {
            AppDialogs.getInstance().showProgressDialog(context, null, "Retrieving Location...", false);
            e.printStackTrace();
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        alertDialog.setTitle("GPS is not Enabled!");
        alertDialog.setMessage("Do you want to turn on GPS?");
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 100);
        });
        alertDialog.setNegativeButton("No", (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }

    private boolean isLocationEnabled() {
        try {
            boolean gps_enabled = false;
            boolean network_enabled = false;
            LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (manager != null) {
                gps_enabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                network_enabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }
            return gps_enabled || network_enabled;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (isLocationEnabled()) {
                getLocation();
            } else {
                showSettingsAlert();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (isLocationEnabled()) {
                getLocation();
            }
        } else if (requestCode == 300) {
            if (AppUtil.isInternetAvailable(context)) {
                getEventNetworkCall(1);
            } else {
                AppUtil.showToast(context, getString(R.string.internet_error));
            }
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
                        eventAdapter = new EventAdapter(getActivity(), EventFragment.this, listResponseModel.getEvents().getList());
                        binding.rvEvents.setAdapter(eventAdapter);

//                        for (int i = 0; i < listResponseModel.getEvents().getList().size(); i++) {
//                            if (listResponseModel.getEvents().getList().get(i).getReservation() != 0 && listResponseModel.getEvents().getList().get(i).getStatus() != null && listResponseModel.getEvents().getList().get(i).getStatus().equalsIgnoreCase("Live")) {
//                                binding.tvCheckIn.setText(getString(R.string.check_in));
//                                break;
//                            } else {
//                                binding.tvCheckIn.setText(getString(R.string.past_event));
//                            }
//                        }
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

    private void locationReteriveSuccessfully(Location location) {
        Log.e("LL", "" + location.getLatitude() + "," + location.getLongitude());
        if (AppUtil.isInternetAvailable(context)) {
            checkInNetworkCall(location);
        } else {
            AppUtil.showToast(context, getString(R.string.internet_error));
        }
    }

    public void checkInNetworkCall(Location location) {
        AppDialogs.getInstance().showProgressBar(context, null, true);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("lat", location.getLatitude());
        parameters.put("lng", location.getLongitude());
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).checkInNetworkCall(LocalPrefences.getInstance().getString(context, AppConstant.TOKEN), parameters), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    AppDialogs.getInstance().showInfoCustomDialog(context, context.getString(R.string.success).toUpperCase(), context.getString(R.string.user_check_in_success_alert), context.getString(R.string.OK), null);
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


    @Override
    public void onLocationChanged(Location location) {
        locationReteriveSuccessfully(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
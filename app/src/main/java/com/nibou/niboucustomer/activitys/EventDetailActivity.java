package com.nibou.niboucustomer.activitys;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nibou.niboucustomer.Dialogs.AppDialogs;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.adapters.EventAdapter;
import com.nibou.niboucustomer.adapters.EventDetailsAdapter;
import com.nibou.niboucustomer.adapters.GoingToAdapter;
import com.nibou.niboucustomer.api.ApiClient;
import com.nibou.niboucustomer.api.ApiEndPoint;
import com.nibou.niboucustomer.api.ApiHandler;
import com.nibou.niboucustomer.databinding.ActivityEventDetailsBinding;
import com.nibou.niboucustomer.databinding.FragmentProfileBinding;
import com.nibou.niboucustomer.fragments.EventFragment;
import com.nibou.niboucustomer.models.ListResponseModel;
import com.nibou.niboucustomer.models.ProfileModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.DateFormatUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;

import java.util.ArrayList;
import java.util.HashMap;

public class EventDetailActivity extends BaseActivity implements LocationListener {
    private ActivityEventDetailsBinding binding;
    private Context context;

    private ListResponseModel.ModelList modelList;

    private String checkInId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_details);
        context = this;
        binding.toolbar.findViewById(R.id.back_arrow).setOnClickListener(v -> {
            AppUtil.hideKeyBoard(context);
            onBackPressed();
        });
        binding.parent.setVisibility(View.GONE);


        binding.btnReserveSpot.setOnClickListener(v -> {
            if (binding.btnReserveSpot.getText().toString().equals(getString(R.string.unreserve))) {
                if (AppUtil.isInternetAvailable(context)) {
                    deleteReserveSpotNetworkCall();
                } else {
                    AppUtil.showToast(context, getString(R.string.internet_error));
                }
            } else {
                Intent intent = new Intent(context, RSVPSpotActivity.class);
                intent.putExtra(AppConstant.EVENT_MODEL, modelList);
                startActivityForResult(intent, 100);
            }
        });

        modelList = (ListResponseModel.ModelList) getIntent().getSerializableExtra(AppConstant.EVENT_MODEL);

        if (AppUtil.isInternetAvailable(context)) {
            getEventDetailNetworkCall();
        } else {
            AppUtil.showToast(context, getString(R.string.internet_error));
        }
    }

    private void showEventData() {

        binding.goingView.setOnClickListener(v -> AppDialogs.getInstance().openGoingToListScreen(modelList.getName(), modelList.getUsers(), context, item -> {
        }));

        binding.aboutEvent.setText(modelList.getText());
        loadImage(binding.ivEventImage, modelList.getImg());
        binding.tvName.setText(modelList.getName());
        binding.tvLoc.setText(modelList.getLocation());
        binding.tvDate.setText(DateFormatUtil.getRequiredDateFormat(DateFormatUtil.getMilliesFromServerDate(modelList.getDate()), "dd MMMM yy"));
        binding.tvTime.setText(modelList.getStart() + " - " + modelList.getEnd());

        if (modelList.getFee() != null && !modelList.getFee().equals("0")) {
            binding.tvCost.setText(modelList.getFee() + " AED");
            binding.tvCost.setTextColor(ContextCompat.getColor(context, R.color.app_disable_color));
            binding.tvCost.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_gradient_white));
        } else {
            binding.tvCost.setText(context.getString(R.string.free));
            binding.tvCost.setTextColor(Color.WHITE);
            binding.tvCost.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_gradient_blue));
        }

        if (modelList.getStatus() != null && modelList.getStatus().equalsIgnoreCase("Live")) {
            binding.ivLive.setVisibility(View.VISIBLE);
        } else {
            binding.ivLive.setVisibility(View.INVISIBLE);
        }

        if (modelList.getLimit_guests() != null && !modelList.getLimit_guests().equals("0")) {
            binding.tvPerson.setVisibility(View.VISIBLE);
            binding.tvPerson.setText(getLeftSpot(modelList) + " " + context.getString(R.string.spots_remaining));
        } else {
            binding.tvPerson.setVisibility(View.INVISIBLE);
        }

        if (modelList.getUsers() != null && modelList.getUsers().size() > 0) {
            binding.goingView.setVisibility(View.VISIBLE);
            binding.tvGoing.setText(modelList.getUsers().size() + " " + context.getString(R.string.going));
            loadProfileImage(binding.icon1, modelList.getUsers().get(0).getImg());

            if (modelList.getUsers().size() > 1) {
                loadProfileImage(binding.icon2, modelList.getUsers().get(1).getImg());
                binding.icon2.setVisibility(View.VISIBLE);
            } else
                binding.icon2.setVisibility(View.GONE);

            if (modelList.getUsers().size() > 2) {
                loadProfileImage(binding.icon3, modelList.getUsers().get(2).getImg());
                binding.icon3.setVisibility(View.VISIBLE);
            } else
                binding.icon3.setVisibility(View.GONE);

            if (modelList.getUsers().size() > 3) {
                loadProfileImage(binding.icon4, modelList.getUsers().get(3).getImg());
                binding.icon4.setVisibility(View.VISIBLE);
            } else
                binding.icon4.setVisibility(View.GONE);
        } else {
            binding.goingView.setVisibility(View.INVISIBLE);
        }

        if (modelList.getReservation() == 0) {
            binding.btnReserveSpot.setText(getString(R.string.reserve_my_spot));
            binding.btnReserveSpot.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_gradient));
        } else {
            if (modelList.getStatus() != null && modelList.getStatus().equals("Live")) {
                binding.btnReserveSpot.setVisibility(View.GONE);
            } else {
                binding.btnReserveSpot.setText(getString(R.string.unreserve));
                binding.btnReserveSpot.setBackground(ContextCompat.getDrawable(context, R.drawable.grey_btn_gradient));
            }
        }

        if (Integer.parseInt(modelList.getLimit_guests()) != 0) {
            if (Integer.parseInt(getLeftSpot(modelList)) <= 0) {
                binding.btnReserveSpot.setVisibility(View.VISIBLE);
                binding.btnReserveSpot.setEnabled(false);
                binding.btnReserveSpot.setText(getString(R.string.fully_booked));
                binding.btnReserveSpot.setBackground(ContextCompat.getDrawable(context, R.drawable.grey_btn_gradient));
            }
        }

        if (modelList.getBrands() != null && modelList.getBrands().size() > 0) {
            binding.rvBrands.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            binding.rvBrands.setAdapter(new EventDetailsAdapter(context, false, false, modelList.getBrands()));
            binding.rvBrands.setVisibility(View.VISIBLE);
            binding.t3.setVisibility(View.VISIBLE);
        } else {
            binding.rvBrands.setVisibility(View.GONE);
            binding.t3.setVisibility(View.GONE);
        }

        if (modelList.getPartners() != null && modelList.getPartners().size() > 0) {
            binding.rvPartners.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            binding.rvPartners.setAdapter(new EventDetailsAdapter(context, false, false, modelList.getPartners()));
            binding.rvPartners.setVisibility(View.VISIBLE);
            binding.t4.setVisibility(View.VISIBLE);
        } else {
            binding.rvPartners.setVisibility(View.GONE);
            binding.t4.setVisibility(View.GONE);
        }
    }

    private String getLeftSpot(ListResponseModel.ModelList modelList) {
        if (modelList.getUsers() != null && modelList.getUsers().size() > 0) {
            int guest = 0;
            for (int i = 0; i < modelList.getUsers().size(); i++) {
                if (modelList.getUsers().get(i).getGuests() != null && !modelList.getUsers().get(i).getGuests().isEmpty())
                    guest = guest + Integer.parseInt(modelList.getUsers().get(i).getGuests());
            }
            if ((Integer.parseInt(modelList.getLimit_guests()) - guest) < 0)
                return "0";
            else
                return String.valueOf(Integer.parseInt(modelList.getLimit_guests()) - guest);
        } else {
            return modelList.getLimit_guests();
        }
    }

    private void loadImage(ImageView imageView, String url) {
        if (url != null && !url.isEmpty()) {
            Glide.with(this).load(url).centerCrop().placeholder(R.drawable.place_holder).error(R.drawable.place_holder).dontAnimate().into(imageView);
        }
    }

    private void loadProfileImage(ImageView imageView, String url) {
        if (url != null && !url.isEmpty())
            Glide.with(this).load(url).centerCrop().placeholder(R.drawable.default_placeholder).error(R.drawable.default_placeholder).dontAnimate().into(imageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null) {
            if (AppUtil.isInternetAvailable(context)) {
                getEventDetailNetworkCall();
            } else {
                AppUtil.showToast(context, getString(R.string.internet_error));
            }
        } else if (requestCode == 200) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (isLocationEnabled()) {
                getLocation(this.checkInId);
            }
        }
    }


    public void getLocation(String checkInId) {
        this.checkInId = checkInId;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 300);
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
            startActivityForResult(intent, 200);
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
        if (requestCode == 300) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (isLocationEnabled()) {
                getLocation(this.checkInId);
            } else {
                showSettingsAlert();
            }
        }
    }

    private void locationReteriveSuccessfully(Location location) {
        Log.e("LL", "" + location.getLatitude() + "," + location.getLongitude());
        if (AppUtil.isInternetAvailable(context)) {
            checkInNetworkCall(location);
        } else {
            AppUtil.showToast(context, getString(R.string.internet_error));
        }
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

    public void getEventDetailNetworkCall() {
        AppDialogs.getInstance().showProgressBar(context, null, true);
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).getEventDetailNetworkCall(LocalPrefences.getInstance().getString(context, AppConstant.TOKEN), modelList.getId()), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                if (isSuccess) {
                    ListResponseModel listResponseModel = (ListResponseModel) data;
                    if (listResponseModel.getEvents() != null && listResponseModel.getEvents().getList() != null && listResponseModel.getEvents().getList().size() > 0) {
                        modelList = listResponseModel.getEvents().getList().get(0);
                        showEventData();
                    }

                    if (binding != null)
                        binding.parent.setVisibility(View.VISIBLE);

                    if (AppUtil.isInternetAvailable(context)) {
                        getCheckpointNetworkCall();
                    } else {
                        AppUtil.showToast(context, getString(R.string.internet_error));
                    }

                } else {
                    AppDialogs.getInstance().showProgressBar(context, null, false);
                    AppDialogs.getInstance().showInfoCustomDialog(context, context.getString(R.string.error).toUpperCase(), String.valueOf(data), context.getString(R.string.OK), null);
                }
            }

            @Override
            public void failed() {
                AppDialogs.getInstance().showProgressBar(context, null, false);
            }
        });
    }

    private void getCheckpointNetworkCall() {
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).getCheckpointNetworkCall(LocalPrefences.getInstance().getString(context, AppConstant.TOKEN), 1000, 1, modelList.getId()), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    ListResponseModel listResponseModel = (ListResponseModel) data;
                    if (listResponseModel.getCheckpoints().getList() != null && listResponseModel.getCheckpoints().getList().size() > 0) {
                        binding.rvCheckpoints.setVisibility(View.VISIBLE);
                        binding.t2.setVisibility(View.VISIBLE);
                        binding.rvCheckpoints.setLayoutManager(new LinearLayoutManager(context));
                        if (modelList.getReservation() != 0 && modelList != null && modelList.getStatus().equalsIgnoreCase("Live")) {
                            binding.rvCheckpoints.setAdapter(new EventDetailsAdapter(context, true, true, listResponseModel.getCheckpoints().getList()));
                        } else {
                            binding.rvCheckpoints.setAdapter(new EventDetailsAdapter(context, true, false, listResponseModel.getCheckpoints().getList()));
                        }
                    }
                }
            }

            @Override
            public void failed() {
                AppDialogs.getInstance().showProgressBar(context, null, false);
            }
        });
    }

    private void deleteReserveSpotNetworkCall() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("user", LocalPrefences.getInstance().getString(context, AppConstant.USER_ID));
        parameters.put("event", modelList.getId());

        AppDialogs.getInstance().showProgressBar(context, null, true);
        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).deleteReserveSpotNetworkCall(LocalPrefences.getInstance().getString(context, AppConstant.TOKEN), parameters), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    AppDialogs.getInstance().showCustomDialog(context, getString(R.string.success).toUpperCase(), getString(R.string.unreserve_spot_alert), getString(R.string.OK), getResources().getColor(R.color.green), status -> {
                        if (AppUtil.isInternetAvailable(context)) {
                            getEventDetailNetworkCall();
                        } else {
                            AppUtil.showToast(context, getString(R.string.internet_error));
                        }
                    });
                } else {
                    AppDialogs.getInstance().showCustomDialog(context, getString(R.string.error).toUpperCase(), String.valueOf(data), getString(R.string.OK), getResources().getColor(R.color.colorPrimary), null);
                }
            }

            @Override
            public void failed() {
                AppDialogs.getInstance().showProgressBar(context, null, false);
            }
        });
    }

    public void checkInNetworkCall(Location location) {
        AppDialogs.getInstance().showProgressBar(context, null, true);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("lat", location.getLatitude());
        parameters.put("lng", location.getLongitude());
        parameters.put("checkpoint", Integer.parseInt(checkInId));


        ApiHandler.requestService(context, ApiClient.getClient().create(ApiEndPoint.class).checkInNetworkCall(LocalPrefences.getInstance().getString(context, AppConstant.TOKEN), parameters), new ApiHandler.CallBack() {
            @Override
            public void success(boolean isSuccess, Object data) {
                AppDialogs.getInstance().showProgressBar(context, null, false);
                if (isSuccess) {
                    AppDialogs.getInstance().showCustomDialog(context, getString(R.string.success).toUpperCase(), getString(R.string.user_check_in_success_alert), getString(R.string.OK), getResources().getColor(R.color.green), status -> {
                        if (AppUtil.isInternetAvailable(context)) {
                            AppDialogs.getInstance().showProgressBar(context, null, true);
                            getCheckpointNetworkCall();
                        } else {
                            AppUtil.showToast(context, getString(R.string.internet_error));
                        }

                    });
                } else {
                    AppDialogs.getInstance().showCustomDialog(context, getString(R.string.error).toUpperCase(), String.valueOf(data), getString(R.string.OK), getResources().getColor(R.color.colorPrimary), null);
                }
            }

            @Override
            public void failed() {
                AppDialogs.getInstance().showProgressBar(context, null, false);
            }
        });
    }
}

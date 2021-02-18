package com.dollop.dukaadriver.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.Utility;
import com.dollop.dukaadriver.UtilityTools.Utils;

import com.dollop.dukaadriver.activity.VehicleDetailActivity;
import com.dollop.dukaadriver.adapter.CompanyDriverHomeAdapter;
import com.dollop.dukaadriver.firebase.Config;

import com.dollop.dukaadriver.model.HomeDTO;
import com.dollop.dukaadriver.UtilityTools.GoogleApisHandle;
import com.dollop.dukaadriver.UtilityTools.IOnBackPressed;
import com.dollop.dukaadriver.activity.AddNewVehicleActivity;
import com.dollop.dukaadriver.activity.HomeActivity;
import com.dollop.dukaadriver.activity.RegistrationActivity;
import com.dollop.dukaadriver.adapter.HomeAdapter;
import com.dollop.dukaadriver.adapter.SaveVehicalAdapter;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.OrderDTO;
import com.dollop.dukaadriver.model.VehicalDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    Button btnselect_vehicleId;
    ImageView iv_close_vehicle, image, no_data_image, wheeler_image;
    Dialog selectvehicle;
    Button lv_applyBtn, add_driver_btn, add_vehicle_btn;
    HomeAdapter mHomeAdapter;
    ArrayList<OrderDTO> mOrderDTOArrayList;

    RecyclerView rv_select_vehicle;
    ArrayList<VehicalDTO> mVehicalDTOS;
    RecyclerView new_order_list;
    SwitchMaterial switch_driver_online;
    TextView vehical_model_name_tv, vehical_number_tv, status_tv, user_name_tv;

    LinearLayout vehical_img_LL, driver_status_ll, vehical_LL, courier_LL;
    private GoogleMap mMap;
    public GoogleApisHandle googleApisHandle;
    private Location location;

    double currentLatitude = 0.00;
    double currentLongitude = 0.00;

    int count = 0;
    LatLng currentLatLng;
    boolean doubleBackToExitPressedOnce = false;
    View root;
    static boolean active = false;

    SessionManager sessionManager;
    private Activity mActivity;
    private int exit = 0;
    private Handler handler;
    String m_deviceId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mActivity = getActivity();
        root = inflater.inflate(R.layout.fragment_home, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        googleApisHandle = GoogleApisHandle.getInstance(getActivity());
        sessionManager = new SessionManager(getActivity());

        initialization(root);


        root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (exit != 0) {

                            getActivity().finish();
                            exit = 0;
                        } else {
                            handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    exit++;
                                }
                            }, 500);
                            Utils.T(getActivity(), "Press back again to exit");
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        getDeviceId(getActivity());
        updateMethod();
        return root;
    }

    public String getDeviceId(Context context) {


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            m_deviceId = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                m_deviceId = mTelephony.getDeviceId();
            } else {
                m_deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }
        Log.e("m_deviceId>", m_deviceId);
        return m_deviceId;
    }



    private void initialization(View root) {

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        btnselect_vehicleId = root.findViewById(R.id.btnselect_vehicleId);
        new_order_list = root.findViewById(R.id.new_order_list);
        switch_driver_online = root.findViewById(R.id.switch_driver_online);
        vehical_model_name_tv = root.findViewById(R.id.vehical_model_name_tv);
        vehical_number_tv = root.findViewById(R.id.vehical_number_tv);
        driver_status_ll = root.findViewById(R.id.driver_status_ll);
        image = root.findViewById(R.id.image);
        vehical_img_LL = root.findViewById(R.id.vehical_img_LL);
        status_tv = root.findViewById(R.id.status_tv);
        user_name_tv = root.findViewById(R.id.user_name_tv);
        vehical_LL = root.findViewById(R.id.vehical_LL);
        add_driver_btn = root.findViewById(R.id.add_driver_btn);
        wheeler_image = root.findViewById(R.id.wheeler_image);
        courier_LL = root.findViewById(R.id.courier_LL);
        add_vehicle_btn = root.findViewById(R.id.add_vehicle_btn);

        btnselect_vehicleId.setOnClickListener(this);
        add_driver_btn.setOnClickListener(this);
        add_vehicle_btn.setOnClickListener(this);
        mVehicalDTOS = new ArrayList<>();
        mOrderDTOArrayList = new ArrayList<>();


        switch_driver_online.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            getDriverStatus("Online");
                        } else {
                            getDriverStatus("Offline");
                        }

                    }
                });




        if (sessionManager.is_DRIVER()) {
            if (sessionManager.is_COMPANY_DRIVER()) {
            } else {
                getVehicalMethod();
            }
        }


        if (sessionManager.is_DRIVER()) {

            if (sessionManager.is_DRIVER_ONLINE_STATUS()) {
                switch_driver_online.setChecked(true);

                if (sessionManager.is_COMPANY_DRIVER()) {
                    vehical_LL.setVisibility(View.GONE);
                    CompanyDriverView_Order();
                } else {
                    getOrderMethod();

                }
            } else {
                switch_driver_online.setChecked(false);
            }


        } else {
            driver_status_ll.setVisibility(View.GONE);
            vehical_LL.setVisibility(View.GONE);
            courier_LL.setVisibility(View.VISIBLE);
            getCourire_ViewMethod();
        }

        if (switch_driver_online.isChecked()) {
            getDriverStatus("Online");
        } else {
            status_tv.setText("Offline");
        }

        if (getContext() != null) {
            if (sessionManager.getRegisterUser().getProfile_img() != null) {

                Glide.with(getActivity()).load(ApiClient.BASE_URL +
                        sessionManager.getRegisterUser().getProfile_img())
                        .error(R.drawable.individual_driver)
                        .into(image);


            }
        }
        user_name_tv.setText(sessionManager.getRegisterUser().getFullName());

    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
           // Utils.E("broadcast:Home Fragment");
            // if (active && ((HomeActivity) getActivity()).isFinishing()) {
            String message = intent.getStringExtra("Message");
            initialization(root);

            //Utils.E("message:Home Fragment" + message);

        }
    };


    private void ShowForReviewsDialog(String msg) {
        if (getContext() != null) {
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setMessage(msg)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //  startActivity(new Intent(PaymentProcessActivity.this,HomeActivity.class));
                            Log.e("SUCCESS", "SUCCESS");

                            dialog.dismiss();
                        }
                    }).show();

        }

    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    private void CompanyDriverView_Order() {
        mOrderDTOArrayList.clear();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", sessionManager.getRegisterUser().getId());

        Call<HomeDTO> call = apiService.courierOrder_assign_driver(hm);
        call.enqueue(new Callback<HomeDTO>() {
            @Override
            public void onResponse(Call<HomeDTO> call, Response<HomeDTO> response) {

                try {

                    HomeDTO body = response.body();

                    if (body.getStatus() == 200) {


                        mVehicalDTOS.clear();

                        mOrderDTOArrayList = body.getAllOrder();
                        mVehicalDTOS = mOrderDTOArrayList.get(0).getVehicle();
                        CompanyDriverHomeAdapter mHomeAdapter = new CompanyDriverHomeAdapter(getActivity(), mOrderDTOArrayList);
                        new_order_list.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));
                        new_order_list.setAdapter(mHomeAdapter);
                        mHomeAdapter.notifyDataSetChanged();

                        vehical_model_name_tv.setText(mVehicalDTOS.get(0).getVehicleName());
                        vehical_number_tv.setText(mVehicalDTOS.get(0).getVehicleRegistrionNumber());
                        Utils.E("Number ::"+mVehicalDTOS.get(0).getVehicleRegistrionNumber());
                        String vehical_type = (mVehicalDTOS.get(0).getVehicleType());

                        if (vehical_type.equals("Van")) {
                            wheeler_image.setBackgroundResource(R.drawable.ic_van);
                           // Log.e("4 Wheeler>>", (mVehicalDTOS.get(0).getVehicleType()));
                        } else if (vehical_type.equals("Bike")) {
                            wheeler_image.setBackgroundResource(R.drawable.ic_bike);
                          //  Log.e("2 Wheeler>>", (mVehicalDTOS.get(0).getVehicleType()));
                        } else if (vehical_type.equals("Truck")) {
                            wheeler_image.setBackgroundResource(R.drawable.ic_truck);
                           // Log.e("2 Wheeler>>", (mVehicalDTOS.get(0).getVehicleType()));
                        } else {
                            wheeler_image.setBackgroundResource(R.drawable.ic_bike);
                        }


                    } else {
                        vehical_LL.setVisibility(View.GONE);
                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<HomeDTO> call, Throwable t) {
                call.cancel();
                t.printStackTrace();

            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v == btnselect_vehicleId) {
            //   selectVehicle();
            startActivity(new Intent(getActivity(), VehicleDetailActivity.class));

        } else if (v == iv_close_vehicle) {
            selectvehicle.dismiss();
        } else if (v == lv_applyBtn) {
            selectvehicle.dismiss();
        }/* else if (v == new_order_layout) {
            startActivity(new Intent(getActivity(), AcceptOrderActivity.class));
        }*/ else if (v == add_driver_btn) {
            startActivity(new Intent(getActivity(), RegistrationActivity.class)
                    .putExtra("type", "Courier"));
        } else if (v == add_vehicle_btn) {
            startActivity(new Intent(getActivity(), AddNewVehicleActivity.class));
        }
    }

    public void selectVehicle() {
        selectvehicle = new Dialog(getContext());
        selectvehicle.setContentView(R.layout.dialog_select_vehcile);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(selectvehicle.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        selectvehicle.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        selectvehicle.getWindow().setAttributes(lp);
        selectvehicle.show();

        rv_select_vehicle = selectvehicle.findViewById(R.id.rv_select_vehicle);
        LinearLayout add_new_vehicle_ll = selectvehicle.findViewById(R.id.add_new_vehicle_ll);
        no_data_image = selectvehicle.findViewById(R.id.no_data_image);

        iv_close_vehicle = selectvehicle.findViewById(R.id.iv_close_vehicle);
        lv_applyBtn = selectvehicle.findViewById(R.id.lv_applyBtn);


        selectvehicle.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //action when dialog is dismissed goes here
                initialization(root);
            }
        });

        iv_close_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectvehicle.dismiss();
            }
        });

        add_new_vehicle_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddNewVehicleActivity.class));
            }
        });

        lv_applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectvehicle.dismiss();
            }
        });

    }

    private void getVehicalMethod() {


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", sessionManager.getRegisterUser().getId());

        Call<AllResponse> call = apiService.view_vehicle(hm);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {

                try {

                    AllResponse body = response.body();

                    if (body.getStatus() == 200) {

                        mVehicalDTOS = body.getmVehicalDTOS();
                        //  if (mVehicalDTOS == null) {
                        //      no_data_image.setVisibility(View.VISIBLE);
                        //  }

                        //   rv_select_vehicle.setLayoutManager(new LinearLayoutManager(getActivity()));
                        //   SaveVehicalAdapter saveVehicalAdapter = new SaveVehicalAdapter(getActivity(), mVehicalDTOS, selectvehicle);
                        //    rv_select_vehicle.setAdapter(saveVehicalAdapter);
                        //   saveVehicalAdapter.notifyDataSetChanged();


                        vehical_model_name_tv.setText(mVehicalDTOS.get(0).getVehicleName());
                        vehical_number_tv.setText(mVehicalDTOS.get(0).getVehicleRegistrionNumber());
                        String vehical_type = (mVehicalDTOS.get(0).getVehicleType());

                        if (vehical_type.equals("Van")) {
                            wheeler_image.setBackgroundResource(R.drawable.ic_van);
                           // Log.e("4 Wheeler>>", (mVehicalDTOS.get(0).getVehicleType()));
                        } else if (vehical_type.equals("Bike")) {
                            wheeler_image.setBackgroundResource(R.drawable.ic_bike);
                          //  Log.e("2 Wheeler>>", (mVehicalDTOS.get(0).getVehicleType()));
                        } else if (vehical_type.equals("Truck")) {
                            wheeler_image.setBackgroundResource(R.drawable.ic_truck);
                            //Log.e("2 Wheeler>>", (mVehicalDTOS.get(0).getVehicleType()));
                        } else {
                            wheeler_image.setBackgroundResource(R.drawable.ic_bike);
                           // Log.e("2 Wheeler>>", (mVehicalDTOS.get(0).getVehicleType()));
                        }


                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                call.cancel();
                t.printStackTrace();

            }
        });
    }


    private void getDriverStatus(String status) {


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", sessionManager.getRegisterUser().getId());
        hm.put("status", status);

        Call<AllResponse> call = apiService.driver_status(hm);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {

                try {

                    AllResponse body = response.body();

                    if (body.getStatus() == 200) {

                        if (status.equals("Offline")) {
                            status_tv.setText("Offline");
                            sessionManager.DRIVER_ONLINE_STATUS(false);

                            mOrderDTOArrayList.clear();
                            mHomeAdapter = new HomeAdapter(getActivity(), mOrderDTOArrayList);
                            new_order_list.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));
                            new_order_list.setAdapter(mHomeAdapter);
                            mHomeAdapter.notifyDataSetChanged();

                        } else {
                            status_tv.setText("Online");
                            sessionManager.DRIVER_ONLINE_STATUS(true);

                            if (sessionManager.is_COMPANY_DRIVER()) {
                                CompanyDriverView_Order();
                            } else {
                                getOrderMethod();

                            }
                        }

                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                call.cancel();
                t.printStackTrace();

            }
        });
    }

    private void getOrderMethod() {
        mOrderDTOArrayList.clear();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", sessionManager.getRegisterUser().getId());

        Call<HomeDTO> call = apiService.view_orders(hm);
        call.enqueue(new Callback<HomeDTO>() {
            @Override
            public void onResponse(Call<HomeDTO> call, Response<HomeDTO> response) {

                try {

                    HomeDTO body = response.body();

                    if (body.getStatus() == 200) {

                        mOrderDTOArrayList = body.getAllOrder();

                        mHomeAdapter = new HomeAdapter(getActivity(), mOrderDTOArrayList);
                        new_order_list.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));
                        new_order_list.setAdapter(mHomeAdapter);
                        mHomeAdapter.notifyDataSetChanged();

                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<HomeDTO> call, Throwable t) {
                call.cancel();
                t.printStackTrace();

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);


        getCurrentLocation();

    }


    private void getCurrentLocation() {

        location = googleApisHandle.getLastKnownLocation(getActivity());
        if (location != null) {

            setCurrentMarker(location.getLatitude(), location.getLongitude());

            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            sessionManager.current_LAT("" + currentLatitude);
            sessionManager.current_LONG("" + currentLongitude);


        } else {
            if (count < 5) {
                count++;
                getCurrentLocation();
            } else {
                ShowRefereshDialog();

            }
        }
        updatelatLong();
    }

    private void updatelatLong() {


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", sessionManager.getRegisterUser().getId());
        hm.put("driver_lat", "" + currentLatitude);
        hm.put("driver_long", "" + currentLongitude);

        Call<AllResponse> call = apiService.driver_update_lat_long(hm);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {

                try {

                    AllResponse body = response.body();

                    if (body.getStatus() == 200) {


                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                call.cancel();
                t.printStackTrace();

            }
        });
    }

    private void updateMethod() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();

        hm.put("driver_id", sessionManager.getRegisterUser().getId());
        hm.put("device_id", m_deviceId);
        hm.put("token", sessionManager.getTokenFCM());

        if (sessionManager.is_DRIVER()) {
            hm.put("type", "driver");
        } else {
            hm.put("type", "company");
        }


        Call<AllResponse> call = apiService.driver_update_token(hm);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {

                try {

                    AllResponse body = response.body();


                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                call.cancel();
                t.printStackTrace();

            }
        });
    }


    private void getCourire_ViewMethod() {
        mOrderDTOArrayList.clear();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", sessionManager.getRegisterUser().getId());

        Call<HomeDTO> call = apiService.courier_view_order(hm);
        call.enqueue(new Callback<HomeDTO>() {
            @Override
            public void onResponse(Call<HomeDTO> call, Response<HomeDTO> response) {

                try {

                    HomeDTO body = response.body();

                    if (body.getStatus() == 200) {

                        mOrderDTOArrayList = body.getAllOrder();

                        mHomeAdapter = new HomeAdapter(getActivity(), mOrderDTOArrayList);
                        new_order_list.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));
                        new_order_list.setAdapter(mHomeAdapter);
                        mHomeAdapter.notifyDataSetChanged();

                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<HomeDTO> call, Throwable t) {
                call.cancel();
                t.printStackTrace();

            }
        });
    }


    private void setCurrentMarker(double latitude, double longitude) {

        currentLatLng = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        getAddress(latitude, longitude);
    }

    private void ShowRefereshDialog() {
        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                .setMessage("please_refresh_your_location")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Utils.I(this, AmountDoneNotificationActivity.class, null);
                        dialog.dismiss();
                    }
                }).show();

    }

    public String getAddress(double lat, double lang) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getActivity());
            if (lat != 0 || lang != 0) {
                addresses = geocoder.getFromLocation(lat, lang, 1);
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getAddressLine(1);
                String country = addresses.get(0).getAddressLine(2);
                String state = addresses.get(0).getSubLocality();

                //  search_TV.setText(address);
                //  strAddress = search_TV.toString();
                return city + "," + country + "-" + state;

            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


/*
    @Override
    public boolean onBackPressed() {


        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getSupportFragmentManager().popBackStack();

        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            // drawer.close();
            Toast.makeText(getActivity(), "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            getActivity().finish();

        }
        return false;
    }
*/


}

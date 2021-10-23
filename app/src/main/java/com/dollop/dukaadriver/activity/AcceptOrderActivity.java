package com.dollop.dukaadriver.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dollop.dukaadriver.DirectionHelper.FetchURL;
import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.NetworkUtil;
import com.dollop.dukaadriver.UtilityTools.SessionManager;

import com.dollop.dukaadriver.UtilityTools.Utility;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.adapter.ProductItemListAdapter;
import com.dollop.dukaadriver.model.AccpetOrderDTO;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.DistanceDTO;
import com.dollop.dukaadriver.model.DistanceRespone;
import com.dollop.dukaadriver.model.OrderDTO;
import com.dollop.dukaadriver.model.OrderItem;
import com.dollop.dukaadriver.model.VehicalDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class AcceptOrderActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressBar timeleft_progress;
    TextView time_left, distributor_distance_tv, retailer_distance_tv;
    int pStatus;
    Button reject_order_btn;
    ImageView order_details_image, back_btn_total_earn;
    Handler handler;
    Window window;
    OrderDTO mOrderDTO;
    TextView order_id_TV, distributore_address_tv, drop_address_TV, total_item_Tv, total_amouny_TV;

    SessionManager sessionManager;
    ArrayList<VehicalDTO> mVehicalDTOS;
    public ArrayList<OrderItem> itemModels = new ArrayList<>();
    MyCountDownTimer myCountDownTimer;
    boolean isCounterRunning = false;
    DistanceRespone mDistanceRespone;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private static final long START_TIME_IN_MILLIS = 30000;
    boolean acceptOrder = false;
    LatLng currentLatLng, distributoreLng, retailerLng;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_order);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        sessionManager = new SessionManager(this);
        mOrderDTO = (OrderDTO) getIntent().getSerializableExtra("object");

        initialization();
        initCountDownTimer();
        getVehicalMethod();
        show_distributor_distance();
        show_retailer_distance();


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initialization() {


        order_id_TV = findViewById(R.id.order_id_TV);
        distributore_address_tv = findViewById(R.id.distributore_address_tv);
        drop_address_TV = findViewById(R.id.drop_address_TV);
        total_item_Tv = findViewById(R.id.total_item_Tv);
        total_amouny_TV = findViewById(R.id.total_amouny_TV);
        distributor_distance_tv = findViewById(R.id.distributor_distance_tv);
        retailer_distance_tv = findViewById(R.id.retailer_distance_tv);
        back_btn_total_earn = findViewById(R.id.back_btn_total_earn);

        order_id_TV.setText("#000" + mOrderDTO.getId());
        distributore_address_tv.setText(mOrderDTO.getDistributorAddress());
        drop_address_TV.setText(mOrderDTO.getRetailerAddress());
        total_item_Tv.setText(mOrderDTO.getItemCount() + " Items");
        total_amouny_TV.setText(getString(R.string.currency_sign) + mOrderDTO.getDeliveryCharge());

        window = AcceptOrderActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(AcceptOrderActivity.this, R.color.darkblue));

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.progress_bar);

        timeleft_progress = findViewById(R.id.timeleft_progress);
        reject_order_btn = findViewById(R.id.reject_order_btn);
        order_details_image = findViewById(R.id.order_details_image);
        time_left = (TextView) findViewById(R.id.time_left);
        time_left = findViewById(R.id.time_left);

        timeleft_progress.setIndeterminate(true);
        timeleft_progress.setProgressDrawable(drawable);

        order_details_image.setOnClickListener(this);
        reject_order_btn.setOnClickListener(this);
        back_btn_total_earn.setOnClickListener(this);

    }

    public void initCountDownTimer() {

        myCountDownTimer = new MyCountDownTimer(30000, 1000);
        myCountDownTimer.start();


    }


    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            mTimeLeftInMillis = millisUntilFinished;
            isCounterRunning = true;
            int seconds = (int) (mTimeLeftInMillis / 1000) % 120;
            timeleft_progress.setProgress(seconds);
            time_left.setText(seconds + "");

        }

        @Override
        public void onFinish() {

            if (sessionManager.is_DRIVER()) {

                if (NetworkUtil.isNetworkAvailable(AcceptOrderActivity.this)) {
                    acceptOrderMethod("Accept");
                } else {
                    Utility.netConnect(AcceptOrderActivity.this);

                }
            } else {

                if (NetworkUtil.isNetworkAvailable(AcceptOrderActivity.this)) {
                    acceptOrderCourirMethod("Accept");
                } else {
                    Utility.netConnect(AcceptOrderActivity.this);

                }

            }
            isCounterRunning = false;

        }
    }


    @Override
    public void onClick(View v) {
        if (v == order_details_image) {
            if (sessionManager.is_DRIVER()) {

                if (NetworkUtil.isNetworkAvailable(AcceptOrderActivity.this)) {
                    acceptOrderMethod("Accept");
                } else {
                    Utility.netConnect(AcceptOrderActivity.this);

                }

            } else {

                if (NetworkUtil.isNetworkAvailable(AcceptOrderActivity.this)) {
                    acceptOrderCourirMethod("Accept");
                } else {
                    Utility.netConnect(AcceptOrderActivity.this);

                }

            }

        } else if (v == reject_order_btn) {

            if (sessionManager.is_DRIVER()) {

                if (NetworkUtil.isNetworkAvailable(AcceptOrderActivity.this)) {
                    acceptOrderMethod("Reject");
                } else {
                    Utility.netConnect(AcceptOrderActivity.this);

                }

            } else {
                if (NetworkUtil.isNetworkAvailable(AcceptOrderActivity.this)) {
                    acceptOrderCourirMethod("Reject");
                } else {
                    Utility.netConnect(AcceptOrderActivity.this);

                }

            }

        } else if (v == back_btn_total_earn) {
            if (!acceptOrder) {

                if (sessionManager.is_DRIVER()) {

                    if (NetworkUtil.isNetworkAvailable(AcceptOrderActivity.this)) {
                        acceptOrderMethod("Reject");
                    } else {
                        Utility.netConnect(AcceptOrderActivity.this);

                    }

                } else {
                    if (NetworkUtil.isNetworkAvailable(AcceptOrderActivity.this)) {
                        acceptOrderCourirMethod("Reject");
                    } else {
                        Utility.netConnect(AcceptOrderActivity.this);

                    }

                }
            }
        }


    }


    private void acceptOrderMethod(String status) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", sessionManager.getRegisterUser().getId());
        hm.put("order_id", mOrderDTO.getId());
        hm.put("vehicle_id", mVehicalDTOS.get(0).getId());
        hm.put("order_status", status);


        Call<AccpetOrderDTO> call = apiService.accept_order(hm);
        call.enqueue(new Callback<AccpetOrderDTO>() {
            @Override
            public void onResponse(Call<AccpetOrderDTO> call, Response<AccpetOrderDTO> response) {

                try {

                    AccpetOrderDTO body = response.body();

                    if (body.getStatus() == 200) {
                        acceptOrder = true;

                        if (status.equals("Reject")) {

                            startActivity(new Intent(AcceptOrderActivity.this, HomeActivity.class));
                            finishAffinity();

                        } else {

                            startActivity(new Intent(AcceptOrderActivity.this, AcceptOrderDriverActivity.class)
                                    .putExtra("model", mOrderDTO).putExtra("stage", "0"));
                            finish();
                            finishAffinity();

                        }

                    } else {
                        ShowDialog(body.getMessage());
                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<AccpetOrderDTO> call, Throwable t) {
                call.cancel();
                t.printStackTrace();

            }
        });
    }


    private void acceptOrderCourirMethod(String status) {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("courier_id", sessionManager.getRegisterUser().getId());
        hm.put("order_id", mOrderDTO.getId());
        hm.put("order_status", status);

        Call<AccpetOrderDTO> call = apiService.courier_accept_order(hm);
        call.enqueue(new Callback<AccpetOrderDTO>() {
            @Override
            public void onResponse(Call<AccpetOrderDTO> call, Response<AccpetOrderDTO> response) {

                try {

                    AccpetOrderDTO body = response.body();

                    if (body.getStatus() == 200) {
                        acceptOrder = true;
                        if (status.equals("Reject")) {

                            startActivity(new Intent(AcceptOrderActivity.this, HomeActivity.class));
                            finish();
                            finishAffinity();

                        } else {
                            /*startActivity(new Intent(AcceptOrderActivity.this, AssignOrderActivity.class)
                                    .putExtra("object", mOrderDTO));*/
                           startActivity(new Intent(AcceptOrderActivity.this, AssignOrderActivity.class)
                                    .putExtra("ID", mOrderDTO.getId())
                                    .putExtra("VehicleType", mOrderDTO.getVehicle_type())
                            );
                            finish();
                            finishAffinity();
                        }


                    } else {
                        ShowDialog(body.getMessage());
                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<AccpetOrderDTO> call, Throwable t) {
                call.cancel();
                t.printStackTrace();

            }
        });
    }

    @Override
    public void onBackPressed() {

        if (!acceptOrder) {

            if (sessionManager.is_DRIVER()) {

                if (NetworkUtil.isNetworkAvailable(AcceptOrderActivity.this)) {
                    acceptOrderMethod("Reject");
                } else {
                    Utility.netConnect(AcceptOrderActivity.this);

                }

            } else {
                if (NetworkUtil.isNetworkAvailable(AcceptOrderActivity.this)) {
                    acceptOrderCourirMethod("Reject");
                } else {
                    Utility.netConnect(AcceptOrderActivity.this);

                }

            }
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();

       /* if (isCounterRunning) {
            myCountDownTimer.cancel();
        }*/

    }


    private void show_distributor_distance() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_lat", sessionManager.getCurrent_LAT());
        hm.put("driver_id", sessionManager.getRegisterUser().getId());
        hm.put("driver_long", sessionManager.getCurrent_LONG());
        hm.put("distributor_lat", mOrderDTO.getStoreLat());
        hm.put("distributor_long", mOrderDTO.getStoreLong());

        Call<DistanceDTO> call = apiService.driver_distance(hm);
        call.enqueue(new Callback<DistanceDTO>() {
            @Override
            public void onResponse(Call<DistanceDTO> call, Response<DistanceDTO> response) {

                try {

                    DistanceDTO body = response.body();

                    if (body.getStatus() == 200) {
                        mDistanceRespone = new DistanceRespone();
                        mDistanceRespone = body.getDistance();
                        distributor_distance_tv.setText("" + mDistanceRespone.getDistanceInKm() + " KM");

                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<DistanceDTO> call, Throwable t) {
                call.cancel();
                t.printStackTrace();

            }
        });
    }

    private void show_retailer_distance() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_lat", sessionManager.getCurrent_LAT());
        hm.put("driver_long", sessionManager.getCurrent_LONG());

        hm.put("retailer_lat", mOrderDTO.getRetailerLat());
        hm.put("retailer_long", mOrderDTO.getRetailerLong());
        hm.put("driver_id", sessionManager.getRegisterUser().getId());
        Call<DistanceDTO> call = apiService.driver_distance(hm);

        call.enqueue(new Callback<DistanceDTO>() {
            @Override
            public void onResponse(Call<DistanceDTO> call, Response<DistanceDTO> response) {

                try {

                    DistanceDTO body = response.body();

                    if (body.getStatus() == 200) {

                        mDistanceRespone = new DistanceRespone();
                        mDistanceRespone = body.getDistance();
                        retailer_distance_tv.setText("" + mDistanceRespone.getDistanceInKm() + " KM");

                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<DistanceDTO> call, Throwable t) {
                call.cancel();
                t.printStackTrace();

            }
        });
    }

    private void ShowDialog(String sms) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(sms)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Utils.I(this, AmountDoneNotificationActivity.class, null);
                        startActivity(new Intent(AcceptOrderActivity.this, HomeActivity.class));
                        finish();
                        dialog.dismiss();
                    }
                }).show();

    }



 /*   private void moveCameraToWantedArea() {
        double currentLat = Double.parseDouble(sessionManager.getCurrent_LAT());
        double currentLong = Double.parseDouble(sessionManager.getCurrent_LONG());

        double distributorLat = Double.parseDouble(mOrderDTO.getStoreLat());
        double distributorLong = Double.parseDouble(mOrderDTO.getStoreLat());

        double retailerLat = Double.parseDouble(mOrderDTO.getRetailerLat());
        double retailerLong = Double.parseDouble(mOrderDTO.getRetailerLong());

        currentLatLng = new LatLng(currentLat, currentLong);
        distributoreLating = new LatLng(distributorLat, distributorLong);
        retailerLating = new LatLng(retailerLat, retailerLong);

    }
*/

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

             /*           VehicalDTO mVehicalDTO = new VehicalDTO();

                        mVehicalDTO.setId(mVehicalDTOS.get(0).getId());
                        mVehicalDTO.setVehicleType(mVehicalDTOS.get(0).getVehicleType());
                        mVehicalDTO.setVehicleNum(mVehicalDTOS.get(0).getVehicleNum());
                        mVehicalDTO.setVehicleName(mVehicalDTOS.get(0).getVehicleName());
                        mVehicalDTO.setVehicleRegistrionNumber(mVehicalDTOS.get(0).getVehicleRegistrionNumber());
                        mVehicalDTO.setModelName(mVehicalDTOS.get(0).getVehicleName());


                       // mOrderDTO.getVehicle().add(mVehicalDTO);

                        mOrderDTO.getVehicle().add(mVehicalDTO);*/


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


}
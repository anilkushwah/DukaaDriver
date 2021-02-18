package com.dollop.dukaadriver.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.VehicalDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleDetailActivity extends AppCompatActivity {

    ImageView back_press_img;
    TextView model_name_tv, vehicle_type_tv, vehicle_registration_number_tv;
    ImageView licence_image, insurance_image;
    ArrayList<VehicalDTO> mVehicalDTOS;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_detail);

        sessionManager = new SessionManager(this);

        back_press_img = findViewById(R.id.back_press_img);
        model_name_tv = findViewById(R.id.model_name_tv);
        licence_image = findViewById(R.id.licence_image);
        insurance_image = findViewById(R.id.insurance_image);
        vehicle_type_tv = findViewById(R.id.vehicle_type_tv);
      //  vehicle_number_tv = findViewById(R.id.vehicle_number_tv);
        vehicle_registration_number_tv = findViewById(R.id.vehicle_registration_number_tv);

        back_press_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getVehicalMethod();

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

                        model_name_tv.setText(mVehicalDTOS.get(0).getVehicleName());
                        vehicle_type_tv.setText(mVehicalDTOS.get(0).getVehicleType());
                      //  vehicle_number_tv.setText(mVehicalDTOS.get(0).getVehicleNum());
                        vehicle_registration_number_tv.setText(mVehicalDTOS.get(0).getVehicleRegistrionNumber());

                        Glide.with(getApplicationContext())
                                .load(ApiClient.BASE_URL  + mVehicalDTOS.get(0).getVehicleInsurance())
                                .into(insurance_image);
                        Log.e("path>>", ApiClient.BASE_URL + mVehicalDTOS.get(0).getVehicleInsurance());
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
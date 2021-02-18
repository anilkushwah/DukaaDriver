package com.dollop.dukaadriver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.adapter.SaveVehicalAdapter;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.VehicalDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageVehicleActivity extends AppCompatActivity {
    Button add_driver_btn;
    ImageView no_data_image, back_home_img;
    ArrayList<VehicalDTO> mVehicalDTOS;
    SessionManager sessionManager;
    RecyclerView all_vehicle_RL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_vehicle);

        sessionManager = new SessionManager(this);

        initialization();
        getVehicalMethod();


    }

    private void initialization() {
        add_driver_btn = findViewById(R.id.add_driver_btn);
        no_data_image = findViewById(R.id.no_data_image);
        all_vehicle_RL = findViewById(R.id.all_vehicle_RL);
        back_home_img = findViewById(R.id.back_home_img);

        add_driver_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageVehicleActivity.this, AddNewVehicleActivity.class));
            }
        });
        back_home_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageVehicleActivity.this, HomeActivity.class));
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

                        if (mVehicalDTOS == null) {
                            no_data_image.setVisibility(View.VISIBLE);
                        }

                        all_vehicle_RL.setLayoutManager(new LinearLayoutManager(ManageVehicleActivity.this));
                        SaveVehicalAdapter saveVehicalAdapter = new SaveVehicalAdapter(ManageVehicleActivity.this, mVehicalDTOS);
                        all_vehicle_RL.setAdapter(saveVehicalAdapter);
                        saveVehicalAdapter.notifyDataSetChanged();


                    } else {
                        no_data_image.setVisibility(View.VISIBLE);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ManageVehicleActivity.this, HomeActivity.class));
    }
}
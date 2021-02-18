package com.dollop.dukaadriver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.adapter.DriverAssignAdapter;
import com.dollop.dukaadriver.adapter.SaveVehicalAdapter;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.OrderDTO;
import com.dollop.dukaadriver.model.VehicalDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleAssigneActivity extends AppCompatActivity {


    ImageView no_data_image;
    ArrayList<VehicalDTO> mVehicalDTOS;
    SessionManager sessionManager;
    RecyclerView assign_RL;
    String vehicle_type = "", order_id = "", driver_id="";
    ImageView back_home_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_assigne);

        sessionManager = new SessionManager(this);


        // mOrderDTO = (OrderDTO) getIntent().getSerializableExtra("object");

        vehicle_type = getIntent().getStringExtra("vehicle_type");
        order_id = getIntent().getStringExtra("order_id");
        driver_id = getIntent().getStringExtra("driver_id");

        no_data_image = findViewById(R.id.no_data_image);
        assign_RL = findViewById(R.id.assign_RL);
        back_home_img = findViewById(R.id.back_home_img);
        back_home_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getVehicalMethod();

    }


    private void getVehicalMethod() {


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", sessionManager.getRegisterUser().getId());
        hm.put("vehicle_type", vehicle_type);

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

                        assign_RL.setLayoutManager(new LinearLayoutManager(VehicleAssigneActivity.this));
                        DriverAssignAdapter mDriverAssignAdapter = new DriverAssignAdapter(VehicleAssigneActivity.this,
                                mVehicalDTOS, order_id, driver_id);
                        assign_RL.setAdapter(mDriverAssignAdapter);
                        mDriverAssignAdapter.notifyDataSetChanged();


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


}
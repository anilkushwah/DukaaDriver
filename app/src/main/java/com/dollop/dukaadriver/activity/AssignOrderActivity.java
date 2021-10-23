package com.dollop.dukaadriver.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.NetworkUtil;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.Utility;
import com.dollop.dukaadriver.adapter.AllDriversAdapter;
import com.dollop.dukaadriver.adapter.AssignedJobsAdapter;
import com.dollop.dukaadriver.adapter.ManageDriverAdapter;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.DriverDTO;
import com.dollop.dukaadriver.model.OrderDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AssignOrderActivity extends AppCompatActivity {

    RecyclerView all_drivers_RL;
    AllDriversAdapter mAllDriversAdapter;
    SessionManager sessionManager;
    ArrayList<DriverDTO> mDriverDTOArrayList;
    String order_id = "";
    String VehicleType = "";
    //OrderDTO mOrderDTO;
    ImageView back_home_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.fragment_assign_order);

        order_id=getIntent().getStringExtra("ID");
        VehicleType=getIntent().getStringExtra("VehicleType");


        all_drivers_RL = findViewById(R.id.all_drivers_RL);
        back_home_img = findViewById(R.id.back_home_img);
        back_home_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sessionManager = new SessionManager(this);
        mDriverDTOArrayList = new ArrayList<>();

        if (NetworkUtil.isNetworkAvailable(AssignOrderActivity.this)) {
            getDriverListMethod();
        } else {
            Utility.netConnect(AssignOrderActivity.this);

        }


    }

    private void getDriverListMethod() {


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", sessionManager.getRegisterUser().getId());
        hm.put("vehicle_type", VehicleType);

        Call<AllResponse> call = apiService.get_driver_status(hm);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {

                try {

                    AllResponse body = response.body();

                    if (body.getStatus() == 200) {
                        mDriverDTOArrayList = body.getDriverList();

                        mAllDriversAdapter = new AllDriversAdapter(AssignOrderActivity.this,
                                mDriverDTOArrayList,  VehicleType,
                                order_id);
                        all_drivers_RL.setLayoutManager(new LinearLayoutManager(AssignOrderActivity.this, RecyclerView.VERTICAL, false));
                        all_drivers_RL.setAdapter(mAllDriversAdapter);
                        mAllDriversAdapter.notifyDataSetChanged();

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

        startActivity(new Intent(AssignOrderActivity.this, HomeActivity.class));
        finish();

    }
}
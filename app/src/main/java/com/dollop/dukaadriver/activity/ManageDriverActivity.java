package com.dollop.dukaadriver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.NetworkUtil;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.Utility;
import com.dollop.dukaadriver.adapter.ManageDriverAdapter;
import com.dollop.dukaadriver.adapter.NewOrderAdapter;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.DriverDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageDriverActivity extends AppCompatActivity implements View.OnClickListener {

    Button add_driver_btn;
    ManageDriverAdapter manageDriverAdapter;
    RecyclerView all_drivers_RL;
    SessionManager sessionManager;
    ArrayList<DriverDTO> mDriverDTOArrayList;
    ImageView back_home_img, no_data_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_manage_driver);

        initialization();



    }

    private void initialization() {
        sessionManager = new SessionManager(this);
        add_driver_btn = findViewById(R.id.add_driver_btn);
        all_drivers_RL = findViewById(R.id.all_drivers_RL);
        back_home_img = findViewById(R.id.back_home_img);
        no_data_image = findViewById(R.id.no_data_image);

        add_driver_btn.setOnClickListener(this);
        back_home_img.setOnClickListener(this);
        no_data_image.setOnClickListener(this);
        mDriverDTOArrayList = new ArrayList<>();

        if (NetworkUtil.isNetworkAvailable(ManageDriverActivity.this)) {
            getDriverListMethod();
        } else {
            Utility.netConnect(ManageDriverActivity.this);

        }
    }

    @Override
    public void onClick(View v) {
        if (v == add_driver_btn) {
            startActivity(new Intent(ManageDriverActivity.this, RegistrationActivity.class)
                    .putExtra("type", "Courier"));
        } else if (v == back_home_img) {
            startActivity(new Intent(ManageDriverActivity.this, HomeActivity.class));
        } else if (v == no_data_image) {
            startActivity(new Intent(ManageDriverActivity.this, RegistrationActivity.class)
                    .putExtra("type", "Courier"));
        }
    }


    private void getDriverListMethod() {


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("delivery_partner_id", sessionManager.getRegisterUser().getId());

        Call<AllResponse> call = apiService.get_driver_by_companyId(hm);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {

                try {

                    AllResponse body = response.body();

                    if (body.getStatus() == 200) {

                        mDriverDTOArrayList = body.getDriverList();
                        if (mDriverDTOArrayList == null) {
                            no_data_image.setVisibility(View.VISIBLE);
                        }

                        manageDriverAdapter = new ManageDriverAdapter(ManageDriverActivity.this, mDriverDTOArrayList);
                        all_drivers_RL.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                        all_drivers_RL.setAdapter(manageDriverAdapter);
                        manageDriverAdapter.notifyDataSetChanged();

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
    protected void onResume() {
        super.onResume();
        initialization();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ManageDriverActivity.this, HomeActivity.class));

    }
}
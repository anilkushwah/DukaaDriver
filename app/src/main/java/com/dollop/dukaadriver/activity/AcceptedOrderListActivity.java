package com.dollop.dukaadriver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.adapter.AcceptedOrderAdapter;
import com.dollop.dukaadriver.adapter.AllDriversAdapter;
import com.dollop.dukaadriver.adapter.TotalEarnAdapeter;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.OrderDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptedOrderListActivity extends AppCompatActivity {
    SessionManager sessionManager;
    AcceptedOrderAdapter mAcceptedOrderAdapter;
    RecyclerView all_drivers_RL;
    ArrayList<OrderDTO> mOrderDTOArrayList;
    ImageView back_home_img, no_data_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_order_list);

        initialization();

    }
    private void acceptedOrder() {


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", sessionManager.getRegisterUser().getId());

        Call<AllResponse> call = apiService.get_driver_accept_order(hm);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {

                try {

                    AllResponse body = response.body();

                    if (body.getStatus() == 200) {

                        mOrderDTOArrayList = body.getAcceptedOrderList();

                        mAcceptedOrderAdapter = new AcceptedOrderAdapter(AcceptedOrderListActivity.this, mOrderDTOArrayList);
                        all_drivers_RL.setLayoutManager(new LinearLayoutManager(AcceptedOrderListActivity.this, RecyclerView.VERTICAL, false));
                        all_drivers_RL.setAdapter(mAcceptedOrderAdapter);
                        mAcceptedOrderAdapter.notifyDataSetChanged();


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
    private void initialization() {
        all_drivers_RL = findViewById(R.id.all_drivers_RL);
        back_home_img = findViewById(R.id.back_home_img);
        no_data_image = findViewById(R.id.no_data_image);

        mOrderDTOArrayList = new ArrayList<>();
        sessionManager = new SessionManager(this);

        acceptedOrder();

        back_home_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}
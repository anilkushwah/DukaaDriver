package com.dollop.dukaadriver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.adapter.OrderItemsDetailsAdapter;
import com.dollop.dukaadriver.model.AccpetOrderDTO;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.OrderDetailsModelList;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rv_items_orders;
    List<OrderDetailsModelList> orderDetailsModelLists = new ArrayList<>();

    Button proceed_btn;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_order_details);

        sessionManager = new SessionManager(this);

        initialization();

    }

    private void initialization() {

        rv_items_orders = findViewById(R.id.rv_items_orders);
        proceed_btn = findViewById(R.id.proceed_btn);



        rv_items_orders.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_items_orders.setLayoutManager(layoutManager);

        OrderItemsDetailsAdapter orderItemsDetailsAdapter = new OrderItemsDetailsAdapter(this, orderDetailsModelLists);
        rv_items_orders.setAdapter(orderItemsDetailsAdapter);



    }

    @Override
    public void onClick(View v) {
        if (v == proceed_btn) {
            acceptOrderMethod();
        }
    }

    private void acceptOrderMethod() {


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id","");
        hm.put("order_id","");


        Call<AccpetOrderDTO> call = apiService.accept_order(hm);
        call.enqueue(new Callback<AccpetOrderDTO>() {
            @Override
            public void onResponse(Call<AccpetOrderDTO> call, Response<AccpetOrderDTO> response) {

                try {

                    AccpetOrderDTO body = response.body();

                    if (body.getStatus() == 200) {

                        startActivity(new Intent(OrderDetailsActivity.this, AcceptOrderDriverActivity.class));
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


}

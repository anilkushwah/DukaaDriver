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
import com.dollop.dukaadriver.adapter.DeliveryItemAdapter;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.DeliveryItemModel;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rv_delivery_items;
    ArrayList<DeliveryItemModel> deliveryItemModelsList;
    DeliveryItemAdapter deliveryItemAdapter;
    Button next_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_delivery);

        rv_delivery_items = findViewById(R.id.rv_delivery_items);
        next_btn = findViewById(R.id.next_btn);
        next_btn.setOnClickListener(this);

        deliveryItemModelsList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            DeliveryItemModel deliveryItemModel = new DeliveryItemModel();
            deliveryItemModel.item_Image = R.drawable.individual_driver;
            deliveryItemModel.item_Price = "50.00";
            deliveryItemModel.item_Quantity = "5";
            deliveryItemModel.item_Name = "Fresh Onions";
            deliveryItemModelsList.add(deliveryItemModel);
        }

        deliveryItemAdapter = new DeliveryItemAdapter(DeliveryActivity.this, deliveryItemModelsList);
        rv_delivery_items.setLayoutManager(new LinearLayoutManager(DeliveryActivity.this, RecyclerView.VERTICAL, false));
        rv_delivery_items.setAdapter(deliveryItemAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == next_btn) {
            startActivity(new Intent(DeliveryActivity.this, DeliveryPrefrences.class));
        }
    }


}
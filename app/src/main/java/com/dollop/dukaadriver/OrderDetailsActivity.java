package com.dollop.dukaadriver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;

import com.dollop.dukaadriver.adapter.OrderItemsDetailsAdapter;
import com.dollop.dukaadriver.model.OrderDetailsModelList;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {
    RecyclerView rv_items_orders;
    List<OrderDetailsModelList>orderDetailsModelLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_order_details);
        rv_items_orders = findViewById(R.id.rv_items_orders);


        rv_items_orders.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_items_orders.setLayoutManager(layoutManager);

        OrderItemsDetailsAdapter orderItemsDetailsAdapter = new OrderItemsDetailsAdapter(this,orderDetailsModelLists);
        rv_items_orders.setAdapter(orderItemsDetailsAdapter);

    }
}

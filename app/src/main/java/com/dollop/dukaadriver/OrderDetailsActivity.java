package com.dollop.dukaadriver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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
        setContentView(R.layout.activity_order_details);
        rv_items_orders = findViewById(R.id.rv_items_orders);

        OrderDetailsModelList orderDetailsModelList = new OrderDetailsModelList();
        orderDetailsModelList.setItemImage(R.drawable.backeryy);
        orderDetailsModelList.setPerQuantityPrice("50.00");
        orderDetailsModelList.setItemQuantity("2 X 50");
        orderDetailsModelList.setItemsName("Fresh Onions");
        orderDetailsModelLists.add(orderDetailsModelList);

        OrderDetailsModelList orderDetailsModelList1 = new OrderDetailsModelList();
        orderDetailsModelList1.setItemImage(R.drawable.backeryy);
        orderDetailsModelList1.setPerQuantityPrice("50.00");
        orderDetailsModelList1.setItemQuantity("2 X 50");
        orderDetailsModelList1.setItemsName("Fresh Onions");
        orderDetailsModelLists.add(orderDetailsModelList1);

        OrderDetailsModelList orderDetailsModelList2 = new OrderDetailsModelList();
        orderDetailsModelList2.setItemImage(R.drawable.backeryy);
        orderDetailsModelList2.setPerQuantityPrice("50.00");
        orderDetailsModelList2.setItemQuantity("2 X 50");
        orderDetailsModelList2.setItemsName("Fresh Onions");
        orderDetailsModelLists.add(orderDetailsModelList2);

        OrderDetailsModelList orderDetailsModelList3 = new OrderDetailsModelList();
        orderDetailsModelList3.setItemImage(R.drawable.backeryy);
        orderDetailsModelList3.setPerQuantityPrice("50.00");
        orderDetailsModelList3.setItemQuantity("2 X 50");
        orderDetailsModelList3.setItemsName("Fresh Onions");
        orderDetailsModelLists.add(orderDetailsModelList3);

        rv_items_orders.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_items_orders.setLayoutManager(layoutManager);

        OrderItemsDetailsAdapter orderItemsDetailsAdapter = new OrderItemsDetailsAdapter(this,orderDetailsModelLists);
        rv_items_orders.setAdapter(orderItemsDetailsAdapter);

    }
}

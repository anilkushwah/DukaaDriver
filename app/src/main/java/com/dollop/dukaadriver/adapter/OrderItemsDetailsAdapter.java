package com.dollop.dukaadriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.model.OrderDetailsModelList;
import com.dollop.dukaadriver.model.savedaddress_model;

import java.util.ArrayList;
import java.util.List;

public class OrderItemsDetailsAdapter extends RecyclerView.Adapter<OrderItemsDetailsAdapter.MyViewHolder> {
    Context context;
    List<OrderDetailsModelList>  orderDetailsModelLists = new ArrayList<>();

    public OrderItemsDetailsAdapter(Context context, List<OrderDetailsModelList> savedaddress_models) {
        this.context = context;
        this.orderDetailsModelLists = savedaddress_models;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_order_details,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderDetailsModelList current = orderDetailsModelLists.get(position);

        holder.tv_itemsName.setText(current.getItemsName());
        holder.tv_itemQuantity.setText(current.getItemQuantity());
        holder.tv_price_per_item.setText(current.getPerQuantityPrice());

    }

    @Override
    public int getItemCount() {
        return orderDetailsModelLists.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_itemsName,tv_itemQuantity,tv_price_per_item;
        RadioButton saved_radio;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_itemsName = itemView.findViewById(R.id.tv_itemsName);
            tv_itemQuantity = itemView.findViewById(R.id.tv_itemQuantity);
            tv_price_per_item = itemView.findViewById(R.id.tv_price_per_item);

        }
    }
}

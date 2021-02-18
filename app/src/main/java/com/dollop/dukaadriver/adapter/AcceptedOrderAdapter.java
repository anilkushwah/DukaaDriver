package com.dollop.dukaadriver.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.activity.AcceptOrderActivity;
import com.dollop.dukaadriver.activity.AssignOrderActivity;
import com.dollop.dukaadriver.model.DriverDTO;
import com.dollop.dukaadriver.model.OrderDTO;

import java.util.ArrayList;


public class AcceptedOrderAdapter extends RecyclerView.Adapter<AcceptedOrderAdapter.MyViewHolder> {

    Context context;
    ArrayList<OrderDTO> orderDTOS;

    public AcceptedOrderAdapter(Context context, ArrayList<OrderDTO> mList) {

        this.context = context;
        this.orderDTOS = mList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.accepted_order_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.order_id_TV.setText("#000" + orderDTOS.get(position).getId());
        holder.order_date_tv.setText(orderDTOS.get(position).getCreateDate());
        holder.pickup_address_Tv.setText(orderDTOS.get(position).getDistributorAddress());
        holder.drop_address.setText(orderDTOS.get(position).getRetailerAddress());
        holder.total_amouny.setText("Delivery charges-" + orderDTOS.get(position).getDeliveryCharge());
        holder.total_vat_TV.setText("Weight-");

        holder.btn_assigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(context, AssignOrderActivity.class)
                        .putExtra("object", orderDTOS.get(position)));

            }
        });

    }

    @Override
    public int getItemCount() {
        return orderDTOS.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView order_id_TV, order_date_tv, pickup_address_Tv, drop_address, total_amouny;
        TextView total_vat_TV;
        Button btn_assigned;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            order_id_TV = itemView.findViewById(R.id.order_id_TV);
            order_date_tv = itemView.findViewById(R.id.order_date_tv);
            pickup_address_Tv = itemView.findViewById(R.id.pickup_address_Tv);
            drop_address = itemView.findViewById(R.id.drop_address);
            total_amouny = itemView.findViewById(R.id.total_amouny);
            total_vat_TV = itemView.findViewById(R.id.total_vat_TV);
            btn_assigned = itemView.findViewById(R.id.btn_assigned);

        }
    }


}




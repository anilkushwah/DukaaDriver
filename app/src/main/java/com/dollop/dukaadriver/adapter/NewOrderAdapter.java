package com.dollop.dukaadriver.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.activity.HomeActivity;
import com.dollop.dukaadriver.activity.OrderDetailsActivity;
import com.dollop.dukaadriver.activity.AssignOrderActivity;
import com.dollop.dukaadriver.model.OrderDTO;

import java.util.ArrayList;

public class NewOrderAdapter extends RecyclerView.Adapter<NewOrderAdapter.MyViewHolder> {
    Context context;
    ArrayList<OrderDTO> mOrderDTOArrayList;
    SessionManager sessionManager;

    public NewOrderAdapter(Context context, ArrayList<OrderDTO> mList) {
        this.context = context;
        this.mOrderDTOArrayList = mList;
    }

    @NonNull
    @Override
    public NewOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.neworder_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewOrderAdapter.MyViewHolder holder, int position) {

        OrderDTO mOrderDTO = mOrderDTOArrayList.get(position);
        holder.Pickup.setText(mOrderDTO.getDistributorAddress());
        holder.Destination.setText(mOrderDTO.getRetailerAddress());
        holder.Total_Destination.setText("10KM");

        if (sessionManager.is_DRIVER()) {
            holder.assign_tv.setVisibility(View.GONE);
        }

        holder.view_detils_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, OrderDetailsActivity.class));
            }
        });

        holder.assign_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               // ((HomeActivity) context).replaceFragmentWithoutBack(new AssignOrderActivity(), "");

            }
        });

    }

    @Override
    public int getItemCount() {
        return mOrderDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Pickup, Destination, Total_Destination, view_detils_tv, assign_tv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            view_detils_tv = itemView.findViewById(R.id.view_detils_tv);
            Pickup = itemView.findViewById(R.id.pick_location_tv);
            Destination = itemView.findViewById(R.id.destination_location_tv);
            Total_Destination = itemView.findViewById(R.id.total_distance);
            assign_tv = itemView.findViewById(R.id.assign_tv);


            sessionManager = new SessionManager(context);
        }
    }
}

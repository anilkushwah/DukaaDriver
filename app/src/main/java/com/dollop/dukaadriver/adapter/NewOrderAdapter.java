package com.dollop.dukaadriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.model.NewOrderModel;

import java.util.ArrayList;

public class NewOrderAdapter extends RecyclerView.Adapter<NewOrderAdapter.MyViewHolder> {
    Context context;
    ArrayList<NewOrderModel> newOrderModels;

    public NewOrderAdapter(Context context, ArrayList<NewOrderModel> newOrderModels) {
        this.context = context;
        this.newOrderModels = newOrderModels;
    }

    @NonNull
    @Override
    public NewOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.neworder_list,parent,false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewOrderAdapter.MyViewHolder holder, int position) {

        NewOrderModel newOrderModel = newOrderModels.get(position);
        holder.Pickup.setText(newOrderModel.PickupLocation);
        holder.Destination.setText(newOrderModel.DropLocation);
        holder.Total_Destination.setText(newOrderModel.Distance);


    }

    @Override
    public int getItemCount() {
        return newOrderModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Pickup,Destination,Total_Destination;
        RelativeLayout  viewdetails_recycle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            viewdetails_recycle = itemView.findViewById(R.id.viewdetails_recycle);
            Pickup = itemView.findViewById(R.id.pick_location_tv);
            Destination = itemView.findViewById(R.id.destination_location_tv);
            Total_Destination = itemView.findViewById(R.id.total_distance);



        }
    }
}

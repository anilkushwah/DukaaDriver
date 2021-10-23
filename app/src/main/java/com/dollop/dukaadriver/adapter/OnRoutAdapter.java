package com.dollop.dukaadriver.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.activity.AcceptOrderDriverActivity;
import com.dollop.dukaadriver.activity.DeliveryPrefrences;
import com.dollop.dukaadriver.model.OrderDTO;
import com.transferwise.sequencelayout.SequenceStep;

import java.util.ArrayList;

public class OnRoutAdapter extends RecyclerView.Adapter<OnRoutAdapter.MyViewHolder> {

    Context context;
    ArrayList<OrderDTO> mOrderDTOArrayList;
    SessionManager sessionManager;

    public  OnRoutAdapter(Context context, ArrayList<OrderDTO> mList) {
        this.context = context;
        this.mOrderDTOArrayList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.on_rout_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.customer_name.setText(mOrderDTOArrayList.get(position).getRetailerName());
        holder.customer_address.setText(mOrderDTOArrayList.get(position).getRetailer_landmark() + "," + mOrderDTOArrayList.get(position).getRetailerAddress());
        holder.order_id.setText("#000" + mOrderDTOArrayList.get(position).getId());

        holder.accepet_order.setEnabled(false);
        holder.placed_complete.setEnabled(false);
        holder.delivered_complete.setEnabled(false);
        holder.arravi_stage.setEnabled(false);
        if (sessionManager.is_DRIVER()) {
            if (mOrderDTOArrayList.get(position).getDelivered().equals("0")) {
                holder.accepet_order.setActive(true);
                holder.accepet_order.setEnabled(true);
            } else if (mOrderDTOArrayList.get(position).getDelivered().equals("Start_Job")) {
                holder.placed_complete.setActive(true);
                holder.placed_complete.setEnabled(true);
            } else if (mOrderDTOArrayList.get(position).getDelivered().equals("Pickup")) {
                holder.delivered_complete.setActive(true);
                holder.delivered_complete.setEnabled(true);
            } else if (mOrderDTOArrayList.get(position).getDelivered().equals("On_the_way")) {
                holder.arravi_stage.setActive(true);
                holder.arravi_stage.setEnabled(true);
            }

        }else {
            holder.accepet_order.setEnabled(false);
            holder.placed_complete.setEnabled(false);
            holder.delivered_complete.setEnabled(false);
            holder.arravi_stage.setEnabled(false);
        }





        holder.accepet_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(context, AcceptOrderDriverActivity.class)
                        .putExtra("model", mOrderDTOArrayList.get(position)).putExtra("stage", "0"));

            }
        });

        holder.placed_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(context, AcceptOrderDriverActivity.class)
                        .putExtra("model", mOrderDTOArrayList.get(position)).putExtra("stage", "Start_Job"));

            }
        });

        holder.arravi_stage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.E("driver_arrived:::"+mOrderDTOArrayList.get(position).getOrderStatus());
                if (mOrderDTOArrayList.get(position).getOrderStatus().equals("8")) {
                    context.startActivity(new Intent(context, DeliveryPrefrences.class)
                            .putExtra("model", mOrderDTOArrayList.get(position)));
                } else {
                    context.startActivity(new Intent(context, AcceptOrderDriverActivity.class)
                            .putExtra("model", mOrderDTOArrayList.get(position)).putExtra("stage", "On_the_way"));
                }

            }
        });

        holder.delivered_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(context, AcceptOrderDriverActivity.class)
                        .putExtra("model", mOrderDTOArrayList.get(position)).putExtra("stage", "Pickup"));

            }
        });

    }

    @Override
    public int getItemCount() {
        return mOrderDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView customer_name, customer_address, order_id;
        ImageView customer_img;
        SequenceStep accepet_order, placed_complete, delivered_complete, arravi_stage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            customer_name = itemView.findViewById(R.id.customer_name);
            customer_address = itemView.findViewById(R.id.customer_address);
            order_id = itemView.findViewById(R.id.order_id);
            customer_img = itemView.findViewById(R.id.customer_img);

            accepet_order = itemView.findViewById(R.id.accepet_order);
            placed_complete = itemView.findViewById(R.id.placed_complete);
            arravi_stage = itemView.findViewById(R.id.arravi_stage);

            delivered_complete = itemView.findViewById(R.id.delivered_complete);
            sessionManager = new SessionManager(context);

        }
    }
}




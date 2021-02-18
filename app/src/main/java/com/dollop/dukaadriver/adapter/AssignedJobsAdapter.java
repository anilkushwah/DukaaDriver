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

import com.bumptech.glide.Glide;
import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.activity.OrderDetailsActivity;
import com.dollop.dukaadriver.model.DriverDTO;
import com.dollop.dukaadriver.model.NewOrderModel;
import com.dollop.dukaadriver.retrofit.ApiClient;

import java.util.ArrayList;

public class AssignedJobsAdapter extends RecyclerView.Adapter<AssignedJobsAdapter.MyViewHolder> {

    Context context;

    ArrayList<DriverDTO> mAssignOrdeList;

    public AssignedJobsAdapter(Context context, ArrayList<DriverDTO> mList) {
        this.context = context;
        this.mAssignOrdeList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.assign_jobs_layout, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.driver_name_tv.setText(mAssignOrdeList.get(position).getFullName());
        holder.driver_number_tv.setText(mAssignOrdeList.get(position).getMobile());
        holder.order_id_TV.setText("Order ID- #000"+mAssignOrdeList.get(position).getId());

        if (mAssignOrdeList.get(position).getProfileImg() != null) {
            Glide.with(context)
                    .load(ApiClient.BASE_URL + mAssignOrdeList.get(position).getProfileImg())
                    .into(holder.user_image);
        }
    }

    @Override
    public int getItemCount() {
        return mAssignOrdeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView driver_name_tv, order_status_tv, order_id_TV, driver_number_tv;
        ImageView user_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            user_image = itemView.findViewById(R.id.user_image);
            driver_name_tv = itemView.findViewById(R.id.driver_name_tv);
            order_status_tv = itemView.findViewById(R.id.order_status_tv);
            order_id_TV = itemView.findViewById(R.id.order_id_TV);
            driver_number_tv = itemView.findViewById(R.id.driver_number_tv);

        }
    }
}



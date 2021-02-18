package com.dollop.dukaadriver.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.activity.AcceptOrderActivity;
import com.dollop.dukaadriver.activity.AssignOrderActivity;
import com.dollop.dukaadriver.activity.HomeActivity;
import com.dollop.dukaadriver.activity.VehicleAssigneActivity;
import com.dollop.dukaadriver.model.AccpetOrderDTO;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.DriverDTO;
import com.dollop.dukaadriver.model.VehicalDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;
import com.dollop.dukaadriver.ui.jobs.CourierJobFragment;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllDriversAdapter extends RecyclerView.Adapter<AllDriversAdapter.MyViewHolder> {

    Context context;
    ArrayList<DriverDTO> mDriverDTOArrayList;
    String vehicle_type, order_id;
    SessionManager sessionManager;

    public AllDriversAdapter(Context context, ArrayList<DriverDTO> mList, String vehicleType, String orderid) {
        this.context = context;
        this.mDriverDTOArrayList = mList;
        this.vehicle_type = vehicleType;
        this.order_id = orderid;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.all_driver_layout, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.assign_task_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  context.startActivity(new Intent(context, HomeActivity.class));
                // ((Activity) context).finish();

              //  assignOrderMethod(order_id, mDriverDTOArrayList.get(position).getId());


                context.startActivity(new Intent(context, VehicleAssigneActivity.class)
                        .putExtra("vehicle_type", vehicle_type)
                        .putExtra("order_id", order_id)
                        .putExtra("driver_id", mDriverDTOArrayList.get(position).getId()));
                ((Activity) context).finish();


            }
        });

        holder.driver_status_tv.setText(mDriverDTOArrayList.get(position).getStatus());
        if (mDriverDTOArrayList.get(position).getStatus().equals("Offline")) {
            holder.driver_status_tv.setTextColor(context.getResources().getColor(R.color.orange));
        } else {
            holder.driver_status_tv.setTextColor(context.getResources().getColor(R.color.colorGreen));
        }
        if (mDriverDTOArrayList.get(position).getProfileImg() != null) {
            Glide.with(context)
                    .load(ApiClient.BASE_URL + mDriverDTOArrayList.get(position).getProfileImg())
                    .into(holder.user_image);
        }
        holder.driver_name_tv.setText(mDriverDTOArrayList.get(position).getFullName());
        holder.driver_number_tv.setText(mDriverDTOArrayList.get(position).getMobile());
    }


    @Override
    public int getItemCount() {
        return mDriverDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView assign_task_tv, driver_status_tv, driver_name_tv, driver_number_tv;
        ImageView user_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            assign_task_tv = itemView.findViewById(R.id.assign_task_tv);
            driver_status_tv = itemView.findViewById(R.id.driver_status_tv);
            user_image = itemView.findViewById(R.id.user_image);
            driver_name_tv = itemView.findViewById(R.id.driver_name_tv);
            driver_number_tv = itemView.findViewById(R.id.driver_number_tv);

            sessionManager = new SessionManager(context);

        }
    }



    private void ShowDialog(String sms) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(sms)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Utils.I(this, AmountDoneNotificationActivity.class, null);
                        dialog.dismiss();
                    }
                }).show();

    }


}



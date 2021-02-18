package com.dollop.dukaadriver.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.Utility;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.activity.HomeActivity;
import com.dollop.dukaadriver.activity.LoginActivity;
import com.dollop.dukaadriver.model.AccpetOrderDTO;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.VehicalDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverAssignAdapter extends RecyclerView.Adapter<DriverAssignAdapter.MyViewHolder> {
    Context context;
    ArrayList<VehicalDTO> mVehicalDTOS;

    private int selectedPosition = -1;
    SessionManager sessionManager;

    //  Dialog dialog;
    String order_id, driver_id;

    public DriverAssignAdapter(Context context, ArrayList<VehicalDTO> mList, String id, String driverID) {
        this.context = context;
        this.mVehicalDTOS = mList;
        this.order_id = id;
        this.driver_id = driverID;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.saved_adddress_item, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        VehicalDTO current = mVehicalDTOS.get(position);


        holder.vehicle_name_tv.setText("Vehicle Name : " + current.getVehicleName());
        holder.vehicle_type_tv.setText("Vehicle Type : " + current.getVehicleType());
        holder.vehicle_number.setText("Vehicle Number : " + current.getVehicleRegistrionNumber());


        holder.saved_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition >= 0)
                    notifyItemChanged(selectedPosition);
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(selectedPosition);

            }
        });
        holder.click_are_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignOrderMethod(order_id, driver_id, current.getId(), context);
            }
        });

        holder.verify_tv.setVisibility(View.GONE);
        holder.trash_vehicle.setVisibility(View.GONE);


        if (selectedPosition == position) {
            holder.itemView.setSelected(true); //using selector drawable
            holder.saved_radio.setChecked(true);
        } else {
            holder.itemView.setSelected(false);
            holder.saved_radio.setChecked(false);
        }


    }

    @Override
    public int getItemCount() {
        return mVehicalDTOS.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView vehicle_name_tv, vehicle_type_tv, vehicle_number;
        RadioButton saved_radio;
        LinearLayout click_are_LL;
        ImageView verify_tv, trash_vehicle;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            vehicle_name_tv = itemView.findViewById(R.id.vehicle_name_tv);
            vehicle_type_tv = itemView.findViewById(R.id.vehicle_type_tv);
            saved_radio = itemView.findViewById(R.id.radio_saved);
            trash_vehicle = itemView.findViewById(R.id.trash_vehicle);
            click_are_LL = itemView.findViewById(R.id.click_are_LL);
            verify_tv = itemView.findViewById(R.id.verify_tv);
            vehicle_number = itemView.findViewById(R.id.vehicle_number);

            sessionManager = new SessionManager(context);
        }
    }


    private void assignOrderMethod(String order_id, String driver_id, String vehicle_id, Context context) {
        final Dialog dialog = Utils.initProgressDialog(this.context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("courier_id", sessionManager.getRegisterUser().getId());
        hm.put("order_id", order_id);
        hm.put("assign_driver", driver_id);

        Call<AccpetOrderDTO> call = apiService.courier_accept_order(hm);
        call.enqueue(new Callback<AccpetOrderDTO>() {
            @Override
            public void onResponse(Call<AccpetOrderDTO> call, Response<AccpetOrderDTO> response) {
                dialog.dismiss();
                try {

                    AccpetOrderDTO body = response.body();

                    if (body.getStatus() == 200) {

                        //  context.startActivity(new Intent(context, HomeActivity.class));
                        assignVehicleMethod(vehicle_id, order_id, context);
                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<AccpetOrderDTO> call, Throwable t) {
                call.cancel();
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }

    private void assignVehicleMethod(String vehicle_id, String order_id, Context context) {
       // final Dialog dialog = Utils.initProgressDialog(this.context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("courier_id", sessionManager.getRegisterUser().getId());
        hm.put("order_id", order_id);
        hm.put("assign_vehicle", vehicle_id);


        Call<AllResponse> call = apiService.courier_assign_vehicle(hm);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
            //    dialog.dismiss();
                try {

                    AllResponse body = response.body();

                    if (body.getStatus() == 200) {

                   // Utils.I_clear(context, HomeActivity.class);


                      context.startActivity(new Intent(context, HomeActivity.class));

                      //  DriverAssignAdapter.this.context
                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                call.cancel();
                t.printStackTrace();
              //  dialog.dismiss();
            }
        });
    }

}

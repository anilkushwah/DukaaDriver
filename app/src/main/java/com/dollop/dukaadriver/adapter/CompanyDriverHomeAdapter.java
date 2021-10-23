package com.dollop.dukaadriver.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.activity.AcceptOrderActivity;
import com.dollop.dukaadriver.activity.AcceptOrderDriverActivity;
import com.dollop.dukaadriver.model.AccpetOrderDTO;
import com.dollop.dukaadriver.model.OrderDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyDriverHomeAdapter extends RecyclerView.Adapter<CompanyDriverHomeAdapter.MyViewHolder> {
    Context context;
    ArrayList<OrderDTO> mOrderDTOArrayList;
    SessionManager sessionManager;

    public CompanyDriverHomeAdapter(Context context, ArrayList<OrderDTO> mList) {
        this.context = context;
        this.mOrderDTOArrayList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.driver_home_order_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.order_id_TV.setText("#000" + mOrderDTOArrayList.get(position).getId());

        DateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.ENGLISH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd yyyy h:mm a");
        // SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
        Date dateasdf = null;
        int hours = 0;

            try {
            dateasdf = output.parse(mOrderDTOArrayList.get(position).getCreateDate());
            long mills = new Date().getTime() - dateasdf.getTime();
            hours = (int) (mills / (1000 * 60 * 60));
            int mins = (int) (mills % (1000 * 60 * 60));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(dateasdf != null ) {
            holder.order_date_tv.setText(dateFormat.format(dateasdf));
        }
        else{
            Utils.E("Null date"+mOrderDTOArrayList.get(position).getCreateDate());
        }

       // holder.order_date_tv.setText(mOrderDTOArrayList.get(position).getCreateDate());

        holder.pickup_address_Tv.setText(mOrderDTOArrayList.get(position).getDistributor_landmark()+","+mOrderDTOArrayList.get(position).getDistributorAddress());
        holder.drop_address.setText(mOrderDTOArrayList.get(position).getRetailer_landmark()+","+mOrderDTOArrayList.get(position).getRetailerAddress());

        holder.total_amouny.setText("Delivery charges-" + mOrderDTOArrayList.get(position).getDeliveryCharge());
        holder.total_vat_TV.setText("Weight-");
        holder.tv_recipientId.setText(mOrderDTOArrayList.get(position).getShopName());


        holder.new_order_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                acceptOrderMethod(mOrderDTOArrayList.get(position));
                context.startActivity(new Intent(context, AcceptOrderDriverActivity.class)
                        .putExtra("model", mOrderDTOArrayList.get(position)).putExtra("stage", "0"));
                ((Activity) context).finish();

            }
        });
        holder.vehical_name.setText(mOrderDTOArrayList.get(position).getVehicle().get(0).getVehicleName());
        holder.vehical__registrationNumber.setText(mOrderDTOArrayList.get(position).getVehicle().get(0).getVehicleRegistrionNumber());

    }

    @Override
    public int getItemCount() {
        return mOrderDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView order_id_TV, order_date_tv, pickup_address_Tv, drop_address, total_amouny;
        TextView total_vat_TV, vehical_name, vehical__registrationNumber,tv_recipientId;
        LinearLayout new_order_layout;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            order_id_TV = itemView.findViewById(R.id.order_id_TV);
            order_date_tv = itemView.findViewById(R.id.order_date_tv);
            pickup_address_Tv = itemView.findViewById(R.id.pickup_address_Tv);
            drop_address = itemView.findViewById(R.id.drop_address);
            total_amouny = itemView.findViewById(R.id.total_amouny);
            total_vat_TV = itemView.findViewById(R.id.total_vat_TV);
            new_order_layout = itemView.findViewById(R.id.new_order_layout);
            vehical_name = itemView.findViewById(R.id.vehical_name);
            vehical__registrationNumber = itemView.findViewById(R.id.vehical__registrationNumber);
            tv_recipientId = itemView.findViewById(R.id.tv_recipientId);


            sessionManager = new SessionManager(context);
        }
    }


    private void acceptOrderMethod(OrderDTO mOrderDTO) {


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", sessionManager.getRegisterUser().getId());
        hm.put("order_id", mOrderDTO.getId());
        hm.put("vehicle_id",mOrderDTO.getVehicle().get(0).getId());
        hm.put("order_status", "Accept");


        Call<AccpetOrderDTO> call = apiService.accept_order(hm);
        call.enqueue(new Callback<AccpetOrderDTO>() {
            @Override
            public void onResponse(Call<AccpetOrderDTO> call, Response<AccpetOrderDTO> response) {

                try {

                    AccpetOrderDTO body = response.body();

                    if (body.getStatus() == 200) {


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

            }
        });
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

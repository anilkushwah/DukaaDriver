package com.dollop.dukaadriver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.NetworkUtil;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.Utility;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.activity.AcceptOrderActivity;
import com.dollop.dukaadriver.activity.AcceptOrderDriverActivity;
import com.dollop.dukaadriver.activity.AssignOrderActivity;
import com.dollop.dukaadriver.activity.HomeActivity;
import com.dollop.dukaadriver.firebase.NotificationUtils;
import com.dollop.dukaadriver.model.AccpetOrderDTO;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.DistanceDTO;
import com.dollop.dukaadriver.model.DistanceRespone;
import com.dollop.dukaadriver.model.OrderDTO;
import com.dollop.dukaadriver.model.VehicalDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    Context context;
    ArrayList<OrderDTO> mOrderDTOArrayList;
    SessionManager sessionManager;
    private ArrayList<VehicalDTO> mVehicalDTOS;

    public HomeAdapter(Context context, ArrayList<OrderDTO> mList) {
        this.context = context;
        this.mOrderDTOArrayList = mList;
        sessionManager = new SessionManager(context);
        getVehicalMethod();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_order_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        show_distributor_distance(mOrderDTOArrayList.get(position), holder);
        show_retailer_distance(mOrderDTOArrayList.get(position), holder);
        holder.order_id_TV.setText("#000" + mOrderDTOArrayList.get(position).getId());
        holder.order_date_tv.setText(mOrderDTOArrayList.get(position).getCreateDate());
        holder.pickup_address_Tv.setText(mOrderDTOArrayList.get(position).getDistributor_landmark() + "," + mOrderDTOArrayList.get(position).getDistributorAddress());
        holder.drop_address.setText(mOrderDTOArrayList.get(position).getRetailer_landmark() + "," + mOrderDTOArrayList.get(position).getRetailerAddress());
        holder.tv_total_item_weight.setText(mOrderDTOArrayList.get(position).getTotal_weight() + " " + mOrderDTOArrayList.get(position).getWeight_unit());
        holder.tv_total_item.setText(mOrderDTOArrayList.get(position).getItemCount() + " Item");

        holder.total_amouny.setText("Delivery charges-" + mOrderDTOArrayList.get(position).getDeliveryCharge());
        holder.total_vat_TV.setText("Weight-");
        holder.tv_recipientId.setText(mOrderDTOArrayList.get(position).getShopName());

        holder.accepte_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationUtils.clearNotifications(context);
                if (sessionManager.is_DRIVER()) {

                    if (NetworkUtil.isNetworkAvailable(context)) {
                        acceptOrderMethod("Accept", mOrderDTOArrayList.get(position));
                    } else {
                        Utility.netConnect(context);

                    }
                } else {

                    if (NetworkUtil.isNetworkAvailable(context)) {
                        acceptOrderCourirMethod("Accept", mOrderDTOArrayList.get(position));
                    } else {
                        Utility.netConnect(context);

                    }

                }
            }
        });
        holder.Reject_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationUtils.clearNotifications(context);
                if (sessionManager.is_DRIVER()) {

                    if (NetworkUtil.isNetworkAvailable(context)) {
                        acceptOrderMethod("Reject", mOrderDTOArrayList.get(position));
                    } else {
                        Utility.netConnect(context);

                    }
                } else {

                    if (NetworkUtil.isNetworkAvailable(context)) {
                        acceptOrderCourirMethod("Reject", mOrderDTOArrayList.get(position));
                    } else {
                        Utility.netConnect(context);

                    }

                }
            }
        });
        if (mOrderDTOArrayList.get(position).getDistributorImage() != null) {
            Glide.with(context)
                    .load(ApiClient.BASE_URL + mOrderDTOArrayList.get(position).getDistributorImage())
                    .error(R.drawable.individual_driver)
                    .into(holder.driver_image);
        }


    }

    @Override
    public int getItemCount() {
        return mOrderDTOArrayList.size();
    }

    private void show_distributor_distance(OrderDTO mOrderDTO, MyViewHolder holder) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_lat", sessionManager.getCurrent_LAT());
        hm.put("driver_long", sessionManager.getCurrent_LONG());
        hm.put("distributor_lat", mOrderDTO.getStoreLat());
        hm.put("distributor_long", mOrderDTO.getStoreLong());
        hm.put("driver_id", sessionManager.getRegisterUser().getId());
        Call<DistanceDTO> call = apiService.driver_distance(hm);
        call.enqueue(new Callback<DistanceDTO>() {
            @Override
            public void onResponse(Call<DistanceDTO> call, Response<DistanceDTO> response) {

                try {

                    DistanceDTO body = response.body();

                    if (body.getStatus() == 200) {
                        DistanceRespone mDistanceRespone = body.getDistance();
                        holder.distributor_distance_tv.setText("" + mDistanceRespone.getDistanceInKm() + " KM");

                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<DistanceDTO> call, Throwable t) {
                call.cancel();
                t.printStackTrace();

            }
        });
    }

    private void show_retailer_distance(OrderDTO mOrderDTO, MyViewHolder holder) {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_lat", sessionManager.getCurrent_LAT());
        hm.put("driver_long", sessionManager.getCurrent_LONG());
        hm.put("driver_id", sessionManager.getRegisterUser().getId());
        hm.put("retailer_lat", mOrderDTO.getRetailerLat());
        hm.put("retailer_long", mOrderDTO.getRetailerLong());

        Call<DistanceDTO> call = apiService.driver_distance(hm);

        call.enqueue(new Callback<DistanceDTO>() {
            @Override
            public void onResponse(Call<DistanceDTO> call, Response<DistanceDTO> response) {

                try {

                    DistanceDTO body = response.body();

                    if (body.getStatus() == 200) {

                        DistanceRespone mDistanceRespone = body.getDistance();
                        holder.retailer_distance_tv.setText("" + mDistanceRespone.getDistanceInKm() + " KM");

                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<DistanceDTO> call, Throwable t) {
                call.cancel();
                t.printStackTrace();

            }
        });
    }

    private void getVehicalMethod() {


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", sessionManager.getRegisterUser().getId());

        Call<AllResponse> call = apiService.view_vehicle(hm);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {

                try {

                    AllResponse body = response.body();

                    if (body.getStatus() == 200) {

                        mVehicalDTOS = body.getmVehicalDTOS();


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

            }
        });
    }

    private void acceptOrderMethod(String status, OrderDTO mOrderDTO) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", sessionManager.getRegisterUser().getId());
        hm.put("order_id", mOrderDTO.getId());
        hm.put("vehicle_id", mVehicalDTOS.get(0).getId());
        hm.put("order_status", status);
    Utils.E("Accept::::"+hm);

        Call<AccpetOrderDTO> call = apiService.accept_order(hm);
        call.enqueue(new Callback<AccpetOrderDTO>() {
            @Override
            public void onResponse(Call<AccpetOrderDTO> call, Response<AccpetOrderDTO> response) {

                try {

                    AccpetOrderDTO body = response.body();

                    if (body.getStatus() == 200) {
                        if (status.equals("Reject")) {

                            Utils.I_clear(context, HomeActivity.class, null);

                        } else {

                            context.startActivity(new Intent(context, AcceptOrderDriverActivity.class)
                                    .putExtra("model", mOrderDTO).putExtra("stage", "0"));

                        }

                    } else {
                        ShowDialog(body.getMessage());
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

    private void acceptOrderCourirMethod(String status, OrderDTO mOrderDTO) {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("courier_id", sessionManager.getRegisterUser().getId());
        hm.put("order_id", mOrderDTO.getId());
        hm.put("order_status", status);

        Call<AccpetOrderDTO> call = apiService.courier_accept_order(hm);
        call.enqueue(new Callback<AccpetOrderDTO>() {
            @Override
            public void onResponse(Call<AccpetOrderDTO> call, Response<AccpetOrderDTO> response) {

                try {

                    AccpetOrderDTO body = response.body();

                    if (body.getStatus() == 200) {

                        if (status.equals("Reject")) {

                            Utils.I_clear(context, HomeActivity.class, null);

                        } else {
                          /*  context.startActivity(new Intent(context, AssignOrderActivity.class)
                                    .putExtra("object", mOrderDTO));*/
                            context.startActivity(new Intent(context, AssignOrderActivity.class)
                                    .putExtra("ID", mOrderDTO.getId())
                                    .putExtra("VehicleType", mOrderDTO.getVehicle_type()));
                        }


                    } else {
                        ShowDialog(body.getMessage());
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
                        Utils.I_clear(context, HomeActivity.class, null);

                        dialog.dismiss();
                    }
                }).show();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView order_id_TV, order_date_tv, pickup_address_Tv, drop_address, total_amouny;
        TextView total_vat_TV;
        TextView distributor_distance_tv;
        TextView retailer_distance_tv;
        TextView tv_total_item_weight;
        TextView tv_total_item;
        Button accepte_btn;
        TextView Reject_btn,tv_recipientId;
        ImageView driver_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_total_item = itemView.findViewById(R.id.tv_total_item);
            order_id_TV = itemView.findViewById(R.id.order_id_TV);
            tv_total_item_weight = itemView.findViewById(R.id.tv_total_item_weight);
            order_date_tv = itemView.findViewById(R.id.order_date_tv);
            pickup_address_Tv = itemView.findViewById(R.id.pickup_address_Tv);
            drop_address = itemView.findViewById(R.id.drop_address);
            total_amouny = itemView.findViewById(R.id.total_amouny);
            total_vat_TV = itemView.findViewById(R.id.total_vat_TV);
            accepte_btn = itemView.findViewById(R.id.accepte_btn);
            driver_image = itemView.findViewById(R.id.driver_image);
            distributor_distance_tv = itemView.findViewById(R.id.distributor_distance_tv);
            retailer_distance_tv = itemView.findViewById(R.id.retailer_distance_tv);
            Reject_btn = itemView.findViewById(R.id.Reject_btn);
            tv_recipientId = itemView.findViewById(R.id.tv_recipientId);


        }
    }
}

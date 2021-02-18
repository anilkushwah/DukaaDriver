package com.dollop.dukaadriver.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.activity.HomeActivity;
import com.dollop.dukaadriver.activity.ManageDriverActivity;
import com.dollop.dukaadriver.activity.ManageVehicleActivity;
import com.dollop.dukaadriver.activity.UpdateVehicleActivity;
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

public class SaveVehicalAdapter extends RecyclerView.Adapter<SaveVehicalAdapter.MyViewHolder> {
    Context context;
    ArrayList<VehicalDTO> mVehicalDTOS;

    private int selectedPosition = -1;
    SessionManager sessionManager;
    //  Dialog dialog;

    public SaveVehicalAdapter(Context context, ArrayList<VehicalDTO> mList) {
        this.context = context;
        this.mVehicalDTOS = mList;
        //   this.dialog = selectvehicle;
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
                // sessionManager.setVehicalData(mVehicalDTOS.get(position));

                //    dialog.dismiss();

            }
        });
        holder.click_are_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition >= 0)
                    notifyItemChanged(selectedPosition);
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(selectedPosition);
                // sessionManager.setVehicalData(mVehicalDTOS.get(position));
//
                //  dialog.dismiss();
            }
        });

        if (current.getIsActive().equals("1")) {
            holder.verify_tv.setVisibility(View.VISIBLE);

        } else {
            holder.click_are_LL.setEnabled(true);
            holder.parent_layout.setBackground(context.getResources().getDrawable(R.drawable.backgroundone));
        }


        if (selectedPosition == position) {
            holder.itemView.setSelected(true); //using selector drawable
            holder.saved_radio.setChecked(true);
        } else {
            holder.itemView.setSelected(false);
            holder.saved_radio.setChecked(false);
        }

/*
        holder.textViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.textViewOptions);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:

                                Intent n_act = new Intent(context, UpdateVehicleActivity.class).putExtra("model", mVehicalDTOS.get(position));
                                context.startActivity(n_act);

                                break;
                            case R.id.delete:


                                deleteVehicalMethod(context, mVehicalDTOS.get(position).getId(), position);


                                break;

                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
*/
        holder.trash_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
             //   alert.setTitle("Delete");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        deleteVehicalMethod(context, mVehicalDTOS.get(position).getId(), position);
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();



            }
        });

    }

    @Override
    public int getItemCount() {
        return mVehicalDTOS.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView vehicle_name_tv, vehicle_type_tv,vehicle_number;
        RadioButton saved_radio;
        LinearLayout click_are_LL, parent_layout;
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
            parent_layout = itemView.findViewById(R.id.parent_layout);

            sessionManager = new SessionManager(context);
        }
    }


    public void removeItem(int position) {
        mVehicalDTOS.remove(position);
        notifyItemRemoved(position);
        //sessionManager.setVehicalData(mVehicalDTOArrayList.get(position));
    }


    private void deleteVehicalMethod(final Context mContext, final String id, int position) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("vehicle_id", id);

        Call<AllResponse> call = apiService.delete_vehicle(hm);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                try {

                    AllResponse body = response.body();


                    if (body.getStatus() == 200) {


                        removeItem(position);
                        notifyDataSetChanged();
                        context.startActivity(new Intent(context, ManageVehicleActivity.class));


                    } else {
                        // new CustomToast().Show_Toast(context, view, body.getMessage(), false);

                    }

                } catch (Exception e) {
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




}

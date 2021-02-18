package com.dollop.dukaadriver.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.activity.CompanyUpdateDriverProfile;
import com.dollop.dukaadriver.activity.DriverUpdateProfileActivity;
import com.dollop.dukaadriver.activity.HomeActivity;
import com.dollop.dukaadriver.activity.ManageDriverActivity;
import com.dollop.dukaadriver.activity.MyRatingActivity;
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

public class ManageDriverAdapter extends RecyclerView.Adapter<ManageDriverAdapter.MyViewHolder> {

    Context context;
    ArrayList<DriverDTO> mDriverDTOArrayList;

    public ManageDriverAdapter(Context context, ArrayList<DriverDTO> mList) {
        this.context = context;
        this.mDriverDTOArrayList = mList;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.manage_driver_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (mDriverDTOArrayList.get(position).getProfileImg() != null) {
            Glide.with(context)
                    .load(ApiClient.BASE_URL + mDriverDTOArrayList.get(position).getProfileImg())
                    .error(R.drawable.individual_driver)
                    .into(holder.driver_image);

        }
        holder.driver_status_tv.setText(mDriverDTOArrayList.get(position).getStatus());
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
                                //  context.startActivity(new Intent(context, CompanyUpdateDriverProfile.class)
                                // .putExtra("model", mDriverDTOArrayList.get(position)));

                                Intent intent = new Intent(context, CompanyUpdateDriverProfile.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("model", mDriverDTOArrayList.get(position));
                                intent.putExtras(bundle);
                                context.startActivity(intent);

                                break;
                            case R.id.delete:
                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                               // alert.setTitle("Delete");
                                alert.setMessage("Are you sure you want to delete?");
                                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteDriverMethod(mDriverDTOArrayList.get(position).getId(), position);
                                    }
                                });
                                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                alert.show();

                                break;

                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
        holder.driver_name_TV.setText(mDriverDTOArrayList.get(position).getFullName());
        holder.driver_number_tv.setText(mDriverDTOArrayList.get(position).getMobile());
        holder.view_rating_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MyRatingActivity.class)
                        .putExtra("id", mDriverDTOArrayList.get(position).getId()).putExtra("type", "company"));
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDriverDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView driver_status_tv, textViewOptions;
        TextView driver_name_TV, driver_number_tv, view_rating_tv;
        ImageView driver_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            driver_status_tv = itemView.findViewById(R.id.driver_status_tv);
            textViewOptions = itemView.findViewById(R.id.textViewOptions);
            driver_name_TV = itemView.findViewById(R.id.driver_name_TV);
            driver_number_tv = itemView.findViewById(R.id.driver_number_tv);
            driver_image = itemView.findViewById(R.id.driver_image);
            view_rating_tv = itemView.findViewById(R.id.view_rating_tv);

        }
    }

    public void removeItem(int position) {
        mDriverDTOArrayList.remove(position);
        notifyItemRemoved(position);
        //sessionManager.setVehicalData(mVehicalDTOArrayList.get(position));
    }

    private void deleteDriverMethod(String id, int position) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", id);

        Call<AllResponse> call = apiService.courier_delete_driver(hm);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                try {

                    AllResponse body = response.body();


                    if (body.getStatus() == 200) {


                        removeItem(position);
                        context.startActivity(new Intent(context, ManageDriverActivity.class));


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



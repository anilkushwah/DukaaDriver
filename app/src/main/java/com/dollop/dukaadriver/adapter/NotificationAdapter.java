package com.dollop.dukaadriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.model.NotificationDTO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    Context context;
    ArrayList<NotificationDTO> notificationModelList;

    public NotificationAdapter(Context context,ArrayList<NotificationDTO> notificationModelList){
        this.context=context;
        this.notificationModelList=notificationModelList;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.notification_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {
        NotificationDTO homeViewModel = notificationModelList.get(position);

        DateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.ENGLISH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd yyyy h:mm a");
        // SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
        Date dateasdf = null;
        int hours = 0;
        try {
            dateasdf = output.parse(homeViewModel.getCreatedDate());
            long mills = new Date().getTime() - dateasdf.getTime();
            hours = (int) (mills / (1000 * 60 * 60));
            int mins = (int) (mills % (1000 * 60 * 60));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tv_notificationTime.setText(dateFormat.format(dateasdf));

        holder.tv_notificaiton_Description.setText(homeViewModel.getMessage());


    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_notificationTime,tv_notificaiton_Description;
        ImageView iv_ProductImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_notificationTime = itemView.findViewById(R.id.tv_notificationTime);
            tv_notificaiton_Description = itemView.findViewById(R.id.tv_notificaiton_Description);

        }
    }
}


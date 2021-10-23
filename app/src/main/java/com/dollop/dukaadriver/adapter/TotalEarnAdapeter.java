package com.dollop.dukaadriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.model.TotalEarnModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TotalEarnAdapeter extends RecyclerView.Adapter<TotalEarnAdapeter.MyViewHolder> {
    Context context;
    ArrayList<TotalEarnModel> totalEarnModels = new ArrayList<>();

    public TotalEarnAdapeter(Context context, ArrayList<TotalEarnModel> totalEarnModels) {
        this.context = context;
        this.totalEarnModels = totalEarnModels;
    }

    @NonNull
    @Override
    public TotalEarnAdapeter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.totalearn, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TotalEarnAdapeter.MyViewHolder holder, int position) {

        TotalEarnModel totalEarnModel = totalEarnModels.get(position);
        holder.orderId.setText("Order ID: #000" + totalEarnModel.getOrderId());
        holder.PaymetnMethod.setText(totalEarnModel.getTransactionMode());

        holder.delivery_charge_tv.setText("Delivery Charges: " + context.getString(R.string.currency_sign) + totalEarnModel.getDeliveryCharge());
        String date = totalEarnModel.getCreateDate();
       // SimpleDateFormat spf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate = null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat("dd MMM yyyy");
        String newDateString = spf.format(newDate);
        System.out.println(newDateString);

        holder.Time.setText("Order Date : " +newDateString);

    }

    @Override
    public int getItemCount() {
        return totalEarnModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, PaymetnMethod, Time;
        TextView delivery_charge_tv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            orderId = itemView.findViewById(R.id.earnorderId_tv);
            PaymetnMethod = itemView.findViewById(R.id.earnpaymethod_tv);
            Time = itemView.findViewById(R.id.earnTime_tv);
            delivery_charge_tv = itemView.findViewById(R.id.delivery_charge_tv);


        }
    }
}

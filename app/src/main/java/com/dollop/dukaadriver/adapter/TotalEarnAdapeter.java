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

import java.util.ArrayList;

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
        View view = LayoutInflater.from(context).inflate(R.layout.totalearn,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TotalEarnAdapeter.MyViewHolder holder, int position) {

        TotalEarnModel totalEarnModel = totalEarnModels.get(position);
        holder.orderId.setText(totalEarnModel.OrderId);
        holder.PaymetnMethod.setText(totalEarnModel.PaymentMehtod);
        holder.Time.setText(totalEarnModel.Time);
        holder.Payment.setText("₹ "+totalEarnModel.Payment);
        holder.Location.setText(totalEarnModel.Location);
    }

    @Override
    public int getItemCount() {
        return totalEarnModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderId,PaymetnMethod,Location,Time,Payment,ViewDetails;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            orderId = itemView.findViewById(R.id.earnorderId_tv);
            PaymetnMethod = itemView.findViewById(R.id.earnpaymethod_tv);
            Time = itemView.findViewById(R.id.earnTime_tv);
            Payment = itemView.findViewById(R.id.earnpaymnet_tv);
            Location = itemView.findViewById(R.id.earnlocation_tv);
            ViewDetails = itemView.findViewById(R.id.earnViewdetails_tv);
        }
    }
}

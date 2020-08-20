package com.dollop.dukaadriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.model.PostorderModel;

import java.util.ArrayList;

public class PostOrderAdapter extends RecyclerView.Adapter<PostOrderAdapter.MyViewHolder> {
    public PostOrderAdapter(Context context, ArrayList<PostorderModel> postorderModels) {
        this.context = context;
        this.postorderModels = postorderModels;
    }

    Context context;
    ArrayList<PostorderModel> postorderModels = new ArrayList<>();


    @NonNull
    @Override
    public PostOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.post_order,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostOrderAdapter.MyViewHolder holder, int position) {

        PostorderModel postorderModel = postorderModels.get(position);

        holder.Pickuplocation.setText(postorderModel.PickupLocation);
        holder.Droplocation.setText(postorderModel.DropLocation);
        holder.OrderId.setText("OrderId : "+postorderModel.OrderId);
        holder.PaymentMethod.setText(postorderModel.PaymentMwthod);
        holder.Price.setText("₹ "+postorderModel.Price);
        holder.Time.setText(postorderModel.Time);
    }

    @Override
    public int getItemCount() {
        return postorderModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Pickuplocation,Droplocation,Price,PaymentMethod,Time,OrderId;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Pickuplocation = itemView.findViewById(R.id.postpickuplocation_tv);
            Droplocation = itemView.findViewById(R.id.postDrop_tv);
            Price = itemView.findViewById(R.id.postpaymnet_tv);
            PaymentMethod = itemView.findViewById(R.id.postpaymethod_tv);
            Time = itemView.findViewById(R.id.postorderTime_tv);
            OrderId = itemView.findViewById(R.id.postorderId_tv);



        }
    }
}

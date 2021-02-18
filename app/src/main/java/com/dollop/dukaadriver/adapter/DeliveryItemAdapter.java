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
import com.dollop.dukaadriver.model.DeliveryItemModel;

import java.util.ArrayList;

public class DeliveryItemAdapter extends RecyclerView.Adapter<DeliveryItemAdapter.MyViewHolder> {

    Context context;
    ArrayList<DeliveryItemModel> deliveryItemModels;

    public DeliveryItemAdapter(Context context, ArrayList<DeliveryItemModel> deliveryItemModels) {
        this.context = context;
        this.deliveryItemModels = deliveryItemModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.delivery_orderlist,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        DeliveryItemModel deliveryItemModel = deliveryItemModels.get(position);

        holder.iv_deliveryItemImage.setImageResource(deliveryItemModel.item_Image);
        holder.tv_deleiveryitemsName.setText(deliveryItemModel.item_Name);
        holder.tv_item_price_per_item.setText(deliveryItemModel.item_Price);
        holder.item_qty_tv.setText(deliveryItemModel.item_Quantity);


    }

    @Override
    public int getItemCount() {
        return deliveryItemModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_deliveryItemImage;
        TextView tv_deleiveryitemsName,item_qty_tv,tv_item_price_per_item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_deliveryItemImage = itemView.findViewById(R.id.iv_deliveryItemImage);
            tv_deleiveryitemsName = itemView.findViewById(R.id.tv_deleiveryitemsName);
            item_qty_tv = itemView.findViewById(R.id.item_qty_tv);
            tv_item_price_per_item = itemView.findViewById(R.id.tv_item_price_per_item);
        }
    }
}

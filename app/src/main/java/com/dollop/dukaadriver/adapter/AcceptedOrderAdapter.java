package com.dollop.dukaadriver.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.activity.AcceptOrderActivity;
import com.dollop.dukaadriver.activity.AssignOrderActivity;
import com.dollop.dukaadriver.model.DriverDTO;
import com.dollop.dukaadriver.model.OrderDTO;
import com.dollop.dukaadriver.model.ProductDatum;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AcceptedOrderAdapter extends RecyclerView.Adapter<AcceptedOrderAdapter.MyViewHolder> {

    Context context;
    ArrayList<OrderDTO> orderDTOS;

    public AcceptedOrderAdapter(Context context, ArrayList<OrderDTO> mList) {

        this.context = context;
        this.orderDTOS = mList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.accepted_order_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.order_id_TV.setText("#000" + orderDTOS.get(position).getId());

        DateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.ENGLISH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd yyyy h:mm a");
        // SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
        Date dateasdf = null;
        int hours = 0;
        try {
            dateasdf = output.parse(orderDTOS.get(position).getCreateDate());
            long mills = new Date().getTime() - dateasdf.getTime();
            hours = (int) (mills / (1000 * 60 * 60));
            int mins = (int) (mills % (1000 * 60 * 60));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.order_date_tv.setText(dateFormat.format(dateasdf));
        holder.pickup_address_Tv.setText(orderDTOS.get(position).getDistributorAddress());
        holder.drop_address.setText(orderDTOS.get(position).getRetailerAddress());

        holder.totalWeight.setText("Weight-" + orderDTOS.get(position).getTotal_weight() + " " + orderDTOS.get(position).getWeight_unit());

        holder.btn_assigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(context, AssignOrderActivity.class)
                        .putExtra("ID", orderDTOS.get(position).getId())
                        .putExtra("VehicleType", orderDTOS.get(position).getVehicle_type())
                );

            }
        });
        holder.ItemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductListDialog(orderDTOS.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderDTOS.size();
    }

    private void ProductListDialog(OrderDTO orderDTO) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_product_list);
        RecyclerView rv_product_list = dialog.findViewById(R.id.rv_product_list);
        rv_product_list.setLayoutManager(new LinearLayoutManager(context));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setAttributes(lp);
        ItemAdapter itemAdapter = new ItemAdapter(context, orderDTO.getProductData());
        rv_product_list.setAdapter(itemAdapter);
        TextView tv_total_ProductPrice = dialog.findViewById(R.id.tv_total_ProductPrice);
        tv_total_ProductPrice.setText(context.getString(R.string.currency_sign) + orderDTO.getProductDiscountedPrice());
        dialog.show();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView order_id_TV, order_date_tv, totalWeight, pickup_address_Tv, drop_address;
        Button btn_assigned;
        LinearLayout ItemList;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            order_id_TV = itemView.findViewById(R.id.order_id_TV);
            order_date_tv = itemView.findViewById(R.id.order_date_tv);
            pickup_address_Tv = itemView.findViewById(R.id.pickup_address_Tv);
            drop_address = itemView.findViewById(R.id.drop_address);


            btn_assigned = itemView.findViewById(R.id.btn_assigned);
            totalWeight = itemView.findViewById(R.id.totalWeight);
            ItemList = itemView.findViewById(R.id.ItemList);

        }

    }


    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

        ArrayList<ProductDatum> itemModels;
        Context context;

        public ItemAdapter(Context context, ArrayList<ProductDatum> itemModels) {
            this.context = context;
            this.itemModels = itemModels;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.order_item_list, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ProductDatum itemModel = itemModels.get(position);
            int Position = position + 1;
            holder.item_count_tv.setText(Position + ") ");
            holder.item_name_id.setText(itemModel.productName);
            int Amount = Integer.parseInt(itemModel.productAmount) * Integer.parseInt(itemModel.productQty);
            holder.item_price_tv.setText(context.getString(R.string.currency_sign) + Amount);
            holder.item_description_tv.setText(itemModel.productQty + " " + itemModel.packing);
            if (position == itemModels.size() - 1) {
                holder.viewLine.setVisibility(View.GONE);
            } else {
                holder.viewLine.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public int getItemCount() {
            return itemModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView item_count_tv;
            TextView item_name_id;
            TextView item_description_tv;
            TextView item_price_tv;
            View viewLine;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                viewLine = itemView.findViewById(R.id.viewLine);
                item_count_tv = itemView.findViewById(R.id.item_count_tv);
                item_description_tv = itemView.findViewById(R.id.item_description_tv);
                item_name_id = itemView.findViewById(R.id.item_name_id);
                item_price_tv = itemView.findViewById(R.id.item_price_tv);
            }
        }
    }

}




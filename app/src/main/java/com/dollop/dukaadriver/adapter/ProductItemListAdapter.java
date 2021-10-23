package com.dollop.dukaadriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.model.OrderDTO;
import com.dollop.dukaadriver.model.OrderItem;

import java.util.ArrayList;

public class ProductItemListAdapter extends RecyclerView.Adapter<ProductItemListAdapter.ViewHolder> {

        ArrayList<OrderDTO> itemModels;
        Context context;

public ProductItemListAdapter(Context context, ArrayList<OrderDTO> itemModels) {
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
    OrderDTO itemModel = itemModels.get(position);
      //  holder.item_count_tv.setText(itemModel.Position + ") ");
        holder.item_name_id.setText(itemModel.orderItem.get(position).getProductName());
        int Amount = Integer.parseInt(itemModel.orderItem.get(position).getDiscountAmount()) * Integer.parseInt(itemModel.orderItem.get(position).getProductQty());

        String ItemAmount= (Utils.getFormatedAmount(Amount));

        holder.item_price_tv.setText(context.getString(R.string.currency_sign) + ItemAmount);
        holder.item_description_tv.setText(itemModel.orderItem.get(position).getProductQty() + " " + itemModel.orderItem.get(position).getpacking());
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

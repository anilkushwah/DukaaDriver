package com.dollop.dukaadriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.model.savedaddress_model;

import java.util.ArrayList;
import java.util.List;

public class SavedAddressAdapter extends RecyclerView.Adapter<SavedAddressAdapter.MyViewHolder> {
    Context context;
    List<savedaddress_model>  savedaddress_models = new ArrayList<>();

    public SavedAddressAdapter(Context context, List<savedaddress_model> savedaddress_models) {
        this.context = context;
        this.savedaddress_models = savedaddress_models;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.saved_adddress_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        savedaddress_model  current  = savedaddress_models.get(position);

        holder.address.setText(current.getAddressname().toString());
        holder.fulladdress.setText(current.getFulladdress().toString());

    }

    @Override
    public int getItemCount() {
        return savedaddress_models.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView address,fulladdress;
        RadioButton saved_radio;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address_name);
            fulladdress = itemView.findViewById(R.id.full_address);
            saved_radio = itemView.findViewById(R.id.radio_saved);


        }
    }
}

package com.dollop.dukaadriver.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.model.OrderDTO;
import com.dollop.dukaadriver.model.PostorderModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.icu.lang.UProperty.INT_START;

public class PostOrderAdapter extends RecyclerView.Adapter<PostOrderAdapter.MyViewHolder> {

    ArrayList<OrderDTO> mOrderDTOArrayListPost;
    Context context;


    public PostOrderAdapter(Context context, ArrayList<OrderDTO> mList) {
        this.context = context;
        this.mOrderDTOArrayListPost = mList;
    }


    @NonNull
    @Override
    public PostOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.post_order, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostOrderAdapter.MyViewHolder holder, int position) {

        OrderDTO postorderModel = mOrderDTOArrayListPost.get(position);

        holder.Pickuplocation.setText(postorderModel.getDistributorAddress());
        holder.Droplocation.setText(postorderModel.getRetailerAddress());


        holder.OrderId.setText("OrderId : #000" + postorderModel.getId());
        holder.theName.setText("Name: " + postorderModel.getRetailerName());
        holder.PaymentMethod.setText(postorderModel.getTransactionMode());
        holder.Price.setText(context.getString(R.string.currency_sign) + postorderModel.getDeliveryCharge());

        String date = postorderModel.getCreateDate();
    /*    SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate = null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat("dd MMM yyyy");
        String newDateString = spf.format(newDate);
        System.out.println(newDateString);
*/
        holder.Time.setText("Order Date : " + date);

    }

    @Override
    public int getItemCount() {
        return mOrderDTOArrayListPost.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Pickuplocation, Droplocation, Price, PaymentMethod, Time, OrderId, theName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Pickuplocation = itemView.findViewById(R.id.postpickuplocation_tv);
            Droplocation = itemView.findViewById(R.id.postDrop_tv);
            Price = itemView.findViewById(R.id.postpaymnet_tv);
            PaymentMethod = itemView.findViewById(R.id.postpaymethod_tv);
            Time = itemView.findViewById(R.id.postorderTime_tv);
            OrderId = itemView.findViewById(R.id.postorderId_tv);
            theName = itemView.findViewById(R.id.name_tv);


        }
    }
}

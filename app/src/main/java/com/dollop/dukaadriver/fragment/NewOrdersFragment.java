package com.dollop.dukaadriver.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.adapter.NewOrderAdapter;
import com.dollop.dukaadriver.adapter.PostOrderAdapter;
import com.dollop.dukaadriver.model.NewOrderModel;
import com.dollop.dukaadriver.model.PostorderModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class NewOrdersFragment extends Fragment {

    String Pickup = "Walmart-Bypass 117-Road-2, Bypass,Indore";
    String Droplocation = "121 block-E,i-bus stop, Near Anand Super,Indore";
    String  Distance = "28KM";
    String Time = "10:20 AM";
    String Price = "200";
    String OrderId = "#337647";
    String Paymentmethod = "cash";
    TextView ordertype_tv;
    PostOrderAdapter postOrderAdapter;
    RecyclerView new_order_list;
    Button new_order_rl,post_order_rl;
    NewOrderAdapter newOrderAdapter;
    ArrayList<NewOrderModel> newOrderModels = new ArrayList<>();
    ArrayList<PostorderModel> postorderModels = new ArrayList<>();
    public NewOrdersFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view = inflater.inflate(R.layout.fragment_new_orders, container, false);


        new_order_list = view.findViewById(R.id.new_order_list);
        new_order_rl = view.findViewById(R.id.new_order_btn);
        post_order_rl = view.findViewById(R.id.post_order_btn);
        ordertype_tv = view.findViewById(R.id.ordertype_tv);


        newOrder();
        new_order_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new_order_rl.setTextColor(Color.BLACK);
                new_order_rl.setBackgroundResource(R.drawable.whitebnt);

                post_order_rl.setTextColor(Color.WHITE);
                post_order_rl.setBackgroundResource(R.drawable.laybtn);

                ordertype_tv.setText("All New Orders");
                newOrder();
            }
        });
        post_order_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                post_order_rl.setTextColor(Color.BLACK);
                post_order_rl.setBackgroundResource(R.drawable.whitebnt);


                new_order_rl.setTextColor(Color.WHITE);
                new_order_rl.setBackgroundResource(R.drawable.laybtn);
                ordertype_tv.setText("Post Orders");
                postOrder();
            }
        });



       return view;
    }


    public void newOrder()
    {
        for(int i=0;i<5;i++)
        {
            NewOrderModel newOrderModel = new NewOrderModel();
            newOrderModel.PickupLocation=Pickup;
            newOrderModel.DropLocation = Droplocation;
            newOrderModel.Distance = Distance;
            newOrderModels.add(newOrderModel);
        }

        newOrderAdapter = new NewOrderAdapter(getActivity(),newOrderModels);
        new_order_list.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),RecyclerView.VERTICAL,false));
        new_order_list.setAdapter(newOrderAdapter);

    }

    public void postOrder()
    {

        for(int i=0;i<5;i++)
        {

            PostorderModel postorderModel = new PostorderModel();
            postorderModel.PickupLocation=Pickup;
            postorderModel.DropLocation = Droplocation;
            postorderModel.PaymentMwthod = Paymentmethod;
            postorderModel.OrderId = OrderId;
            postorderModel.Price = Price;
            postorderModel.Time = Time;
            postorderModels.add(postorderModel);
        }
        postOrderAdapter = new PostOrderAdapter(getActivity(),postorderModels);
        new_order_list.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),RecyclerView.VERTICAL,false));
        new_order_list.setAdapter(postOrderAdapter);

    }
}
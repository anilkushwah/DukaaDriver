package com.dollop.dukaadriver.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.model.HomeDTO;
import com.dollop.dukaadriver.activity.HomeActivity;
import com.dollop.dukaadriver.adapter.OnRoutAdapter;
import com.dollop.dukaadriver.adapter.PostOrderAdapter;
import com.dollop.dukaadriver.model.OrderDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class NewOrdersFragment extends Fragment {



    PostOrderAdapter postOrderAdapter;
    RecyclerView new_order_list;
    TextView new_order_rl, post_order_rl;
    //  NewOrderAdapter newOrderAdapter;
    ImageView no_data_image, back_home_img;

    ArrayList<OrderDTO> mOrderDTOArrayList;
    ArrayList<OrderDTO> mOrderDTOArrayListPost;

    OnRoutAdapter mOnRoutAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_orders, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        new_order_list = view.findViewById(R.id.new_order_list);
        new_order_rl = view.findViewById(R.id.new_order_btn);
        post_order_rl = view.findViewById(R.id.post_order_btn);

        no_data_image = view.findViewById(R.id.no_data_image);
        back_home_img = view.findViewById(R.id.back_home_img);

        mOrderDTOArrayList = new ArrayList<>();
        mOrderDTOArrayListPost = new ArrayList<>();

        getVehicalMethod();

        new_order_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new_order_rl.setTextColor(Color.BLACK);
                new_order_rl.setBackgroundResource(R.drawable.whitebnt);

                post_order_rl.setTextColor(Color.WHITE);
                post_order_rl.setBackgroundColor(Color.TRANSPARENT);


                mOrderDTOArrayListPost.clear();
                postOrderAdapter = new PostOrderAdapter(getActivity(), mOrderDTOArrayListPost);
                new_order_list.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));
                new_order_list.setAdapter(postOrderAdapter);
                postOrderAdapter.notifyDataSetChanged();


                getVehicalMethod();
            }
        });
        post_order_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                post_order_rl.setTextColor(Color.BLACK);
                post_order_rl.setBackgroundResource(R.drawable.whitebnt);

                new_order_rl.setTextColor(Color.WHITE);
                new_order_rl.setBackgroundColor(Color.TRANSPARENT);


                mOrderDTOArrayList.clear();
                mOnRoutAdapter = new OnRoutAdapter(getActivity(), mOrderDTOArrayList);
                new_order_list.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),
                        RecyclerView.VERTICAL, false));
                new_order_list.setAdapter(mOnRoutAdapter);
                mOnRoutAdapter.notifyDataSetChanged();

                completeOrder();

            }
        });

        back_home_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               getActivity().onBackPressed();

            }
        });
        return view;
    }


    private void getVehicalMethod() {

        final Dialog dialog = Utils.initProgressDialog(getActivity());
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", ((HomeActivity) getActivity()).sessionManager.getRegisterUser().getId());

        Call<HomeDTO> call = apiService.route_jobs_order(hm);
        call.enqueue(new Callback<HomeDTO>() {
            @Override
            public void onResponse(Call<HomeDTO> call, Response<HomeDTO> response) {
                dialog.dismiss();
                try {

                    HomeDTO body = response.body();

                    if (body.getStatus() == 200) {
                        no_data_image.setVisibility(View.GONE);
                        mOrderDTOArrayList = body.getAllOrder();
                        if (mOrderDTOArrayList != null) {
                            no_data_image.setVisibility(View.GONE);
                        } else {
                            no_data_image.setVisibility(View.VISIBLE);
                        }

                        mOnRoutAdapter = new OnRoutAdapter(getActivity(), mOrderDTOArrayList);
                        new_order_list.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),
                                RecyclerView.VERTICAL, false));
                        new_order_list.setAdapter(mOnRoutAdapter);
                        mOnRoutAdapter.notifyDataSetChanged();
                    } else {
                        no_data_image.setVisibility(View.VISIBLE);
                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<HomeDTO> call, Throwable t) {
                call.cancel();
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }

    private void completeOrder() {

        final Dialog dialog = Utils.initProgressDialog(getActivity());
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", ((HomeActivity) getActivity()).sessionManager.getRegisterUser().getId());
        hm.put("type", "complete");

        Call<HomeDTO> call = apiService.route_jobs_order(hm);
        call.enqueue(new Callback<HomeDTO>() {
            @Override
            public void onResponse(Call<HomeDTO> call, Response<HomeDTO> response) {
                dialog.dismiss();
                try {

                    HomeDTO body = response.body();

                    if (body.getStatus() == 200) {
                        no_data_image.setVisibility(View.GONE);
                        mOrderDTOArrayListPost = body.getAllOrder();
                        if (mOrderDTOArrayList != null) {
                            no_data_image.setVisibility(View.GONE);
                        } else {
                            no_data_image.setVisibility(View.VISIBLE);
                        }
                        postOrderAdapter = new PostOrderAdapter(getActivity(), mOrderDTOArrayListPost);
                        new_order_list.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));
                        new_order_list.setAdapter(postOrderAdapter);
                        postOrderAdapter.notifyDataSetChanged();
                    } else {
                        no_data_image.setVisibility(View.VISIBLE);
                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<HomeDTO> call, Throwable t) {
                call.cancel();
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }

}
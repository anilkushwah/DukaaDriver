package com.dollop.dukaadriver.ui.jobs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.model.HomeDTO;
import com.dollop.dukaadriver.activity.HomeActivity;
import com.dollop.dukaadriver.adapter.AssignedJobsAdapter;
import com.dollop.dukaadriver.adapter.NewOrderAdapter;
import com.dollop.dukaadriver.adapter.OnRoutAdapter;
import com.dollop.dukaadriver.adapter.PostOrderAdapter;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.DriverDTO;
import com.dollop.dukaadriver.model.NewOrderModel;
import com.dollop.dukaadriver.model.OrderDTO;
import com.dollop.dukaadriver.model.PostorderModel;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CourierJobFragment extends Fragment implements View.OnClickListener {

    public static CourierJobFragment newInstance(String param1, String param2) {
        CourierJobFragment fragment = new CourierJobFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    ArrayList<NewOrderModel> newOrderModels;
    View view;
    Button btn_assigned, btn_on_rout, btn_complete;
    RecyclerView jobs_order_RL;
    ArrayList<PostorderModel> postorderModels = new ArrayList<>();
    AssignedJobsAdapter mAssignedJobsAdapter;
    OnRoutAdapter mOnRoutAdapter;
    NewOrderAdapter mNewOrderAdapter;
    PostOrderAdapter postOrderAdapter;
    ArrayList<DriverDTO> mAssignOrdeList;
    ImageView back_home_img, no_data_image;
    ArrayList<OrderDTO> mOrderDTOArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_courier_job, container, false);

        initialization();

        return view;
    }

    private void initialization() {

        newOrderModels = new ArrayList<>();
        mOrderDTOArrayList = new ArrayList<>();
        mAssignOrdeList = new ArrayList<>();
        btn_assigned = view.findViewById(R.id.btn_assigned);
        btn_on_rout = view.findViewById(R.id.btn_on_rout);
        btn_complete = view.findViewById(R.id.btn_complete);
        jobs_order_RL = view.findViewById(R.id.jobs_order_RL);
        back_home_img = view.findViewById(R.id.back_home_img);
        no_data_image = view.findViewById(R.id.no_data_image);

        btn_assigned.setOnClickListener(this);
        btn_on_rout.setOnClickListener(this);
        btn_complete.setOnClickListener(this);
        back_home_img.setOnClickListener(this);

        assignJobsMethod();

    }

    @Override
    public void onClick(View v) {

        if (v == btn_assigned) {
            btn_assigned.setBackgroundResource(R.drawable.whitebnt);
            btn_assigned.setTextColor(getResources().getColor(R.color.black));

            btn_on_rout.setBackgroundResource(R.drawable.laybtn);
            btn_on_rout.setTextColor(getResources().getColor(R.color.white));

            btn_complete.setBackgroundResource(R.drawable.laybtn);
            btn_complete.setTextColor(getResources().getColor(R.color.white));

            mOrderDTOArrayList.clear();
            mOnRoutAdapter = new OnRoutAdapter(getActivity(), mOrderDTOArrayList);
            jobs_order_RL.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));
            jobs_order_RL.setAdapter(mOnRoutAdapter);
            mOnRoutAdapter.notifyDataSetChanged();

            mOrderDTOArrayList.clear();
            postOrderAdapter = new PostOrderAdapter(getActivity(), mOrderDTOArrayList);
            jobs_order_RL.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));
            jobs_order_RL.setAdapter(postOrderAdapter);
            postOrderAdapter.notifyDataSetChanged();


            assignJobsMethod();

        } else if (v == btn_on_rout) {
            btn_on_rout.setBackgroundResource(R.drawable.whitebnt);
            btn_on_rout.setTextColor(getResources().getColor(R.color.black));

            btn_assigned.setBackgroundResource(R.drawable.laybtn);
            btn_assigned.setTextColor(getResources().getColor(R.color.white));


            btn_complete.setBackgroundResource(R.drawable.laybtn);
            btn_complete.setTextColor(getResources().getColor(R.color.white));


            mAssignOrdeList.clear();
            mAssignedJobsAdapter = new AssignedJobsAdapter(getActivity(), mAssignOrdeList);
            jobs_order_RL.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));
            jobs_order_RL.setAdapter(mAssignedJobsAdapter);
            mAssignedJobsAdapter.notifyDataSetChanged();

            mOrderDTOArrayList.clear();
            postOrderAdapter = new PostOrderAdapter(getActivity(), mOrderDTOArrayList);
            jobs_order_RL.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));
            jobs_order_RL.setAdapter(postOrderAdapter);
            postOrderAdapter.notifyDataSetChanged();

            getVehicalMethod();
        } else if (v == btn_complete) {

            btn_complete.setBackgroundResource(R.drawable.whitebnt);
            btn_complete.setTextColor(getResources().getColor(R.color.black));

            btn_on_rout.setBackgroundResource(R.drawable.laybtn);
            btn_on_rout.setTextColor(getResources().getColor(R.color.white));


            btn_assigned.setBackgroundResource(R.drawable.laybtn);
            btn_assigned.setTextColor(getResources().getColor(R.color.white));


            mOrderDTOArrayList.clear();
            mOnRoutAdapter = new OnRoutAdapter(getActivity(), mOrderDTOArrayList);
            jobs_order_RL.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));
            jobs_order_RL.setAdapter(mOnRoutAdapter);
            mOnRoutAdapter.notifyDataSetChanged();

            mAssignOrdeList.clear();
            mAssignedJobsAdapter = new AssignedJobsAdapter(getActivity(), mAssignOrdeList);
            jobs_order_RL.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));
            jobs_order_RL.setAdapter(mAssignedJobsAdapter);
            mAssignedJobsAdapter.notifyDataSetChanged();

            completeOrder();

        } else if (v == back_home_img) {
            getActivity().onBackPressed();
        }
    }

    private void assignJobsMethod() {
        final Dialog dialog = Utils.initProgressDialog(getActivity());
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("courier_id", ((HomeActivity) getActivity()).sessionManager.getRegisterUser().getId());

        Call<AllResponse> call = apiService.pending_assign_driver(hm);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                dialog.dismiss();
                try {

                    AllResponse body = response.body();

                    if (body.getStatus() == 200) {
                        no_data_image.setVisibility(View.GONE);
                        mAssignOrdeList = body.getAssignDriver();

                        if (mAssignOrdeList.isEmpty()) {
                            no_data_image.setVisibility(View.VISIBLE);
                        } else {
                            no_data_image.setVisibility(View.GONE);
                        }

                        mAssignedJobsAdapter = new AssignedJobsAdapter(getActivity(), mAssignOrdeList);
                        jobs_order_RL.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));
                        jobs_order_RL.setAdapter(mAssignedJobsAdapter);
                        mAssignedJobsAdapter.notifyDataSetChanged();

                    } else {
                        no_data_image.setVisibility(View.VISIBLE);
                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                call.cancel();
                t.printStackTrace();
                dialog.dismiss();
            }
        });
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
                        if (mOrderDTOArrayList == null) {
                            no_data_image.setVisibility(View.VISIBLE);
                        } else {
                            no_data_image.setVisibility(View.GONE);
                        }

                        mOnRoutAdapter = new OnRoutAdapter(getActivity(), mOrderDTOArrayList);
                        jobs_order_RL.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));
                        jobs_order_RL.setAdapter(mOnRoutAdapter);
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
                        mOrderDTOArrayList = body.getAllOrder();

                        if (mOrderDTOArrayList == null) {
                            no_data_image.setVisibility(View.VISIBLE);
                        } else {
                            no_data_image.setVisibility(View.GONE);
                        }

                        postOrderAdapter = new PostOrderAdapter(getActivity(), mOrderDTOArrayList);
                        jobs_order_RL.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));
                        jobs_order_RL.setAdapter(postOrderAdapter);
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
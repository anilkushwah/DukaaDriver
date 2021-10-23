package com.dollop.dukaadriver.ui.total_earnfragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.activity.HomeActivity;
import com.dollop.dukaadriver.adapter.TotalEarnAdapeter;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.TotalEarnModel;
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
public class TotalEarningFragment extends Fragment {

    RecyclerView transation_list_earn;

    TotalEarnAdapeter totalEarnAdapeter;
    ArrayList<TotalEarnModel> totalEarnModels = new ArrayList<>();
    Spinner spinner;
    ImageView back_btn_total_earn, no_data_image;
    TextView total_errning_tv;
    View view;
    public TotalEarningFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_total_earning, container, false);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initialization(view);
        return view;
    }


    private void CourierEarning(String type) {
        final Dialog dialog = Utils.initProgressDialog(requireActivity());
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", ((HomeActivity) requireActivity()).sessionManager.getRegisterUser().getId());
        hm.put("earning_type", type);
        Utils.E("TotaCourierEarningFrag" + hm);


        Call<AllResponse> call = apiService.courier_earning(hm);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                dialog.dismiss();
                try {

                    AllResponse body = response.body();

                    if (body.getStatus() == 200) {

                        totalEarnModels = body.getEarning();

                        if (totalEarnModels == null) {
                            no_data_image.setVisibility(View.VISIBLE);
                        }

                        total_errning_tv.setText("KES" + body.getTotal_earning());
                        totalEarnAdapeter = new TotalEarnAdapeter(requireActivity().getApplicationContext(), totalEarnModels);
                        transation_list_earn.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext(), RecyclerView.VERTICAL, false));
                        transation_list_earn.setAdapter(totalEarnAdapeter);


                    } else {

                        total_errning_tv.setText("KES" + body.getTotal_earning());
                        // no_data_image.setVisibility(View.VISIBLE);
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

    private void Earning(String type) {


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", ((HomeActivity) requireActivity()).sessionManager.getRegisterUser().getId());
        hm.put("earning_type", type);
        Utils.E("TotalearningFrag" + hm);
        Utils.E("::Params:::" + hm);
        Call<AllResponse> call = apiService.driver_earning(hm);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {

                try {

                    AllResponse body = response.body();

                    if (body.getStatus() == 200) {

                        totalEarnModels = body.getEarning();

                        if (totalEarnModels == null) {
                            no_data_image.setVisibility(View.VISIBLE);
                        }
                        total_errning_tv.setText("KES" + body.getTotal_earning());
                        totalEarnAdapeter = new TotalEarnAdapeter(requireActivity().getApplicationContext(), totalEarnModels);
                        transation_list_earn.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext(), RecyclerView.VERTICAL, false));
                        transation_list_earn.setAdapter(totalEarnAdapeter);


                    } else {

                        total_errning_tv.setText("KES" + body.getTotal_earning());
                        // no_data_image.setVisibility(View.VISIBLE);
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

            }
        });
    }

    private void initialization(View view) {
        transation_list_earn = view.findViewById(R.id.transation_list_earn);
        back_btn_total_earn = view.findViewById(R.id.back_btn_total_earn);
        no_data_image = view.findViewById(R.id.no_data_image);
        spinner = view.findViewById(R.id.spinner);
        total_errning_tv = view.findViewById(R.id.total_errning_tv);

        if (((HomeActivity) requireActivity()).sessionManager.is_DRIVER()) {
            Earning("weekly");
        } else {

            CourierEarning("weekly");
        }


        back_btn_total_earn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), HomeActivity.class));
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (((HomeActivity) requireActivity()).sessionManager.is_DRIVER()) {
                    Earning(spinner.getSelectedItem().toString());
                } else {

                    CourierEarning(spinner.getSelectedItem().toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

}
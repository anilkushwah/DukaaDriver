package com.dollop.dukaadriver.ui.total_earnfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.adapter.TotalEarnAdapeter;
import com.dollop.dukaadriver.model.TotalEarnModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class TotalEarningFragment extends Fragment {

    RecyclerView transation_list_earn;

    TotalEarnAdapeter totalEarnAdapeter;
    ArrayList<TotalEarnModel> totalEarnModels = new ArrayList<>();


    public TotalEarningFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_total_earning, container, false);

        transation_list_earn = view.findViewById(R.id.transation_list_earn);

        for(int i=0;i<10;i++)
        {
            TotalEarnModel totalEarnModel = new TotalEarnModel();
            totalEarnModel.OrderId="#21457826";
            totalEarnModel.PaymentMehtod="cash";
            totalEarnModel.Location="Vishnupuri";
            totalEarnModel.Payment="250";
            totalEarnModel.Time="Today 12:0"+i;
            totalEarnModels.add(totalEarnModel);

        }

        totalEarnAdapeter = new TotalEarnAdapeter(getActivity().getApplicationContext(),totalEarnModels);
        transation_list_earn.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),RecyclerView.VERTICAL,false));
        transation_list_earn.setAdapter(totalEarnAdapeter);

        return view;
    }
}
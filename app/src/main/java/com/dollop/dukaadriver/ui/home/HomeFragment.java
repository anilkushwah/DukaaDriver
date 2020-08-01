package com.dollop.dukaadriver.ui.home;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.adapter.SavedAddressAdapter;
import com.dollop.dukaadriver.model.savedaddress_model;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener{
    List<savedaddress_model>savedaddress_models;
    Button btnselect_vehicleId;
    ImageView iv_close_vehicle;
    Dialog selectvehicle;
    RelativeLayout lv_applyBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        btnselect_vehicleId = root.findViewById(R.id.btnselect_vehicleId);

        btnselect_vehicleId.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        if (v == btnselect_vehicleId){
            selectVehicle();
        }else if (v == iv_close_vehicle){
            selectvehicle.dismiss();
        }else if (v == lv_applyBtn){
            selectvehicle.dismiss();
        }
    }

    public void selectVehicle(){
        selectvehicle = new Dialog(getContext());
        selectvehicle.setContentView(R.layout.dialog_select_vehcile);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(selectvehicle.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        selectvehicle.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        selectvehicle.getWindow().setAttributes(lp);
        selectvehicle.show();

        savedaddress_models = new ArrayList<>();

        RecyclerView rv_select_vehicle = selectvehicle.findViewById(R.id.rv_select_vehicle);
        iv_close_vehicle = selectvehicle.findViewById(R.id.iv_close_vehicle);
        lv_applyBtn = selectvehicle.findViewById(R.id.lv_applyBtn);

        savedaddress_model model = new savedaddress_model();
        model.setAddressname("Ford Pickup");
        model.setFulladdress("KCD 4562 TV");
        savedaddress_models.add(model);


        savedaddress_model model1 = new savedaddress_model();
        model1.setAddressname("Ford Pickup - 2");
        model1.setFulladdress("KCD 6248 KV");
        savedaddress_models.add(model1);

        savedaddress_model model2 = new savedaddress_model();
        model2.setAddressname("Ford Pickup - 3");
        model2.setFulladdress("KCD 9862 LG");
        savedaddress_models.add(model2);


        rv_select_vehicle.setLayoutManager(new LinearLayoutManager(getActivity()));

        SavedAddressAdapter savedAddressAdapter = new SavedAddressAdapter(getActivity(),savedaddress_models);
        rv_select_vehicle.setAdapter(savedAddressAdapter);

        lv_applyBtn.setOnClickListener(this);
        iv_close_vehicle.setOnClickListener(this);
    }

}

package com.dollop.dukaadriver.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dollop.dukaadriver.OrderDetailsActivity;
import com.dollop.dukaadriver.R;

public class DashboardFragment extends Fragment {
    LinearLayout btn_upload_proof;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        btn_upload_proof = root.findViewById(R.id.btn_upload_proof);


        btn_upload_proof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}

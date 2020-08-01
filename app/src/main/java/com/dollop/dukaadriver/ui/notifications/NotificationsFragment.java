package com.dollop.dukaadriver.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dollop.dukaadriver.AcceptOrderDriverActivity;
import com.dollop.dukaadriver.R;

public class NotificationsFragment extends Fragment implements View.OnClickListener{
    Button btn_accpet_order;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        btn_accpet_order = root.findViewById(R.id.btn_accpet_order);

        btn_accpet_order.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        if (v == btn_accpet_order){
            Intent intent = new Intent(getActivity(), AcceptOrderDriverActivity.class);
            startActivity(intent);
        }
    }
}

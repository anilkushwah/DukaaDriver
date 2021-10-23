package com.dollop.dukaadriver.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dollop.dukaadriver.activity.AcceptOrderDriverActivity;
import com.dollop.dukaadriver.R;

public class NotificationsFragment extends Fragment implements View.OnClickListener{
    Button btn_accpet_order;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        btn_accpet_order = root.findViewById(R.id.btn_accpet_order);

        btn_accpet_order.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        if (v == btn_accpet_order){
            Intent intent = new Intent(requireActivity(), AcceptOrderDriverActivity.class);
            startActivity(intent);
        }
    }
}

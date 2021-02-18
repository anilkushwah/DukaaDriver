package com.dollop.dukaadriver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.adapter.MyReviewAdapter;
import com.dollop.dukaadriver.adapter.NotificationAdapter;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.NotificationDTO;
import com.dollop.dukaadriver.model.StaticsDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    ImageView back_home_img, no_data_image;
    NotificationAdapter mNotificationAdapter;
    RecyclerView notification_RL;

    SessionManager sessionManager;

    ArrayList<NotificationDTO> mNotificationDTOArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_notification);
        sessionManager = new SessionManager(this);

        initialization();

        getStaticsMethod();

    }

    private void initialization() {


        back_home_img = findViewById(R.id.back_home_img);
        notification_RL = findViewById(R.id.notification_RL);
        no_data_image = findViewById(R.id.no_data_image);
        mNotificationDTOArrayList = new ArrayList<>();

        back_home_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void getStaticsMethod() {

        final Dialog dialog = Utils.initProgressDialog(this);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("customer_id", sessionManager.getRegisterUser().getId());
        hm.put("type", "Driver");


        Call<AllResponse> call = apiService.get_all_notificaton(hm);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                dialog.dismiss();
                try {

                    AllResponse body = response.body();

                    if (body.getStatus() == 200) {

                        mNotificationDTOArrayList = body.getNotification();

                        mNotificationAdapter = new NotificationAdapter(NotificationActivity.this, mNotificationDTOArrayList);
                        notification_RL.setLayoutManager(new LinearLayoutManager(NotificationActivity.this, RecyclerView.VERTICAL, false));

                        notification_RL.setAdapter(mNotificationAdapter);
                        mNotificationAdapter.notifyDataSetChanged();
                    } else {
                        no_data_image.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
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


}
package com.dollop.dukaadriver.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;


import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.SessionManager;

public class SeletedUserTypeActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout card_driver, card_courier;
    Button btn_selete_type;
    String selected_type = "";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleted_user_type);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        sessionManager = new SessionManager(this);

        card_driver = findViewById(R.id.card_driver);
        card_courier = findViewById(R.id.card_courier);
        btn_selete_type = findViewById(R.id.btn_selete_type);

        card_driver.setOnClickListener(this);
        card_courier.setOnClickListener(this);
        btn_selete_type.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == card_driver) {

            selected_type = "Individual Driver";
            sessionManager.set_DELIVERY_TYPE_DRIVER(true);

            card_driver.setBackground(getResources().getDrawable(R.drawable.seletec_user_type));
            card_courier.setBackground(getResources().getDrawable(R.drawable.seletec_user_type_gray));

        } else if (v == card_courier) {

            sessionManager.set_DELIVERY_TYPE_DRIVER(false);
            selected_type = "Courier Compnay";
            card_courier.setBackground(getResources().getDrawable(R.drawable.seletec_user_type));
            card_driver.setBackground(getResources().getDrawable(R.drawable.seletec_user_type_gray));


        } else if (v == btn_selete_type) {

            if (selected_type.equals("")) {
                ShowDialog("Select Delivery Type");
                //  Toast.makeText(SeletedUserTypeActivity.this, "Select Delivery Type", Toast.LENGTH_LONG).show();
            } else if (selected_type.equals("Courier Compnay")) {
                startActivity(new Intent(SeletedUserTypeActivity.this, SignupCourierCompanyActivity.class).putExtra("type", selected_type));
            } else if (selected_type.equals("Individual Driver")) {
                startActivity(new Intent(SeletedUserTypeActivity.this, RegistrationActivity.class).putExtra("type", selected_type));
            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SeletedUserTypeActivity.this, LoginActivity.class));
        finish();
        finishAffinity();
    }


    private void ShowDialog(String sms) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(sms)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Utils.I(this, AmountDoneNotificationActivity.class, null);
                        dialog.dismiss();
                    }
                }).show();

    }
}
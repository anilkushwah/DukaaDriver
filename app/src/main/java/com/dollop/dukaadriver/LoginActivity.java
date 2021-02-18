package com.dollop.dukaadriver;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.dollop.dukaadriver.activity.HomeActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_GoSignUp_Id;
    Button btn_login_send_otp;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        tv_GoSignUp_Id = findViewById(R.id.tv_GoSignUp_Id);
        btn_login_send_otp = findViewById(R.id.btn_login_send_otp);

        tv_GoSignUp_Id.setOnClickListener(this);
        btn_login_send_otp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tv_GoSignUp_Id){
            Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
            startActivity(intent);
        }else if (v == btn_login_send_otp){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

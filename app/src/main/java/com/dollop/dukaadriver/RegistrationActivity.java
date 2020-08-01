package com.dollop.dukaadriver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_reg_submit;
    ImageView create_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        btn_reg_submit = findViewById(R.id.btn_reg_submit);
        create_back = findViewById(R.id.create_back);


        btn_reg_submit.setOnClickListener(this);
        create_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_reg_submit){
            Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else if(v == create_back){
            onBackPressed();
        }
    }
}

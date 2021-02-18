package com.dollop.dukaadriver.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dollop.dukaadriver.R;

public class SuccessfullRegistrionActivity extends AppCompatActivity {
    Button register_ok_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfull_registrion);
        register_ok_btn = findViewById(R.id.register_ok_btn);
        register_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SuccessfullRegistrionActivity.this, LoginActivity.class));
                finish();
                finishAffinity();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SuccessfullRegistrionActivity.this, LoginActivity.class));
        finish();
        finishAffinity();
    }
}
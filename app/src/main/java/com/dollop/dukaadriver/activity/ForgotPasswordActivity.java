package com.dollop.dukaadriver.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.Const;
import com.dollop.dukaadriver.UtilityTools.UserAccount;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.retrofit.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    TextView btn_forgot_send;
    EditText et_forgot_mobile_number;
    TextView tv_forgot_mobile_req;
    LinearLayout ll_forgot_mobi;
    ImageView iv_back_arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        tv_forgot_mobile_req = findViewById(R.id.tv_forgot_mobile_req);
        btn_forgot_send = findViewById(R.id.btn_forgot_send);
        et_forgot_mobile_number = findViewById(R.id.et_forgot_mobile_number);
        ll_forgot_mobi = findViewById(R.id.ll_forgot_mobi);
        iv_back_arrow = findViewById(R.id.iv_back_arrow);

        btn_forgot_send.setOnClickListener(this);
        iv_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btn_forgot_send) {
            if (!UserAccount.isEmpty(et_forgot_mobile_number)) {
                tv_forgot_mobile_req.setText("please enter mobile number");
                et_forgot_mobile_number.requestFocus();
                tv_forgot_mobile_req.setVisibility(View.VISIBLE);
            } else if (!UserAccount.isPhoneNumberLength(et_forgot_mobile_number)) {
                tv_forgot_mobile_req.setText("please enter 9 digit mobile number");
                et_forgot_mobile_number.requestFocus();
                tv_forgot_mobile_req.setVisibility(View.VISIBLE);
            } else {
                tv_forgot_mobile_req.setVisibility(View.GONE);
                Forgot();
            }
        }
    }

    private void Forgot() {
        final Dialog dialog = Utils.initProgressDialog(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiClient.driver_forgot_password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();

                Utils.E("forgot_password:--" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.getInt("status") == 200) {

                        String otpdata = jsonObject.getString("data");


                        Intent intent = new Intent(ForgotPasswordActivity.this, ForgotMatchOtpActivity.class);
                        intent.putExtra("otp", otpdata.toString());
                        intent.putExtra("mobile", et_forgot_mobile_number.getText().toString());
                        startActivity(intent);
                        finish();
                    } else {
                        Utils.T(ForgotPasswordActivity.this, jsonObject.getString("message"));
                    }

                } catch (JSONException e) {
                    dialog.dismiss();

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                NetworkResponse networkResponse = error.networkResponse;

                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                        Utils.T(ForgotPasswordActivity.this, errorMessage + "please check Internet connection");
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");
                        Utils.E("Error Status" + status);
                        Utils.E("Error Message" + message);
                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> stringStringHashMap = new HashMap<>();
                stringStringHashMap.put("mobile", et_forgot_mobile_number.getText().toString());

                Utils.E("forgot_password:--" + stringStringHashMap);
                return stringStringHashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }
}
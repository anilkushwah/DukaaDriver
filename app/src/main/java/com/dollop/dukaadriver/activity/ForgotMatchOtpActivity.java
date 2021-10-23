package com.dollop.dukaadriver.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.chaos.view.PinView;
import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.Const;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.retrofit.ApiClient;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotMatchOtpActivity extends AppCompatActivity  implements View.OnClickListener{

    TextView btn_submit_otp;
    PinView firstPinView;
    String mobile,data;
    TextView tv_resend_Ot,tv_resend_otp_timer,tv_forgot_mobile_req;
    CountDownTimer countDownTimer;

    LinearLayout lv_cound_down_timerForgot_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_match_otp);


        tv_forgot_mobile_req = findViewById(R.id.tv_forgot_mobile_req);
        tv_resend_Ot = findViewById(R.id.tv_resend_Ot);
        btn_submit_otp = findViewById(R.id.btn_submit_otp);
        tv_resend_otp_timer = findViewById(R.id.tv_resend_otp_timer);
        lv_cound_down_timerForgot_Id = findViewById(R.id.lv_cound_down_timerForgot_Id);
        firstPinView = findViewById(R.id.firstPinView);
        btn_submit_otp.setOnClickListener(this);
        tv_resend_Ot.setOnClickListener(this);



        data = getIntent().getExtras().getString("otp","");
        mobile = getIntent().getExtras().getString("mobile","");
        firstPinView.setText(data);

        getTimer();
    }

    @Override
    public void onClick(View v) {
        if (v == btn_submit_otp){
            String otp = firstPinView.getText().toString();
            if (otp.isEmpty()){
                tv_forgot_mobile_req.setText("Please Enter Otp");
                tv_forgot_mobile_req.setVisibility(View.VISIBLE);
                firstPinView.requestFocus();
            }else if (otp.length()!=4){
                tv_forgot_mobile_req.setText("please enter 4 digit otp");
                tv_forgot_mobile_req.setVisibility(View.VISIBLE);
                firstPinView.requestFocus();
            }
            else {
                tv_forgot_mobile_req.setVisibility(View.GONE);

                matchOtp();
            }
        }

        else if(v == tv_resend_Ot){
            ResendOtp();
            lv_cound_down_timerForgot_Id.setVisibility(View.VISIBLE);
            tv_resend_Ot.setVisibility(View.GONE);
            getTimer();
        }


    }


    private void matchOtp() {
        {
            final Dialog dialog = Utils.initProgressDialog(this);
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiClient.match_otp, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();

                    Utils.E( "Otp:--" + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);



                        if (jsonObject.getInt("status")==200) {
                            JSONObject object = jsonObject.getJSONObject("data");
                            String retailer_id = object.getString("id");
                            Bundle bundle    = new Bundle();
                            bundle.putString("retailer_id", retailer_id);
                            Utils.I(ForgotMatchOtpActivity.this,UpdatePasswordActivity.class,bundle);
                            finish();
                        }else {
                            Utils.T(ForgotMatchOtpActivity.this,jsonObject.getString("message"));
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
                            Utils.T(ForgotMatchOtpActivity.this, errorMessage + "please check Internet connection");
                        } else if (error.getClass().equals(NoConnectionError.class)) {
                            errorMessage = "Failed to connect server";
                        }
                    } else {
                        String result = new String(networkResponse.data);
                        try {
                            JSONObject response = new JSONObject(result);
                            String status = response.getString("status");
                            String message = response.getString("message");
                            Utils.E( "Error Status"+ status);
                            Utils.E( "Error Message"+message);
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
                    stringStringHashMap.put("mobile",mobile);
                    stringStringHashMap.put("type","driver");
                    stringStringHashMap.put("otp",firstPinView.getText().toString());
                    Utils.E( "paramLogin:--" + stringStringHashMap);
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


    public void getTimer(){

        countDownTimer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_resend_otp_timer.setText((millisUntilFinished/30000)+":"+(millisUntilFinished%30000/1000)+" |");
                tv_resend_Ot.setVisibility(View.GONE);

            }
            @Override
            public void onFinish() {
                lv_cound_down_timerForgot_Id.setVisibility(View.GONE);
                tv_resend_Ot.setVisibility(View.VISIBLE);
            }
        }.start();
    }


    private void ResendOtp() {
        final Dialog dialog = Utils.initProgressDialog(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiClient.driver_resend_otp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Utils.E( "ResendOtp:--" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getInt("status")==200) {
                        String otpdata = jsonObject.getString("data");
                        firstPinView.setText(otpdata);
                        //  Utils.T_Long(ForgotMatchOtpActivity.this,otpdata);
                    }else {
                        Utils.T(ForgotMatchOtpActivity.this, jsonObject.getString("message"));

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
                        Utils.T(ForgotMatchOtpActivity.this, errorMessage + "please check Internet connection");
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");
                        Utils.E( "Error Status"+ status);
                        Utils.E( "Error Message"+ message);
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
                stringStringHashMap.put("mobile",mobile);

                Utils.E( "ResendOtp:--" + stringStringHashMap);
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
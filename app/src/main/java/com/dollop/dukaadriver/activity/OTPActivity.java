package com.dollop.dukaadriver.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.Const;
import com.dollop.dukaadriver.UtilityTools.NetworkUtil;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.Utility;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.CourierDTO;
import com.dollop.dukaadriver.model.OTPResponse;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener {

    PinView firstPinView;
    Button btn_submit_otp;
    LinearLayout lv_cound_down_timerForgot_Id;
    String mobile_number = "", otp_number = "", DriverType = "";
    CourierDTO mCourierDTO;
    TextView tv_resend_otp_timer, tv_resend_Ot;
    SessionManager sessionManager;
    boolean isLogin = false;
    boolean status;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_o_t_p);

        btn_submit_otp = findViewById(R.id.btn_submit_otp);
        firstPinView = findViewById(R.id.firstPinView);
        tv_resend_otp_timer = findViewById(R.id.tv_resend_otp_timer);
        tv_resend_Ot = findViewById(R.id.tv_resend_Ot);
        lv_cound_down_timerForgot_Id = findViewById(R.id.lv_cound_down_timerForgot_Id);

        sessionManager = new SessionManager(this);

        btn_submit_otp.setOnClickListener(this);
        tv_resend_Ot.setOnClickListener(this);


        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {

            otp_number = mBundle.getString("otp");
            mobile_number = mBundle.getString("mobile");
            DriverType = mBundle.getString("DriverType");

          //  firstPinView.setText(otp_number);
        }

        getTimer();
    }

    public void getTimer() {
        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_resend_otp_timer.setText((millisUntilFinished / 30000) + ":" + (millisUntilFinished % 30000 / 1000) + " |");
                tv_resend_Ot.setVisibility(View.GONE);

            }

            @Override
            public void onFinish() {
                lv_cound_down_timerForgot_Id.setVisibility(View.GONE);
                tv_resend_Ot.setVisibility(View.VISIBLE);
            }
        }.start();
    }


    @Override
    public void onClick(View v) {
        if (v == btn_submit_otp) {

            if (firstPinView.getText().equals("")) {
                //  Toast.makeText(OTPActivity.this, "Enter OTP", Toast.LENGTH_LONG).show();
                ShowDialog("Enter OTP");
            } else {


                if (NetworkUtil.isNetworkAvailable(OTPActivity.this)) {
                    otpVerifyMethod();
                } else {
                    Utility.netConnect(OTPActivity.this);

                }


            }
        } else if (v == tv_resend_Ot) {
            if (status) {
                lv_cound_down_timerForgot_Id.setVisibility(View.VISIBLE);
                tv_resend_Ot.setVisibility(View.GONE);
                firstPinView.getText().clear();
                getTimer();
                Resend();
            } else {
                Utils.T(OTPActivity.this, "No Internet Connection available. Please try again");
            }
        }

    }

    private void otpVerifyMethod() {
        final Dialog dialog = Utils.initProgressDialog(this);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("mobile", mobile_number);
        hm.put("otp", firstPinView.getText().toString());
        hm.put("type", "driver");

        Call<OTPResponse> call = apiService.match_otp(hm);
        call.enqueue(new Callback<OTPResponse>() {
            @Override
            public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
                dialog.dismiss();
                try {

                    OTPResponse body = response.body();

                    if (body.getStatus() == 200) {

                        mCourierDTO = body.getData();

                        //  sessionManager.setRegisterUser(mCourierDTO);
                        //  sessionManager.setLoginSession(true);

                        isLogin = true;


                        final Dialog dialog1 = new Dialog(OTPActivity.this, R.style.dialogstyle); // Context, this, etc.
                        dialog1.setContentView(R.layout.activity_successfull_registrion);
                        dialog1.setCanceledOnTouchOutside(true);
                        dialog1.setCancelable(true);
                        Button register_ok_btn = dialog1.findViewById(R.id.register_ok_btn);
                        register_ok_btn = dialog1.findViewById(R.id.register_ok_btn);
                        register_ok_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(OTPActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                finishAffinity();
                            }
                        });
                        dialog1.show();
                        // dialog_LL.setVisibility(View.VISIBLE);

                    } else {
                        ShowDialog(body.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<OTPResponse> call, Throwable t) {
                call.cancel();
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isLogin) {
            startActivity(new Intent(OTPActivity.this, LoginActivity.class));
            finish();
            finishAffinity();
        } else {
            startActivity(new Intent(OTPActivity.this, SeletedUserTypeActivity.class));
            finish();
            finishAffinity();
        }

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

    private void Resend() {
        final Dialog dialog = Utils.initProgressDialog(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiClient.driver_resend_otp, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();

                Log.e("distributerResend:", "Resend:--" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    if (jsonObject.getInt("status") == 200) {
                        String otpdata = jsonObject.getString("data");
                       // firstPinView.setText(otpdata);
                    } else {
                        Utils.T(OTPActivity.this, jsonObject.getString("message"));
                    }

                } catch (JSONException e) {
                    dialog.dismiss();

                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                NetworkResponse networkResponse = error.networkResponse;

                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                        Utils.T(OTPActivity.this, errorMessage + "please check Internet connection");
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");
                        Log.e("Error Status", status);
                        Log.e("Error Message", message);
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
                stringStringHashMap.put("mobile", mobile_number);
                Log.e("ResnedParameter::", "paramResned:--" + stringStringHashMap);
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
package com.dollop.dukaadriver.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

public class UpdatePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    TextView btn_update_pass;
    EditText edt_update_confirm_pass,edt_update_pass;
    String retailer_id;

    LinearLayout ll_update_pass,ll_update_confirm_pass;
    TextView tv_confim_password_match,tv_confirm_pass_text_red,tv_new_pass_text_red;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        btn_update_pass = findViewById(R.id.btn_update_pass);

        tv_new_pass_text_red = findViewById(R.id.tv_new_pass_text_red);
        tv_confim_password_match = findViewById(R.id.tv_confim_password_match);
        tv_confirm_pass_text_red = findViewById(R.id.tv_confirm_pass_text_red);

        edt_update_confirm_pass = findViewById(R.id.edt_update_confirm_pass);
        edt_update_pass = findViewById(R.id.edt_update_pass);
        ll_update_pass = findViewById(R.id.ll_update_pass);
        ll_update_confirm_pass = findViewById(R.id.ll_update_confirm_pass);

        btn_update_pass.setOnClickListener(this);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            retailer_id = (String) bundle.getString("retailer_id");
        }


    }
    @Override
    public void onClick(View v) {
        if(v == btn_update_pass){
            if (!UserAccount.isPasswordValid(edt_update_pass)){
                tv_new_pass_text_red.setVisibility(View.VISIBLE);
                tv_new_pass_text_red.setText("Please Enter min 6 digit Password");
                edt_update_pass.requestFocus();
            }
            else if (!UserAccount.isEmpty(edt_update_confirm_pass)){
                tv_confirm_pass_text_red.setVisibility(View.VISIBLE);
                tv_new_pass_text_red.setVisibility(View.GONE);
                tv_confirm_pass_text_red.setText("Please Enter Confirm Password");
                edt_update_confirm_pass.requestFocus();
            }
            else{

                if(! edt_update_confirm_pass.getText().toString().equals(edt_update_pass.getText().toString())){
                    tv_confirm_pass_text_red.setText("Password and Confirm password not match ");
                    tv_confirm_pass_text_red.setVisibility(View.VISIBLE);
                }
                else {
                    tv_confirm_pass_text_red.setVisibility(View.GONE);
                    Updatepass();

                }
            }
        }
    }

    private void Updatepass() {
        final Dialog dialog = Utils.initProgressDialog(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiClient.driver_change_password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Utils.E(   "change_password:--" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("status")==200) {
                        String otpdata = jsonObject.getString("data");
                        Utils.I_clear(UpdatePasswordActivity.this, LoginActivity.class,null);
                    }else {
                        Utils.T(UpdatePasswordActivity.this, jsonObject.getString("message"));

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
                        Utils.T(UpdatePasswordActivity.this, errorMessage + "please check Internet connection");
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");
                        Utils.E(  "Error Status"+ status);
                        Utils.E(  "Error Message"+ message);
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
                stringStringHashMap.put("driver_id",retailer_id);
                stringStringHashMap.put("new_password",edt_update_pass.getText().toString());
                Utils.E(  "change_password:--" + stringStringHashMap);
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
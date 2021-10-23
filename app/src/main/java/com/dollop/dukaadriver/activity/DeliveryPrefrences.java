package com.dollop.dukaadriver.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.dollop.dukaadriver.UtilityTools.MarshMallowPermission;
import com.dollop.dukaadriver.UtilityTools.NetworkUtil;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.Utility;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.UtilityTools.multipart.VolleyMultipartRequest;
import com.dollop.dukaadriver.UtilityTools.multipart.VolleySingleton;
import com.dollop.dukaadriver.firebase.Config;
import com.dollop.dukaadriver.model.ItemModel;
import com.dollop.dukaadriver.model.OrderDTO;
import com.dollop.dukaadriver.model.VehicalDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.simplify.ink.InkView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.dollop.dukaadriver.UtilityTools.multipart.AppHelper.getFileDataFromDrawable;


public class DeliveryPrefrences extends AppCompatActivity implements View.OnClickListener {
    public static final int GALLERY = 1;
    protected static final int CAMERA_REQUEST = 2;
    RelativeLayout signature_view_RL;
    ImageView final_image, delivery_image, iv_back_arrow;
    Button upload_proof_and_order;
    //   private SignaturePad mSignaturePad;
    SessionManager sessionManager;
    LinearLayout image_LL;
    RelativeLayout ll_payOnDelivery;
    Uri insuranceUri;
    Bitmap insuranceBitmap;
    boolean insuranceCamera;

    InkView ink;
    Bitmap bitmap = null;
    TextView item_tv, total_amount_tv, trash_sign, tv_distributor, tv_retailer_name, tv_requestPayment, tv_total_ProductPrice;
    OrderDTO mOrderDTO;
    ScrollView scrollViewId;

    ArrayList<VehicalDTO> mVehicalDTOS;
    RecyclerView rv_ItemList;
    ImageView refresh_iv;
    ArrayList<ItemModel> itemModels = new ArrayList<>();
    String transactionId = "";
    Activity activity = DeliveryPrefrences.this;
    TextView tv_orderId;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            if (!isFinishing()) {


                if (intent.getStringExtra("action").equals("payment_success")) {
                    tv_requestPayment.setEnabled(false);
                    tv_requestPayment.setText("Paid");
                    tv_requestPayment.setTextColor(Color.WHITE);
                    tv_requestPayment.setBackgroundResource(R.drawable.the_green_btn);

                }
            }
        }
    };
    private String callBack_url = ApiClient.BASE_URL + "callback_url?transaction_id=";


    private void GetOrderStatus(final String order_id) {
        final Dialog dialog = Utils.initProgressDialog(activity);
        RequestQueue queue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiClient.BASE_URL + "order_history", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Utils.E("GetOrderStatus:response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("status") == 200) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            mOrderDTO.transactionId = object.getString("transaction_id");
                            mOrderDTO.transactionMode = object.getString("transaction_mode");
                            mOrderDTO.transaction_status = object.getString("transaction_status");
                            /*if (object.getString("transaction_mode").equals("Pay On Delivery")) {
                                ll_payOnDelivery.setVisibility(View.VISIBLE);
                                tv_retailer_name.setText(mOrderDTO.getRetailerName());

                            } else {
                                ll_payOnDelivery.setVisibility(View.GONE);
                            }*/
                            if (object.getString("transaction_status").equals("Paid")) {
                                tv_requestPayment.setEnabled(false);
                                tv_requestPayment.setText("Paid");
                                tv_requestPayment.setTextColor(Color.WHITE);
                                tv_requestPayment.setBackgroundResource(R.drawable.the_green_btn);

                            } else if (object.getString("transaction_status").equals("Failed")) {
                                tv_requestPayment.setText("Not Paid");
                                tv_requestPayment.setTextColor(Color.WHITE);
                                tv_requestPayment.setBackgroundResource(R.drawable.red_btn);
                                tv_requestPayment.setEnabled(true);

                            } else {
                                tv_requestPayment.setEnabled(true);
                            }

                        }

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

                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                    Utils.T(activity, errorMessage + "please check Internet connection");
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
                        Utils.T(activity, errorMessage + "please check Internet connection");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("order_id", order_id);
                params.put("driver_id", sessionManager.getRegisterUser().getId());
                Utils.E("GetOrderStatus:params:" + params);
                return params;
            }


        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_delivery_prefrences);
        ll_payOnDelivery = findViewById(R.id.ll_payOnDelivery);
        tv_retailer_name = findViewById(R.id.tv_retailer_name);
        tv_requestPayment = findViewById(R.id.tv_requestPayment);
        tv_total_ProductPrice = findViewById(R.id.tv_total_ProductPrice);
        iv_back_arrow = findViewById(R.id.iv_back_arrow);
        refresh_iv = findViewById(R.id.refresh_iv);
        tv_orderId = findViewById(R.id.tv_orderId);

        mOrderDTO = (OrderDTO) getIntent().getSerializableExtra("model");
        if (!mOrderDTO.getTransactionId().equals("")) {
            refresh_iv.setVisibility(View.VISIBLE);
        } else {
            refresh_iv.setVisibility(View.GONE);
        }
        refresh_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetOrderStatus(mOrderDTO.getId());
            }
        });
        if (mOrderDTO.getTransactionMode().equals("Pay On Delivery")) {
            ll_payOnDelivery.setVisibility(View.VISIBLE);
            tv_retailer_name.setText(mOrderDTO.getRetailerName());
            if (mOrderDTO.transaction_status.equals("Paid")) {
                tv_requestPayment.setEnabled(false);
                tv_requestPayment.setText("Paid");
                tv_requestPayment.setTextColor(Color.WHITE);
                tv_requestPayment.setBackgroundResource(R.drawable.the_green_btn);

            } else if (mOrderDTO.transaction_status.equals("Failed")) {
                tv_requestPayment.setText("Not Paid");
                tv_requestPayment.setTextColor(Color.WHITE);
                tv_requestPayment.setBackgroundResource(R.drawable.red_btn);
                tv_requestPayment.setEnabled(true);

            } else {
                tv_requestPayment.setEnabled(true);
            }
        } else {
            ll_payOnDelivery.setVisibility(View.GONE);
        }

        tv_requestPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_requestPayment.getText().toString().equals("Request Payment")) {
                    RequestPaymentTask();
                }

            }
        });
        iv_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        sessionManager = new SessionManager(this);


        signature_view_RL = findViewById(R.id.signature_view_RL);

        rv_ItemList = findViewById(R.id.rv_ItemList);
        tv_distributor = findViewById(R.id.tv_distributor);

        upload_proof_and_order = findViewById(R.id.upload_proof_and_order);
        trash_sign = findViewById(R.id.trash_sign);
        ink = findViewById(R.id.ink);
        ink.setFlags(InkView.FLAG_INTERPOLATION | InkView.FLAG_RESPONSIVE_WIDTH);
        ink.setColor(getResources().getColor(android.R.color.black));
        ink.setMinStrokeWidth(1.5f);
        ink.setMaxStrokeWidth(6f);
        final_image = findViewById(R.id.final_image);
        image_LL = findViewById(R.id.image_LL);
        delivery_image = findViewById(R.id.delivery_image);
        item_tv = findViewById(R.id.item_tv);
        total_amount_tv = findViewById(R.id.total_amount_tv);

        upload_proof_and_order.setOnClickListener(this);
        trash_sign.setOnClickListener(this);
        image_LL.setOnClickListener(this);

        item_tv.setText("" + mOrderDTO.getItemCount() + " Items");
        total_amount_tv.setText("" + mOrderDTO.getTotalAmount());


        scrollViewId = findViewById(R.id.scrollViewId);
        ink.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disable the scroll view to intercept the touch event
                        scrollViewId.requestDisallowInterceptTouchEvent(true);
                        return false;
                    case MotionEvent.ACTION_UP:
                        // Allow scroll View to interceot the touch event
                        scrollViewId.requestDisallowInterceptTouchEvent(false);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        scrollViewId.requestDisallowInterceptTouchEvent(true);
                        return false;
                    default:
                        return true;
                }
            }
        });

        GetItemListTask();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
    }

    private void RequestPaymentTask() {
        final Dialog dialog = Utils.initProgressDialog(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiClient.payment_notification_for_retailer, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Utils.E("RequestPaymentTask:--" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("status") == 200) {
                        refresh_iv.setVisibility(View.VISIBLE);
                        transactionIdMethod();
                    } else {
                        Utils.T(DeliveryPrefrences.this, jsonObject.getString("message"));

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
                        Utils.T(DeliveryPrefrences.this, errorMessage + "please check Internet connection");
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
                stringStringHashMap.put("order_id", mOrderDTO.getId());
                Utils.E("RequestPaymentTask:--" + stringStringHashMap);
                return stringStringHashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    private void transactionIdMethod() {
        final Dialog dialog = Utils.initProgressDialog(activity);
        RequestQueue queue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiClient.generate_transaction_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Utils.E("transactionIdMethod:response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    transactionId = jsonObject.getString("transaction_id");
                    callBack_url = callBack_url + jsonObject.getString("transaction_id");
                    UpdateTransactionId(transactionId);

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
                        Utils.T(activity, errorMessage + "please check Internet connection");
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
                HashMap<String, String> params = new HashMap<>();

                Utils.E("transactionIdMethod:params:" + params);
                return params;

            }


        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    private void UpdateTransactionId(final String transactionId) {
        final Dialog dialog = Utils.initProgressDialog(activity);
        RequestQueue queue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiClient.update_transaction_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Utils.E("UpdateTransactionId:response:" + response);
                RequestPayment(mOrderDTO.getId());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.E("UpdateTransactionId:response:" + error);
                dialog.dismiss();

                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                        Utils.T(activity, errorMessage + "please check Internet connection");
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
                HashMap<String, String> params = new HashMap<>();
                params.put("order_id", mOrderDTO.getId());
                params.put("transaction_id", transactionId);

                Utils.E("UpdateTransactionId:params:" + params);
                return params;

            }


        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    private void RequestPayment(final String order_id) {
        final Dialog dialog = Utils.initProgressDialog(activity);
        RequestQueue queue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiClient.requestPayment, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Utils.E("RequestPayment:response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("status") == 200) {

                        //Utils.I(ConfirmOrderActivity.this, PaymentwaitingActivity.class, bundle);
                        //  placeOrderMethod();
                        //String gatewayResponse = jsonObject.getString("data");
                        JSONObject gatewayResponseJsonObject = new JSONObject(jsonObject.getString("data"));
                        if (gatewayResponseJsonObject.getBoolean("status")) {
                            Utils.T(activity, gatewayResponseJsonObject.getString("message"));
                        } else {
                            Utils.T(activity, jsonObject.getString("message"));
                        }
                    } else {
                        Utils.T(activity, jsonObject.getString("message"));
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
                        Utils.T(activity, errorMessage + "please check Internet connection");
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
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> stringStringHashMap = new HashMap<>();
                stringStringHashMap.put("json_str", getJsonParam(order_id).toString());

                return stringStringHashMap;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    private JSONObject getJsonParam(String order_id) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("amount", mOrderDTO.getPaidAmount());
            jsonObject.put("tel", "254" + mOrderDTO.getRetailerMobile());
            jsonObject.put("reference", "Retailer");
            jsonObject.put("vendor", mOrderDTO.getDistributorId());
            jsonObject.put("callback_url", callBack_url);
            jsonObject.put("order_id", "#000" + order_id);
            Utils.E("checkJsonObjectPut::" + jsonObject);

        } catch (Exception e) {
        }

        return jsonObject;
    }

    @Override
    public void onClick(View v) {
        if (v == upload_proof_and_order) {
            if (ink.getBitmap() != null) {
                bitmap = ink.getBitmap(getResources().getColor(R.color.white));
                //bitmap = ink.getBitmap();
                final_image.setImageBitmap(bitmap);
            }
            if (delivery_image.getDrawable() != null && !ink.isViewEmpty()) {
                if (NetworkUtil.isNetworkAvailable(DeliveryPrefrences.this)) {
                    deliverOrder();
                } else {
                    Utility.netConnect(DeliveryPrefrences.this);
                }
            } else {
                ShowDialog("Click the picture of the delivered order and take the sign of the retailer to make delivery confirm");
            }

            //
        } else if (v == trash_sign) {
            ink.clear();
        } else if (v == image_LL) {
            MarshMallowPermission marshMallowPermission = new MarshMallowPermission(DeliveryPrefrences.this);

            if (marshMallowPermission.checkPermissionForCamera()) {
                openGallery();
            } else {
                marshMallowPermission.requestPermissionForCamera();
            }

        }
    }

    public void deliverOrder() {
        final Dialog dialog = Utils.initProgressDialog(DeliveryPrefrences.this);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, ApiClient.BASE_URL + "delivered_order", new com.android.volley.Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                dialog.dismiss();
                String resultResponse = new String(response.data);

                Utils.E("resultResponse::" + resultResponse);

                try {
                    JSONObject result = new JSONObject(resultResponse);
                    Utils.E("SignUp::" + result);
                    int status = result.getInt("status");
                    String msg = result.getString("message");

                    if (status == 200) {

                        startActivity(new Intent(DeliveryPrefrences.this, HomeActivity.class));
                        finish();

                    } else {

                        ShowDialog(msg);

                    }
                } catch (JSONException e) {
                    Utils.E("UpdateUserProfileJSONException::" + e);
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;

                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                        Utils.T(getApplicationContext(), "Request timeout please check Internet connection");
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        //   response.getJSONArray(result);
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
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hm = new HashMap<>();

                hm.put("order_id", mOrderDTO.getId());
                hm.put("driver_id", sessionManager.getRegisterUser().getId());

                Utils.E("SIGNUPIMAGEINFO" + hm);
                return hm;

            }

            @Override
            protected Map<String, DataPart> getByteData() throws IOException {
                Map<String, DataPart> params = new HashMap<>();


                if (delivery_image.getDrawable() != null) {
                    params.put("proof_delivery", new DataPart(System.currentTimeMillis() + ".png",
                            getFileDataFromDrawable(getApplicationContext(),
                                    delivery_image.getDrawable()), "image/png"));
                }

                if (final_image.getDrawable() != null) {

                    params.put("signature_img", new DataPart(System.currentTimeMillis() + ".png",
                            getFileDataFromDrawable(getApplicationContext(),
                                    final_image.getDrawable()), "image/png"));
                }

                Utils.E("params>>" + params);
                return params;

            }


        };
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == GALLERY) {
            insuranceUri = data.getData();
            delivery_image.setVisibility(View.VISIBLE);
            delivery_image.setImageURI(insuranceUri);
            insuranceCamera = false;
        } else if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            insuranceBitmap = (Bitmap) data.getExtras().get("data");
            delivery_image.setImageBitmap(insuranceBitmap);
            delivery_image.setVisibility(View.VISIBLE);
            insuranceCamera = true;
        }
    }

    private void openGallery() {


        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DeliveryPrefrences.this);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_REQUEST);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(gallery, GALLERY);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.I_clear(DeliveryPrefrences.this, HomeActivity.class, null);
    }

    private void GetItemListTask() {

        StringRequest stringRequest = new StringRequest(1, ApiClient.order_history, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Utils.E("response::" + response);
                    if (object.getInt("status") == 200) {
                        JSONArray data = object.getJSONArray("data");
                        for (int a = 0; a < data.length(); a++) {
                            JSONObject jsonObject = data.getJSONObject(a);
                            tv_distributor.setText(jsonObject.getString("shop_name"));
                            tv_total_ProductPrice.setText(getString(R.string.currency_sign) + jsonObject.getString("paid_amount"));

                            for (int i = 0; i < jsonObject.getJSONArray("products").length(); i++) {
                                JSONObject Item = jsonObject.getJSONArray("products").getJSONObject(i);
                                ItemModel itemModel = new ItemModel();
                                itemModel.id = Item.getString("id");
                                itemModel.retailer_id = Item.getString("retailer_id");
                                itemModel.order_id = Item.getString("order_id");
                                itemModel.product_id = Item.getString("product_id");
                                itemModel.offer_id = Item.getString("offer_id");
                                itemModel.product_qty = Item.getString("product_qty");
                                itemModel.product_amount = Item.getString("product_amount");
                                itemModel.total_amount = Item.getString("total_amount");
                                itemModel.discount_amount = Item.getString("discount_amount");
                                itemModel.product_discounted_price = Item.getString("product_discounted_price");
                                itemModel.create_date = Item.getString("create_date");
                                itemModel.distributor_id = Item.getString("distributor_id");
                                itemModel.category_id = Item.getString("category_id");
                                itemModel.sub_category_id = Item.getString("sub_category_id");
                                itemModel.brand = Item.getString("brand");
                                itemModel.product_name = Item.getString("product_name");
                                itemModel.packing_id = Item.getString("packing_id");
                                itemModel.unit_per_packing_weight = Item.getString("unit_per_packing_weight");
                                itemModel.total_unit = Item.getString("total_unit");
                                itemModel.total_weight = Item.getString("total_weight");
                                itemModel.unit = Item.getString("unit");
                                itemModel.discount = Item.getString("discount");
                                itemModel.original_price = Item.getString("original_price");
                                itemModel.selling_price = Item.getString("selling_price");
                                itemModel.item_code = Item.getString("item_code");
                                itemModel.product_image = Item.getString("product_image");
                                itemModel.product_availability = Item.getString("product_availability");
                                itemModel.stock_quantity = Item.getString("stock_quantity");
                                itemModel.is_tax = Item.getString("is_tax");
                                itemModel.tax_id = Item.getString("tax_id");
                                itemModel.description = Item.getString("description");
                                itemModel.is_active = Item.getString("is_active");
                                itemModel.is_delete = Item.getString("is_delete");
                                itemModel.packing = Item.getString("packing");
                                itemModel.tax_name = Item.getString("tax_name");
                                itemModels.add(itemModel);
                                tv_orderId.setText("Order ID-: #000"+itemModel.order_id);

                            }

                        }
                        ItemAdapter itemAdapter = new ItemAdapter(DeliveryPrefrences.this, itemModels);
                        rv_ItemList.setLayoutManager(new LinearLayoutManager(DeliveryPrefrences.this));
                        rv_ItemList.setAdapter(itemAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("order_id", mOrderDTO.getId());
                hashMap.put("driver_id", sessionManager.getRegisterUser().getId());
                return hashMap;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

        ArrayList<ItemModel> itemModels;
        Context context;

        public ItemAdapter(Context context, ArrayList<ItemModel> itemModels) {
            this.context = context;
            this.itemModels = itemModels;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.order_item_list, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ItemModel itemModel = itemModels.get(position);
            int Position = position + 1;
            holder.item_count_tv.setText(Position + ") ");
            holder.item_name_id.setText(itemModel.product_name);
            int Amount = Integer.parseInt(itemModel.product_amount) * Integer.parseInt(itemModel.product_qty);
            holder.item_price_tv.setText(context.getString(R.string.currency_sign) + Amount);
            holder.item_description_tv.setText(itemModel.packing + " Quantity : " + itemModel.product_qty);
            if (position == itemModels.size() - 1) {
                holder.viewLine.setVisibility(View.GONE);
            } else {
                holder.viewLine.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public int getItemCount() {
            return itemModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView item_count_tv;
            TextView item_name_id;
            TextView item_description_tv;
            TextView item_price_tv;
            View viewLine;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                viewLine = itemView.findViewById(R.id.viewLine);
                item_count_tv = itemView.findViewById(R.id.item_count_tv);
                item_description_tv = itemView.findViewById(R.id.item_description_tv);
                item_name_id = itemView.findViewById(R.id.item_name_id);
                item_price_tv = itemView.findViewById(R.id.item_price_tv);
            }
        }
    }

}
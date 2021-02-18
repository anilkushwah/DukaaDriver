package com.dollop.dukaadriver.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.Const;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.OTPResponse;
import com.dollop.dukaadriver.model.StaticEarning;
import com.dollop.dukaadriver.model.StaticsDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaticsActivity extends AppCompatActivity {

    BarChart product_sale_graph;
    BarData barData;
    BarDataSet barDataSet;
    static ArrayList barentries;
    SessionManager sessionManager;
    StaticEarning mStaticEarning;

    ArrayList<String> xAxis;
    ArrayList<String> yAxis;
    //private Cartesian cartesian;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statics);
        sessionManager = new SessionManager(this);

        product_sale_graph = findViewById(R.id.product_sale_graph);
        xAxis = new ArrayList<>();
        yAxis = new ArrayList<>();
        setGraph();

      //  getDashBoardDetail("");
    }

    private void setGraph() {
        Toast.makeText(StaticsActivity.this, "setGraph", Toast.LENGTH_SHORT).show();
        barDataSet = new BarDataSet(barentries, "Dataset");
        barData = new BarData(barDataSet);
        product_sale_graph.setData(barData);

        product_sale_graph.setFitBars(true);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        barData.setBarWidth(0.5f);

    }

/*
    private void getEntries() {

        barentries = new ArrayList();
        barentries.add(new BarEntry(1f, 2));
        barentries.add(new BarEntry(2f, 4));
        barentries.add(new BarEntry(3f, 3));
        barentries.add(new BarEntry(5f, 4));
        barentries.add(new BarEntry(7f, 3));
    }
*/


/*
    private void getStaticsMethod(String status) {

        final Dialog dialog = Utils.initProgressDialog(this);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", sessionManager.getRegisterUser().getId());
        hm.put("earning_type", status);

        Call<StaticsDTO> call = apiService.statics_driver_earning(hm);
        call.enqueue(new Callback<StaticsDTO>() {
            @Override
            public void onResponse(Call<StaticsDTO> call, Response<StaticsDTO> response) {
                dialog.dismiss();
                try {

                    StaticsDTO body = response.body();

                    if (body.getStatus() == 200) {

                        mStaticEarning = body.getEarning();
                        String[] xAxis = mStaticEarning.getX().get(0).split(",");
                        String[] yAxis = mStaticEarning.getY().get(0).split(",");

                        for (String x : xAxis) {
                          //  barentries.add()
                        }

                    } else {

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<StaticsDTO> call, Throwable t) {
                call.cancel();
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }
*/
/*
    private void getDashBoardDetail(final String status) {
        final Dialog dialog = Utils.initProgressDialog(StaticsActivity.this);
        RequestQueue queue = Volley.newRequestQueue(StaticsActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL.statics_driver_earning, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Utils.E("getDashBoardDetail:response:" + response);
              */
/*  {"status":200,"message":"success","earning":{"x":["2020-10-20 15:54:12","2020-10-20 18:06:22","2020-10-20
                    19:43:55","2020-10-21 11:33:54","2020-10-21 11:49:25"],"y":["2200","1100","1200","26000","26000"]}}

*//*

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(StaticsActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                    if (jsonObject.getInt("status") == 200) {
                        JSONObject earning = jsonObject.getJSONObject("earning");
                        JSONArray jsonArrayX = earning.getJSONArray("x");
                        JSONArray jsonArrayY = earning.getJSONArray("y");
                        data.clear();
                        for (int i = 0; i < jsonArrayX.length(); i++) {
                            String[] onlyDate = jsonArrayX.getString(i).split("-");
                            String dates = onlyDate[onlyDate.length - 1];
                            data.add(new ValueDataEntry(dates, Double.parseDouble(jsonArrayY.getString(i))));
                        }


                        Column column = cartesian.column(data);

                        column.tooltip()
                                .titleFormat("{X}")
                                .position(Position.CENTER_BOTTOM)
                                .anchor(Anchor.CENTER_BOTTOM)
                                .offsetX(0d)
                                .offsetY(0d)
                                .format();


                        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
                        cartesian.interactivity().hoverMode(HoverMode.BY_X);


                        anyChartView.setChart(cartesian);
                    }
                } catch (Exception e) {
                    Utils.E("getDashBoardDetail:Exception:" + response);
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
                        Utils.T(StaticsActivity.this, errorMessage + "please check Internet connection");
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
                stringStringHashMap.put("driver_id",sessionManager.getRegisterUser().getId());
                stringStringHashMap.put("earning_type", status);
                Utils.E("getDashBoardDetail:Params:" + stringStringHashMap);

                return stringStringHashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }
*/


}
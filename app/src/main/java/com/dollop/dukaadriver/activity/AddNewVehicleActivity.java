package com.dollop.dukaadriver.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.MarshMallowPermission;
import com.dollop.dukaadriver.UtilityTools.NetworkUtil;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.UserAccount;
import com.dollop.dukaadriver.UtilityTools.Utility;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.UtilityTools.multipart.VolleyMultipartRequest;
import com.dollop.dukaadriver.UtilityTools.multipart.VolleySingleton;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.CourierDTO;

import com.dollop.dukaadriver.model.VehicalDTO;
import com.dollop.dukaadriver.model.VehicleTypeDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dollop.dukaadriver.UtilityTools.multipart.AppHelper.getFileDataFromDrawable;

public class AddNewVehicleActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView back_to_chngeV, vehicle_Insurance_img;
    Spinner vehicle_type_spiner;
    Button vehicle_submit_btn;
    SessionManager sessionManager;
    LinearLayout driver_licence_LL;
    EditText model_name_edt, et_ve_registeration_number;
    ArrayList<String> mStringArrayList;
    public static final int vehicle_insurance_GALLERY = 1;
    protected static final int vehicle_insurance_CAMERA_REQUEST = 2;
    TextView model_name_error_tv, vehicle_type_error_tv, registeration_number_error_tv;
    Bitmap insuranceBitmap;
    Uri insuranceUri;
    boolean insuranceCamera;
    ArrayList<VehicleTypeDTO> mVehicleTypeDTOS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_add_new_vehicle);
        sessionManager = new SessionManager(this);

        initialization();
        TextChangedListenerMethod();
        getVehicleType();

    }

    private void TextChangedListenerMethod() {
        model_name_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (UserAccount.isEmpty(model_name_edt)) {
                    model_name_error_tv.setVisibility(View.GONE);
                } else {
                    model_name_error_tv.setText("Enter Model Name");
                    model_name_error_tv.setVisibility(View.VISIBLE);

                }

            }
        });

/*
        edt_vehichle_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (UserAccount.isEmpty(edt_vehichle_number)) {
                    vehichle_number_error_tv.setVisibility(View.GONE);
                } else {
                    vehichle_number_error_tv.setText("Enter Vehichle Number");
                    vehichle_number_error_tv.setVisibility(View.VISIBLE);

                }

            }
        });
*/

        et_ve_registeration_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (UserAccount.isEmpty(et_ve_registeration_number)) {
                    registeration_number_error_tv.setVisibility(View.GONE);
                } else {
                    registeration_number_error_tv.setText("Enter Registeration Number");
                    registeration_number_error_tv.setVisibility(View.VISIBLE);

                }

            }
        });


    }

    private void initialization() {

        mVehicleTypeDTOS = new ArrayList<>();
        mStringArrayList = new ArrayList<>();

        vehicle_submit_btn = findViewById(R.id.vehicle_submit_btn);
        back_to_chngeV = findViewById(R.id.back_to_chngeV);
        model_name_edt = findViewById(R.id.model_name_edt);
      //  edt_vehichle_number = findViewById(R.id.edt_vehichle_number);
        et_ve_registeration_number = findViewById(R.id.et_ve_registeration_number);
        vehicle_type_spiner = findViewById(R.id.vehicle_type_spiner);
        driver_licence_LL = findViewById(R.id.driver_licence_LL);
        vehicle_Insurance_img = findViewById(R.id.vehicle_Insurance_img);
        model_name_error_tv = findViewById(R.id.model_name_error_tv);
        vehicle_type_error_tv = findViewById(R.id.vehicle_type_error_tv);
     //   vehichle_number_error_tv = findViewById(R.id.vehichle_number_error_tv);
        registeration_number_error_tv = findViewById(R.id.registeration_number_error_tv);


        back_to_chngeV.setOnClickListener(this);
        vehicle_submit_btn.setOnClickListener(this);
        driver_licence_LL.setOnClickListener(this);
        vehicle_Insurance_img.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (v == back_to_chngeV) {
            onBackPressed();
        } else if (v == vehicle_submit_btn) {

            if (model_name_edt.getText().toString().equals("")) {
                model_name_edt.setFocusable(true);
                model_name_error_tv.setText("Enter Model Name");
                model_name_error_tv.setVisibility(View.VISIBLE);
            } else if (vehicle_type_spiner.getSelectedItem().toString().equals("Select wheeler type")) {

                //  Toast.makeText(AddNewVehicleActivity.this, "Selete Wheeler Type", Toast.LENGTH_LONG).show();
                vehicle_type_error_tv.setText("Selete Wheeler Type");
                vehicle_type_error_tv.setVisibility(View.VISIBLE);

            } /*else if (edt_vehichle_number.getText().toString().equals("")) {
                edt_vehichle_number.setFocusable(true);
                vehichle_number_error_tv.setText("Enter Vehichle Number");
                vehichle_number_error_tv.setVisibility(View.VISIBLE);
            }*/ else if (et_ve_registeration_number.getText().toString().equals("")) {
                et_ve_registeration_number.setFocusable(true);
                registeration_number_error_tv.setText("Enter Registeration Number");
                registeration_number_error_tv.setVisibility(View.VISIBLE);
            } else if (vehicle_Insurance_img.getDrawable() == null) {
                //  Toast.makeText(AddNewVehicleActivity.this, "Select Vehicle Insurance", Toast.LENGTH_LONG).show();
                ShowDialog("Select Vehicle Insurance");
            } else {
                if (NetworkUtil.isNetworkAvailable(AddNewVehicleActivity.this)) {
                    addvehicleMethod();
                } else {
                    Utility.netConnect(AddNewVehicleActivity.this);

                }


            }

        } else if (v == driver_licence_LL) {


            MarshMallowPermission marshMallowPermission = new MarshMallowPermission(AddNewVehicleActivity.this);

            if (marshMallowPermission.checkPermissionForCamera()) {
                openGallery();
            } else {
                marshMallowPermission.requestPermissionForCamera();
            }


        } else if (v == vehicle_Insurance_img) {
            if (insuranceCamera) {
                ShowFullSizeImage(insuranceBitmap);
            } else {
                ShowFullSizeImage(insuranceUri);
            }
        }
    }


    private void openGallery() {


        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, vehicle_insurance_CAMERA_REQUEST);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(gallery, vehicle_insurance_GALLERY);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }


    private void addvehicleMethod() {
        final Dialog dialog = Utils.initProgressDialog(this);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, ApiClient.BASE_URL + "add_vehicle", new com.android.volley.Response.Listener<NetworkResponse>() {
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
                    int data = result.getInt("data");
                    if (status == 200) {

                        VehicalDTO mvehicleDTO = new VehicalDTO();
                        mvehicleDTO.setId("" + data);
                        mvehicleDTO.setModelName(model_name_edt.getText().toString());
                        //mvehicleDTO.setVehicleNum(edt_vehichle_number.getText().toString());
                        mvehicleDTO.setVehicleName(model_name_edt.getText().toString());
                        mvehicleDTO.setVehicleRegistrionNumber(et_ve_registeration_number.getText().toString());
                        mvehicleDTO.setVehicleType(vehicle_type_spiner.getSelectedItem().toString());

                        //  sessionManager.setVehicalData(mvehicleDTO);
                        startActivity(new Intent(AddNewVehicleActivity.this, ManageVehicleActivity.class));
                        finish();
                        finishAffinity();

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

                hm.put("driver_id", sessionManager.getRegisterUser().getId());
                hm.put("vehicle_name", model_name_edt.getText().toString());
                //hm.put("vehicle_num", edt_vehichle_number.getText().toString());
                hm.put("vehicle_type", vehicle_type_spiner.getSelectedItem().toString());
                hm.put("vehicle_registrion_number", et_ve_registeration_number.getText().toString());
                hm.put("model_name", model_name_edt.getText().toString());


                Utils.E("SIGNUPIMAGEINFO" + hm);
                return hm;

            }

            @Override
            protected Map<String, DataPart> getByteData() throws IOException {
                Map<String, DataPart> params = new HashMap<>();

                if (vehicle_Insurance_img.getDrawable() != null) {
                    params.put("vehicle_insurance", new DataPart(System.currentTimeMillis() + ".png",
                            getFileDataFromDrawable(getApplicationContext(),
                                    vehicle_Insurance_img.getDrawable()), "image/png"));


                }

                Utils.E("profile_img" + params);
                return params;

            }


        };
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);
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


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == vehicle_insurance_GALLERY) {
            insuranceUri = data.getData();
            vehicle_Insurance_img.setVisibility(View.VISIBLE);
            vehicle_Insurance_img.setImageURI(insuranceUri);
            insuranceCamera = false;
        } else if (resultCode == RESULT_OK && requestCode == vehicle_insurance_CAMERA_REQUEST) {
            insuranceBitmap = (Bitmap) data.getExtras().get("data");
            vehicle_Insurance_img.setImageBitmap(insuranceBitmap);
            vehicle_Insurance_img.setVisibility(View.VISIBLE);
            insuranceCamera = true;
        }
    }

    public void ShowFullSizeImage(Bitmap bitmap) {
        Utils.E("bitmap>>>" + bitmap);
        final Dialog dialog1 = new Dialog(AddNewVehicleActivity.this, R.style.dialogstyle); // Context, this, etc.
        dialog1.setContentView(R.layout.registrationsucces);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.setCancelable(true);
        ImageView full_size_image = dialog1.findViewById(R.id.full_size_image);
        full_size_image.setImageBitmap(bitmap);
        dialog1.show();
    }

    public void ShowFullSizeImage(final Uri mUri) {
        Utils.E("uri>>>" + mUri);
        final Dialog dialog1 = new Dialog(AddNewVehicleActivity.this, R.style.dialogstyle); // Context, this, etc.
        dialog1.setContentView(R.layout.registrationsucces);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.setCancelable(true);
        ImageView full_size_image = dialog1.findViewById(R.id.full_size_image);
        full_size_image.setImageURI(mUri);
        dialog1.show();
    }


    private void getVehicleType() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<AllResponse> call = apiService.get_vehicle_type();
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {

                try {

                    AllResponse body = response.body();

                    if (body.getStatus() == 200) {

                        mVehicleTypeDTOS = body.getVehicleType();

                        for (int i = 0; i < mVehicleTypeDTOS.size(); i++) {
                            mStringArrayList.add(mVehicleTypeDTOS.get(i).getType());
                        }
                        ArrayAdapter array = new ArrayAdapter(AddNewVehicleActivity.this, android.R.layout.simple_spinner_item, mStringArrayList);
                        array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicle_type_spiner.setAdapter(array);


                    } else {
                        ShowDialog(body.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                call.cancel();
                t.printStackTrace();

            }
        });
    }

}
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
import com.dollop.dukaadriver.model.DriverDTO;
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

public class CompanyUpdateDriverProfile extends AppCompatActivity {

    SessionManager sessionManager;
    DriverDTO mDriverDTO;

    EditText edt_driver_name, edt_email_id, edt_mobile_number, edt_national_Id;
    EditText edt_password;
    TextView password_error_tv, email_error_tv, mobile_error_tv, name_error_tv, nationID_error_tv, licence_error_tv;
    Button btn_reg_submit;
    LinearLayout company_image_LL, driver_licence_LL;
    ImageView user_profile, driver_licence_img, create_back;
    TextView password_tv;
    public static final int GALLERY = 5;
    protected static final int CAMERA_REQUEST = 6;

    public static final int Driver_Licence_GALLERY = 3;
    protected static final int Driver_Licence_CAMERA_REQUEST = 4;

    Bitmap profileBitmap, licenceBitmap;
    Uri profileUri, licenceUri;
    boolean licenCamera, updateTime = false;
    Spinner vehicle_type_spiner;

    ArrayList<VehicleTypeDTO> mVehicleTypeDTOS;
    ArrayList<String> mStringArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_company_update_driver_profile);

        mDriverDTO = (DriverDTO) getIntent().getSerializableExtra("model");
        sessionManager = new SessionManager(this);
        mVehicleTypeDTOS = new ArrayList<>();
        mStringArrayList = new ArrayList<>();

        initialization();
        TextChangedListenerMethod();
        getVehicleType();
        setData();

    }

    private void TextChangedListenerMethod() {
        driver_licence_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateTime) {
                    if (licenCamera) {
                        ShowFullSizeImage(licenceBitmap);
                    } else {
                        ShowFullSizeImage(licenceUri);
                    }
                } else {
                    final Dialog dialog1 = new Dialog(CompanyUpdateDriverProfile.this, R.style.dialogstyle); // Context, this, etc.
                    dialog1.setContentView(R.layout.registrationsucces);
                    dialog1.setCanceledOnTouchOutside(true);
                    dialog1.setCancelable(true);
                    ImageView full_size_image = dialog1.findViewById(R.id.full_size_image);

                    if (mDriverDTO.getLicenseImg() != null) {
                        //  Log.e("image licence>>", mDriverDTO.getLicenseImg());
                        driver_licence_img.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext())
                                .load(ApiClient.BASE_URL + mDriverDTO.getLicenseImg())
                                .into(full_size_image);
                        //   Log.e("image licence>>", ApiClient.BASE_URL_licence + mDriverDTO.getLicenseImg());
                    }
                    dialog1.show();
                }
            }
        });
        btn_reg_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!UserAccount.isEmpty(edt_driver_name)) {

                    UserAccount.EditTextPointer.setFocusable(true);
                    UserAccount.EditTextPointer.setSelection(0);
                    // edt_driver_name.setError("Enter Full Name");
                    name_error_tv.setText("Enter Full Name");
                    name_error_tv.setVisibility(View.VISIBLE);

                } else if (!UserAccount.isEmailValid(edt_email_id)) {

                    UserAccount.EditTextPointer.setFocusable(true);
                    UserAccount.EditTextPointer.setSelection(0);
                    email_error_tv.setVisibility(View.VISIBLE);
                    email_error_tv.setText("Enter Valid E-mail");

                } else if (!UserAccount.isPhoneNumberLength(edt_mobile_number)) {

                    UserAccount.EditTextPointer.setFocusable(true);
                    UserAccount.EditTextPointer.setSelection(0);
                    mobile_error_tv.setVisibility(View.VISIBLE);
                    mobile_error_tv.setText("Enter 9 digits number");


                } else if (!UserAccount.isEmpty(edt_national_Id)) {

                    UserAccount.EditTextPointer.setFocusable(true);
                    UserAccount.EditTextPointer.setSelection(0);
                    nationID_error_tv.setVisibility(View.VISIBLE);
                    nationID_error_tv.setText("Enter National ID");

                    //  } else if (!UserAccount.isEmpty(edt_Sacco_Name, edt_Sacco_number, et_reg_other_services)) {
                } else if (!UserAccount.isPasswordValid(edt_password)) {
                    UserAccount.EditTextPointer.setFocusable(true);
                    UserAccount.EditTextPointer.setSelection(0);
                    password_error_tv.setVisibility(View.VISIBLE);
                    password_error_tv.setText("Password must be at least 6 characters ");


                } else if (driver_licence_img.getDrawable() == null) {
                    licence_error_tv.setText("Select Driver licence");
                    licence_error_tv.setVisibility(View.VISIBLE);
                    //  Toast.makeText(RegistrationActivity.this, "Select Driver licence", Toast.LENGTH_LONG).show();
                } /*else if (!UserAccount.isEmpty(edt_Sacco_Name)) {

                    UserAccount.EditTextPointer.setFocusable(true);
                    UserAccount.EditTextPointer.setSelection(0);
                    sacco_name_error_tv.setText("Enter Sacco Name");
                    sacco_name_error_tv.setVisibility(View.VISIBLE);


                }*/ /*else if (!UserAccount.isEmpty(edt_Sacco_number)) {

                    UserAccount.EditTextPointer.setFocusable(true);
                    UserAccount.EditTextPointer.setSelection(0);
                    sacco_Membership_error_tv.setVisibility(View.VISIBLE);
                    sacco_Membership_error_tv.setText("Enter Sacco Membership Number");

                }*/ else {

                    if (NetworkUtil.isNetworkAvailable(CompanyUpdateDriverProfile.this)) {
                        updateDriver();
                    } else {
                        Utility.netConnect(CompanyUpdateDriverProfile.this);

                    }


                }


            }
        });

        company_image_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MarshMallowPermission marshMallowPermission = new MarshMallowPermission(CompanyUpdateDriverProfile.this);

                if (marshMallowPermission.checkPermissionForCamera()) {
                    profileMethod();
                } else {
                    marshMallowPermission.requestPermissionForCamera();
                }

            }
        });

        driver_licence_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        edt_email_id.addTextChangedListener(new TextWatcher() {
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
                if (UserAccount.isEmailValid(edt_email_id)) {
                    email_error_tv.setVisibility(View.GONE);
                } else {
                    email_error_tv.setVisibility(View.VISIBLE);
                    email_error_tv.setText("Enter Valid E-mail");
                }

            }
        });

        edt_mobile_number.addTextChangedListener(new TextWatcher() {
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
                if (UserAccount.isPhoneNumberLength(edt_mobile_number)) {
                    mobile_error_tv.setVisibility(View.GONE);
                } else {
                    mobile_error_tv.setVisibility(View.VISIBLE);
                    mobile_error_tv.setText("Enter 9 digits number");
                }

            }
        });
        edt_password.addTextChangedListener(new TextWatcher() {
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
                if (UserAccount.isPasswordValid(edt_password)) {
                    password_error_tv.setVisibility(View.GONE);
                } else {
                    password_error_tv.setVisibility(View.VISIBLE);
                    password_error_tv.setText("Password must be at least 6 characters ");
                }

            }
        });

        edt_driver_name.addTextChangedListener(new TextWatcher() {
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
                if (UserAccount.isEmpty(edt_driver_name)) {
                    name_error_tv.setVisibility(View.GONE);
                } else {
                    name_error_tv.setText("Enter Full Name");
                    name_error_tv.setVisibility(View.VISIBLE);

                }

            }
        });

        edt_national_Id.addTextChangedListener(new TextWatcher() {
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
                if (UserAccount.isEmpty(edt_national_Id)) {
                    nationID_error_tv.setVisibility(View.GONE);
                } else {
                    nationID_error_tv.setVisibility(View.VISIBLE);
                    nationID_error_tv.setText("Enter National ID");


                }

            }
        });


/*
        edt_Sacco_Name.addTextChangedListener(new TextWatcher() {
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
                if (UserAccount.isEmpty(edt_Sacco_Name)) {
                    sacco_name_error_tv.setVisibility(View.GONE);
                } else {
                    sacco_name_error_tv.setText("Enter Sacco Name");
                    sacco_name_error_tv.setVisibility(View.VISIBLE);


                }

            }
        });
*/


/*
        edt_Sacco_number.addTextChangedListener(new TextWatcher() {
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
                if (UserAccount.isEmpty(edt_Sacco_number)) {
                    sacco_Membership_error_tv.setVisibility(View.GONE);
                } else {
                    sacco_Membership_error_tv.setVisibility(View.VISIBLE);
                    sacco_Membership_error_tv.setText("Enter Sacco Membership Number");
                }

            }
        });
*/
    }

    private void initialization() {

        edt_driver_name = findViewById(R.id.edt_driver_name);
        edt_email_id = findViewById(R.id.edt_email_id);
        edt_mobile_number = findViewById(R.id.edt_mobile_number);
        edt_national_Id = findViewById(R.id.edt_national_Id);
        // edt_Sacco_Name = findViewById(R.id.edt_Sacco_Name);
        //  edt_Sacco_number = findViewById(R.id.edt_Sacco_number);
        //  et_reg_other_services = findViewById(R.id.et_reg_other_services);
        btn_reg_submit = findViewById(R.id.btn_reg_submit);
        user_profile = findViewById(R.id.user_profile);
        company_image_LL = findViewById(R.id.company_image_LL);
        driver_licence_img = findViewById(R.id.driver_licence_img);
        driver_licence_LL = findViewById(R.id.driver_licence_LL);
        edt_password = findViewById(R.id.edt_password);
        password_error_tv = findViewById(R.id.password_error_tv);
        email_error_tv = findViewById(R.id.email_error_tv);
        mobile_error_tv = findViewById(R.id.mobile_error_tv);
        name_error_tv = findViewById(R.id.name_error_tv);
        nationID_error_tv = findViewById(R.id.nationID_error_tv);
        licence_error_tv = findViewById(R.id.licence_error_tv);
        //sacco_name_error_tv = findViewById(R.id.sacco_name_error_tv);
        // sacco_Membership_error_tv = findViewById(R.id.sacco_Membership_error_tv);
        vehicle_type_spiner = findViewById(R.id.vehicle_type_spiner);

        create_back = findViewById(R.id.create_back);
        create_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void setData() {
        edt_email_id.setEnabled(false);
        edt_mobile_number.setEnabled(false);

        edt_driver_name.setText(mDriverDTO.getFullName());
        edt_email_id.setText(mDriverDTO.getEmail());
        edt_mobile_number.setText(mDriverDTO.getMobile());
        edt_national_Id.setText(mDriverDTO.getNationalId());
        // edt_Sacco_Name.setText(mDriverDTO.getSaccoName());
        //  edt_Sacco_number.setText(mDriverDTO.getSaccoMembershipNumber());
        //  et_reg_other_services.setText(mDriverDTO.getOtherServices());
        edt_password.setText(mDriverDTO.getPassword());

        if (mDriverDTO.getProfileImg() != null) {
            Glide.with(getApplicationContext())
                    .load(ApiClient.BASE_URL + mDriverDTO.getProfileImg())
                    .into(user_profile);

        }

        if (mDriverDTO.getLicenseImg() != null) {
            //  Log.e("image licence>>", mDriverDTO.getLicenseImg());
            driver_licence_img.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext())
                    .load(ApiClient.BASE_URL + mDriverDTO.getLicenseImg())
                    .into(driver_licence_img);
            //   Log.e("image licence>>", ApiClient.BASE_URL_licence + mDriverDTO.getLicenseImg());
        }
        Log.e("getVehicle_type>>", mDriverDTO.getVehicle_type());

        for (int j = 0; j < mStringArrayList.size(); j++) {

            if (mDriverDTO.getVehicle_type().equals(mStringArrayList.get(j))) {
                vehicle_type_spiner.setSelection(j);
                Log.e("mStringArrayList.get>>", mStringArrayList.get(j));
            }
        }

    }

    private void updateDriver() {
        final Dialog dialog = Utils.initProgressDialog(this);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, ApiClient.BASE_URL + "driver_register", new com.android.volley.Response.Listener<NetworkResponse>() {
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

                        onBackPressed();
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

                hm.put("driver_name", edt_driver_name.getText().toString());
                hm.put("email", edt_email_id.getText().toString());
                hm.put("mobile", edt_mobile_number.getText().toString());
                hm.put("national_id", edt_national_Id.getText().toString());
                hm.put("password", edt_password.getText().toString());
                //   hm.put("sacco_name", edt_Sacco_Name.getText().toString());
                hm.put("vehicle_type", vehicle_type_spiner.getSelectedItem().toString());
                hm.put("type", vehicle_type_spiner.getSelectedItem().toString());
                //   hm.put("sacco_membership_number", edt_Sacco_number.getText().toString());
                //  hm.put("other_services", et_reg_other_services.getText().toString());
                hm.put("id", mDriverDTO.getId());


                Utils.E("SIGNUPIMAGEINFO" + hm);
                return hm;

            }

            @Override
            protected Map<String, DataPart> getByteData() throws IOException {
                Map<String, DataPart> params = new HashMap<>();
                if (driver_licence_img.getDrawable() != null) {
                    params.put("license_img", new DataPart(System.currentTimeMillis() + ".png",
                            getFileDataFromDrawable(getApplicationContext(),
                                    driver_licence_img.getDrawable()), "image/png"));


                }
                if (user_profile.getDrawable() != null) {
                    params.put("profile_img", new DataPart(System.currentTimeMillis() + ".png",
                            getFileDataFromDrawable(getApplicationContext(),
                                    user_profile.getDrawable()), "image/png"));


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


    private void profileMethod() {


        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
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


    private void openGallery() {


        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Driver_Licence_CAMERA_REQUEST);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(gallery, Driver_Licence_GALLERY);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == GALLERY) {
            profileUri = data.getData();
            user_profile.setImageURI(profileUri);
        } else if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {

            profileBitmap = (Bitmap) data.getExtras().get("data");
            user_profile.setImageBitmap(profileBitmap);


        } else if (resultCode == RESULT_OK && requestCode == Driver_Licence_CAMERA_REQUEST) {
            updateTime = true;
            licenceBitmap = (Bitmap) data.getExtras().get("data");
            driver_licence_img.setImageBitmap(licenceBitmap);
            driver_licence_img.setVisibility(View.VISIBLE);
            licenCamera = true;

        } else if (resultCode == RESULT_OK && requestCode == Driver_Licence_GALLERY) {
            updateTime = true;
            licenceUri = data.getData();
            driver_licence_img.setVisibility(View.VISIBLE);
            driver_licence_img.setImageURI(licenceUri);
            licenCamera = false;
        }
    }


    public void ShowFullSizeImage(Bitmap bitmap) {
        Utils.E("bitmap>>>" + bitmap);
        final Dialog dialog1 = new Dialog(CompanyUpdateDriverProfile.this, R.style.dialogstyle); // Context, this, etc.
        dialog1.setContentView(R.layout.registrationsucces);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.setCancelable(true);
        ImageView full_size_image = dialog1.findViewById(R.id.full_size_image);
        full_size_image.setImageBitmap(bitmap);
        dialog1.show();
    }

    public void ShowFullSizeImage(final Uri mUri) {
        Utils.E("uri>>>" + mUri);
        final Dialog dialog1 = new Dialog(CompanyUpdateDriverProfile.this, R.style.dialogstyle); // Context, this, etc.
        dialog1.setContentView(R.layout.registrationsucces);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.setCancelable(true);
        ImageView full_size_image = dialog1.findViewById(R.id.full_size_image);
        full_size_image.setImageURI(mUri);

        dialog1.show();
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

    private void getVehicleType() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", sessionManager.getRegisterUser().getId());

        Call<AllResponse> call = apiService.get_vehicle_type(hm);
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


                        ArrayAdapter array = new ArrayAdapter(CompanyUpdateDriverProfile.this, android.R.layout.simple_spinner_item, mStringArrayList);
                        array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicle_type_spiner.setAdapter(array);

                        for (int j = 0; j < mStringArrayList.size(); j++) {

                            if (mDriverDTO.getVehicle_type().equals(mStringArrayList.get(j))) {
                                vehicle_type_spiner.setSelection(j);
                                Log.e("mStringArrayList.get>>", mStringArrayList.get(j));
                            }
                        }

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
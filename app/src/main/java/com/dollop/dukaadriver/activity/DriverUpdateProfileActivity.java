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
import com.dollop.dukaadriver.model.SignupDTO;
import com.dollop.dukaadriver.model.VehicalDTO;
import com.dollop.dukaadriver.model.VehicleTypeDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

public class DriverUpdateProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_reg_submit, btn_edit_profile;
    ImageView create_back, driver_licence_img, user_profile;
    Spinner vehicle_type_spiner;
    EditText edt_driver_name, edt_mobile_number, edt_national_Id, et_reg_modelName, et_reg_vehicle_regiter_num, et_reg_vehicle_number;
    EditText edt_email_id;
    String selete_type = "";

    LinearLayout driver_licence_LL;
    SessionManager sessionManager;
    TextView title_TV;
    LinearLayout company_image_LL, vehicle_type_LL;
    TextView name_error_tv, email_error_tv, mobile_error_tv, nationID_error_tv,
           vehicle_type_error_tv, title_vehicle_type_tv;
    public static final int Driver_Licence_GALLERY = 3;
    protected static final int Driver_Licence_CAMERA_REQUEST = 4;

    public static final int GALLERY = 5;
    protected static final int CAMERA_REQUEST = 6;

    Bitmap profileBitmap, licenceBitmap;
    Uri profileUri, licenceUri;
    boolean licenCamera, profileCamera, updateTime = false;

    ArrayList<VehicleTypeDTO> mVehicleTypeDTOS;
    ArrayList<VehicalDTO> mVehicleTypeDTOArrayList;
    ArrayList<String> mStringArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_driver_update_profile);

        Intent intent = getIntent();
        if (intent != null) {
            selete_type = intent.getStringExtra("type");
        }

        mVehicleTypeDTOS = new ArrayList<>();
        mStringArrayList = new ArrayList<>();
        mVehicleTypeDTOArrayList = new ArrayList<>();
        sessionManager = new SessionManager(this);

        initialization();
        TextChangedListenerMethod();
        getVehicleType();

        edt_email_id.setEnabled(false);
        edt_mobile_number.setEnabled(false);

        if (selete_type.equals("edit")) {
            edt_driver_name.setEnabled(true);
            edt_national_Id.setEnabled(true);
           // edt_Sacco_Name.setEnabled(true);
            //edt_Sacco_number.setEnabled(true);
          //  et_reg_other_services.setEnabled(true);
            vehicle_type_spiner.setEnabled(true);
            et_reg_modelName.setEnabled(true);
            et_reg_vehicle_regiter_num.setEnabled(true);
            et_reg_vehicle_number.setEnabled(true);
            driver_licence_LL.setEnabled(true);
            company_image_LL.setEnabled(true);
            driver_licence_img.setEnabled(true);
            title_TV.setText("Update Profile");
            btn_reg_submit.setVisibility(View.VISIBLE);
            btn_edit_profile.setVisibility(View.INVISIBLE);

        } else {
            edt_driver_name.setEnabled(false);
            edt_mobile_number.setEnabled(false);
            edt_national_Id.setEnabled(false);
         //   edt_Sacco_Name.setEnabled(false);
           // edt_Sacco_number.setEnabled(false);
          //  et_reg_other_services.setEnabled(false);
            vehicle_type_spiner.setEnabled(false);
            et_reg_modelName.setEnabled(false);
            et_reg_vehicle_regiter_num.setEnabled(false);
            company_image_LL.setEnabled(false);
            et_reg_vehicle_number.setEnabled(false);
            driver_licence_LL.setEnabled(false);
            driver_licence_img.setEnabled(false);

        }

        setData();


    }

    private void TextChangedListenerMethod() {

        btn_reg_submit.setOnClickListener(this);
        create_back.setOnClickListener(this);
        driver_licence_LL.setOnClickListener(this);
        btn_edit_profile.setOnClickListener(this);
        company_image_LL.setOnClickListener(this);
        driver_licence_img.setOnClickListener(this);

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
        btn_reg_submit = findViewById(R.id.btn_reg_submit);
        create_back = findViewById(R.id.create_back);
        company_image_LL = findViewById(R.id.company_image_LL);
        edt_driver_name = findViewById(R.id.edt_driver_name);
        edt_mobile_number = findViewById(R.id.edt_mobile_number);
        edt_national_Id = findViewById(R.id.edt_national_Id);
     //   edt_Sacco_Name = findViewById(R.id.edt_Sacco_Name);
       // edt_Sacco_number = findViewById(R.id.edt_Sacco_number);
      //  et_reg_other_services = findViewById(R.id.et_reg_other_services);
        vehicle_type_spiner = findViewById(R.id.vehicle_type_spiner);
        et_reg_modelName = findViewById(R.id.et_reg_modelName);
        et_reg_vehicle_regiter_num = findViewById(R.id.et_reg_vehicle_regiter_num);
        et_reg_vehicle_number = findViewById(R.id.et_reg_vehicle_number);
        driver_licence_LL = findViewById(R.id.driver_licence_LL);
        driver_licence_img = findViewById(R.id.driver_licence_img);
        edt_email_id = findViewById(R.id.edt_email_id);
        btn_edit_profile = findViewById(R.id.btn_edit_profile);
        title_TV = findViewById(R.id.title_TV);
        user_profile = findViewById(R.id.user_profile);
        name_error_tv = findViewById(R.id.name_error_tv);
        email_error_tv = findViewById(R.id.email_error_tv);
        mobile_error_tv = findViewById(R.id.mobile_error_tv);
        nationID_error_tv = findViewById(R.id.nationID_error_tv);
      //  sacco_name_error_tv = findViewById(R.id.sacco_name_error_tv);
       // sacco_Membership_error_tv = findViewById(R.id.sacco_Membership_error_tv);
        vehicle_type_error_tv = findViewById(R.id.vehicle_type_error_tv);
        title_vehicle_type_tv = findViewById(R.id.title_vehicle_type_tv);
        vehicle_type_LL = findViewById(R.id.vehicle_type_LL);

    }

    private void setData() {
        edt_driver_name.setText(sessionManager.getRegisterUser().getFullName());
        edt_mobile_number.setText(sessionManager.getRegisterUser().getMobile());
        edt_national_Id.setText(sessionManager.getRegisterUser().getNationalId());
       // edt_Sacco_Name.setText(sessionManager.getRegisterUser().getSaccoName());
      //  edt_Sacco_number.setText(sessionManager.getRegisterUser().getSaccoMembershipNumber());
      //  et_reg_other_services.setText(sessionManager.getRegisterUser().getOtherServices());
        edt_email_id.setText(sessionManager.getRegisterUser().getEmail());

        if (sessionManager.getRegisterUser() != null) {
            Glide.with(getApplicationContext())
                    .load(ApiClient.BASE_URL + sessionManager.getRegisterUser().getProfile_img())
                    .into(user_profile);

        }

        if (sessionManager.getRegisterUser().getLicenseImg() != null) {

            driver_licence_img.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext())
                    .load(ApiClient.BASE_URL + sessionManager.getRegisterUser().getLicenseImg())
                    .into(driver_licence_img);

        }
        for (int j = 0; j < mStringArrayList.size(); j++) {

            if (sessionManager.getRegisterUser().getVehicle_type().equals(mStringArrayList.get(j))) {
                vehicle_type_spiner.setSelection(j);
                Log.e("mStringArrayList.get>>", mStringArrayList.get(j));
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (v == btn_reg_submit) {

            if (!UserAccount.isEmpty(edt_driver_name)) {

                UserAccount.EditTextPointer.setFocusable(true);
                UserAccount.EditTextPointer.setSelection(0);

                name_error_tv.setText("Enter Full Name");
                name_error_tv.setVisibility(View.VISIBLE);

            } else if (!UserAccount.isEmpty(edt_email_id)) {

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

            } else if (driver_licence_img.getDrawable() == null) {
                // Toast.makeText(DriverUpdateProfileActivity.this, "Select Driver licence", Toast.LENGTH_LONG).show();
                ShowDialog("Select Driver licence");
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


                if (NetworkUtil.isNetworkAvailable(DriverUpdateProfileActivity.this)) {
                    registrationMethod();
                } else {
                    Utility.netConnect(DriverUpdateProfileActivity.this);

                }


            }


        } else if (v == create_back) {

            onBackPressed();

        } else if (v == driver_licence_LL) {

            MarshMallowPermission marshMallowPermission = new MarshMallowPermission(DriverUpdateProfileActivity.this);

            if (marshMallowPermission.checkPermissionForCamera()) {
                DrivingLicence();
            } else {
                marshMallowPermission.requestPermissionForCamera();
            }


        } else if (v == btn_edit_profile) {

            edt_driver_name.setEnabled(true);
            //  edt_mobile_number.setEnabled(true);
            edt_national_Id.setEnabled(true);
           // edt_Sacco_Name.setEnabled(true);
          //  edt_Sacco_number.setEnabled(true);
          //  et_reg_other_services.setEnabled(true);
            vehicle_type_spiner.setEnabled(true);
            et_reg_modelName.setEnabled(true);
            et_reg_vehicle_regiter_num.setEnabled(true);
            et_reg_vehicle_number.setEnabled(true);
            driver_licence_LL.setEnabled(true);

            driver_licence_img.setEnabled(true);

            company_image_LL.setEnabled(true);
            //    edt_email_id.setEnabled(true);
            title_TV.setText("Update Profile");
            btn_reg_submit.setVisibility(View.VISIBLE);
            btn_edit_profile.setVisibility(View.INVISIBLE);
        } else if (v == company_image_LL) {


            MarshMallowPermission marshMallowPermission = new MarshMallowPermission(DriverUpdateProfileActivity.this);

            if (marshMallowPermission.checkPermissionForCamera()) {
                openGallery();
            } else {
                marshMallowPermission.requestPermissionForCamera();
            }

        } else if (v == driver_licence_img) {
            if (updateTime) {
                if (licenCamera) {
                    ShowFullSizeImage(licenceBitmap);
                } else {
                    ShowFullSizeImage(licenceUri);
                }
            } else {
                final Dialog dialog1 = new Dialog(DriverUpdateProfileActivity.this, R.style.dialogstyle); // Context, this, etc.
                dialog1.setContentView(R.layout.registrationsucces);
                dialog1.setCanceledOnTouchOutside(true);
                dialog1.setCancelable(true);
                ImageView full_size_image = dialog1.findViewById(R.id.full_size_image);

                if (sessionManager.getRegisterUser().getLicenseImg() != null) {

                    driver_licence_img.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext())
                            .load(ApiClient.BASE_URL + sessionManager.getRegisterUser().getLicenseImg())
                            .into(full_size_image);

                }
                dialog1.show();
            }
        }


    }


    private void registrationMethod() {
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

                        JSONObject data = result.getJSONObject("data");
                        dialog.dismiss();
                        CourierDTO mCourierDTO = new CourierDTO();
                        mCourierDTO.setId(data.getString("id"));
                        mCourierDTO.setFullName(data.getString("full_name"));
                        mCourierDTO.setCompanyName(data.getString("company_name"));
                        mCourierDTO.setEmail(data.getString("email"));
                        mCourierDTO.setMobile(data.getString("mobile"));
                        mCourierDTO.setPassword(data.getString("password"));
                        mCourierDTO.setOtp(data.getString("otp"));
                        mCourierDTO.setIsActive(data.getString("is_active"));
                        mCourierDTO.setIsDelete(data.getString("is_delete"));
                        mCourierDTO.setCreateDate(data.getString("create_date"));
                        mCourierDTO.setType(data.getString("type"));
                        mCourierDTO.setDeliveryPartnerId(data.getString("delivery_partner_id"));
                        mCourierDTO.setSelfService(data.getString("self_service"));
                        mCourierDTO.setNationalId(data.getString("national_id"));
                        mCourierDTO.setNationalIdImg(data.getString("national_id_img"));
                        mCourierDTO.setLicenseImg(data.getString("license_img"));
                        //   mCourierDTO.setVehicleInsuranceImg(data.getString("vehicle_insurance_img"));
                        mCourierDTO.setSaccoName(data.getString("sacco_name"));
                        mCourierDTO.setSaccoMembershipNumber(data.getString("sacco_membership_number"));
                        mCourierDTO.setOtherServices(data.getString("other_services"));
                        mCourierDTO.setProfile_img(data.getString("profile_img"));
                        mCourierDTO.setVehicle_type(data.getString("vehicle_type"));

                        sessionManager.setRegisterUser(mCourierDTO);

                        //  edt_driver_name.setEnabled(false);
                        //   edt_mobile_number.setEnabled(false);
                        //  edt_national_Id.setEnabled(false);
                        //    edt_Sacco_Name.setEnabled(false);
                        //   edt_Sacco_number.setEnabled(false);
                        //   et_reg_other_services.setEnabled(false);
                        ////   vehicle_type_spiner.setEnabled(false);
                        //   et_reg_modelName.setEnabled(false);
                        //  et_reg_vehicle_regiter_num.setEnabled(false);
                        //  et_reg_vehicle_number.setEnabled(false);
                        //  driver_licence_LL.setEnabled(false);

                        //   driver_licence_img.setEnabled(false);

                        //   company_image_LL.setEnabled(false);
                        //  edt_email_id.setEnabled(false);
                        //   title_TV.setText("Personal Details");
                        //   btn_reg_submit.setVisibility(View.GONE);
                        //  btn_edit_profile.setVisibility(View.VISIBLE);
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
                hm.put("password", sessionManager.getRegisterUser().getPassword());
           //     hm.put("sacco_name", edt_Sacco_Name.getText().toString());
              //  hm.put("sacco_membership_number", edt_Sacco_number.getText().toString());
             //   hm.put("other_services", et_reg_other_services.getText().toString());
                hm.put("id", sessionManager.getRegisterUser().getId());
                hm.put("vehicle_type", vehicle_type_spiner.getSelectedItem().toString());
                hm.put("type", vehicle_type_spiner.getSelectedItem().toString());
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


    private void DrivingLicence() {


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

        if (resultCode == RESULT_OK && requestCode == Driver_Licence_CAMERA_REQUEST) {

            licenceBitmap = (Bitmap) data.getExtras().get("data");
            driver_licence_img.setImageBitmap(licenceBitmap);
            driver_licence_img.setVisibility(View.VISIBLE);
            licenCamera = true;
            updateTime = true;
        } else if (resultCode == RESULT_OK && requestCode == Driver_Licence_GALLERY) {

            licenceUri = data.getData();
            driver_licence_img.setVisibility(View.VISIBLE);
            driver_licence_img.setImageURI(licenceUri);
            licenCamera = false;
            updateTime = true;
        } else if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {

            profileBitmap = (Bitmap) data.getExtras().get("data");
            user_profile.setImageBitmap(profileBitmap);
            profileCamera = true;

        } else if (resultCode == RESULT_OK && requestCode == GALLERY) {

            profileUri = data.getData();
            user_profile.setImageURI(profileUri);
            profileCamera = false;
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

    public void ShowFullSizeImage(Bitmap bitmap) {
        Utils.E("bitmap>>>" + bitmap);
        final Dialog dialog1 = new Dialog(DriverUpdateProfileActivity.this, R.style.dialogstyle); // Context, this, etc.
        dialog1.setContentView(R.layout.registrationsucces);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.setCancelable(true);
        ImageView full_size_image = dialog1.findViewById(R.id.full_size_image);
        full_size_image.setImageBitmap(bitmap);
        dialog1.show();
    }

    public void ShowFullSizeImage(final Uri mUri) {
        Utils.E("uri>>>" + mUri);
        final Dialog dialog1 = new Dialog(DriverUpdateProfileActivity.this, R.style.dialogstyle); // Context, this, etc.
        dialog1.setContentView(R.layout.registrationsucces);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.setCancelable(true);
        ImageView full_size_image = dialog1.findViewById(R.id.full_size_image);
        full_size_image.setImageURI(mUri);

        dialog1.show();
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

                        ArrayAdapter array = new ArrayAdapter(DriverUpdateProfileActivity.this, android.R.layout.simple_spinner_item, mStringArrayList);
                        array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicle_type_spiner.setAdapter(array);

                        for (int j = 0; j < mStringArrayList.size(); j++) {

                            if (sessionManager.getRegisterUser().getVehicle_type().equals(mStringArrayList.get(j))) {
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


package com.dollop.dukaadriver.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.NetworkUtil;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.UserAccount;
import com.dollop.dukaadriver.UtilityTools.Utility;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.UtilityTools.multipart.VolleyMultipartRequest;
import com.dollop.dukaadriver.UtilityTools.multipart.VolleySingleton;
import com.dollop.dukaadriver.model.CourierDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.dollop.dukaadriver.UtilityTools.multipart.AppHelper.getFileDataFromDrawable;

public class UpdateCourierActivity extends AppCompatActivity {

    public static final int GALLERY = 0x1;
    protected static final int CAMERA_REQUEST = 0;
    EditText edt_full_name, edt_company_name, edt_email_id, edt_mobile_number;
    SessionManager sessionManager;
    Button btn_reg_submit, btn_edit_profile;
    TextView email_error_tv, mobile_error_tv, title_TV, full_name_error_tv, comapny_name_error_tv;
    ImageView create_back, user_profile;
    LinearLayout company_image_LL;
    CourierDTO mCourierDTO;
    String incomimg_type = "";
    String selete_type = "";
    public static String getDeviceId(Context context) {
        String m_deviceId;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            m_deviceId = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                m_deviceId = mTelephony.getDeviceId();
            } else {
                m_deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }
        Log.e("..", m_deviceId);
        return m_deviceId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_courier);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        sessionManager = new SessionManager(this);

        Intent intent = getIntent();
        if (intent != null) {
            incomimg_type = intent.getStringExtra("type");
        }

        initialization();
        TextChangedListenerMethod();
        setData();


    }

    private void TextChangedListenerMethod() {
        if (incomimg_type.equals("edit")) {
            edt_full_name.setEnabled(false);
            edt_company_name.setEnabled(false);
            //  edt_email_id.setEnabled(true);
               edt_mobile_number.setEnabled(false);
            company_image_LL.setEnabled(true);
            title_TV.setText("Update Profile");
            btn_reg_submit.setVisibility(View.VISIBLE);
            btn_edit_profile.setVisibility(View.INVISIBLE);

        } else {
            edt_full_name.setEnabled(false);
            edt_company_name.setEnabled(false);
            edt_email_id.setEnabled(false);
            edt_mobile_number.setEnabled(false);
            company_image_LL.setEnabled(false);
        }


        btn_reg_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (incomimg_type.equals("edit")) {
                    Utils.E("Edit:::::::::::::");
                    if (user_profile.getDrawable() == null) {
                        // Toast.makeText(DriverUpdateProfileActivity.this, "Select Driver licence", Toast.LENGTH_LONG).show();
                        ShowDialog("Select Profile Image");
                    }
               /* else  if (!UserAccount.isEmpty(edt_driver_name)) {

                    UserAccount.EditTextPointer.setFocusable(true);
                    UserAccount.EditTextPointer.setSelection(0);
                    name_error_tv.setText("Enter Full Name");
                    name_error_tv.setVisibility(View.VISIBLE);

                }
                else if (!UserAccount.isEmpty(edt_national_Id)) {

                    UserAccount.EditTextPointer.setFocusable(true);
                    UserAccount.EditTextPointer.setSelection(0);
                    nationID_error_tv.setVisibility(View.VISIBLE);
                    nationID_error_tv.setText("Enter National ID");

                }
                else if (!UserAccount.isPhoneNumberLength(edt_mobile_number)) {

                    UserAccount.EditTextPointer.setFocusable(true);
                    UserAccount.EditTextPointer.setSelection(0);
                    mobile_error_tv.setVisibility(View.VISIBLE);
                    Utils.E("isPhoneNumberLength:::::::::::::");
                    mobile_error_tv.setText("Enter 9 digits number");

                }*/
              /*  else if (!UserAccount.isEmpty(edt_email_id)) {

                    UserAccount.EditTextPointer.setFocusable(true);
                    UserAccount.EditTextPointer.setSelection(0);
                    email_error_tv.setVisibility(View.VISIBLE);
                    email_error_tv.setText("Enter Valid E-mail");

                }*/


            /*else if (!UserAccount.isEmpty(edt_Sacco_Name)) {

                UserAccount.EditTextPointer.setFocusable(true);
                UserAccount.EditTextPointer.setSelection(0);

                sacco_name_error_tv.setText("Enter Sacco Name");
                sacco_name_error_tv.setVisibility(View.VISIBLE);


            }*/
            /*else if (!UserAccount.isEmpty(edt_Sacco_number)) {

                UserAccount.EditTextPointer.setFocusable(true);
                UserAccount.EditTextPointer.setSelection(0);

                sacco_Membership_error_tv.setVisibility(View.VISIBLE);
                sacco_Membership_error_tv.setText("Enter Sacco Membership Number");


            }*/
                    else {

                        UpdateCourierProfileMethod();


                    }
                }
                else{
                    Utils.E("Register:::::::::::::");
                    if (!UserAccount.isEmpty(edt_full_name)) {
                        edt_full_name.setFocusable(true);
                        edt_full_name.setSelection(0);
                        full_name_error_tv.setText("Enter Full Name");
                        full_name_error_tv.setVisibility(View.VISIBLE);
                    }
                    else if (!UserAccount.isEmpty(edt_company_name)) {
                        edt_company_name.setFocusable(true);
                        edt_company_name.setSelection(0);
                        comapny_name_error_tv.setText("Enter Company Name");
                        comapny_name_error_tv.setVisibility(View.VISIBLE);
                    }
                    else if (!UserAccount.isEmailValid(edt_email_id)) {
                        edt_email_id.setFocusable(true);
                        edt_email_id.setSelection(0);
                        email_error_tv.setVisibility(View.VISIBLE);
                        email_error_tv.setText("Enter Valid E-mail");
                    }
                    else if (!UserAccount.isPhoneNumberLength(edt_mobile_number)) {
                        edt_mobile_number.setFocusable(true);
                        edt_mobile_number.setSelection(0);
                        mobile_error_tv.setVisibility(View.VISIBLE);
                        mobile_error_tv.setText("Enter 9 digits number");
                    }
                    else {
                        //updateCourier();
                        registrationMethod();
                    }

                }






            }
        });

        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edt_full_name.setEnabled(false);
                edt_company_name.setEnabled(false);
                 edt_email_id.setEnabled(true);
                //  edt_mobile_number.setEnabled(true);
                company_image_LL.setEnabled(true);
                title_TV.setText("Update Profile");
                btn_reg_submit.setVisibility(View.VISIBLE);
                btn_edit_profile.setVisibility(View.INVISIBLE);

            }
        });
        create_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        company_image_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
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

        edt_full_name.addTextChangedListener(new TextWatcher() {
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
                if (UserAccount.isEmpty(edt_full_name)) {
                    full_name_error_tv.setVisibility(View.GONE);
                } else {
                    full_name_error_tv.setText("Enter Full Name");
                    full_name_error_tv.setVisibility(View.VISIBLE);

                }

            }
        });


        edt_company_name.addTextChangedListener(new TextWatcher() {
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
                if (UserAccount.isEmpty(edt_company_name)) {
                    comapny_name_error_tv.setVisibility(View.GONE);
                } else {
                    comapny_name_error_tv.setText("Enter Company Name");
                    comapny_name_error_tv.setVisibility(View.VISIBLE);

                }

            }
        });


    }

    private void initialization() {
        edt_full_name = findViewById(R.id.edt_full_name);
        edt_company_name = findViewById(R.id.edt_company_name);
        edt_email_id = findViewById(R.id.edt_email_id);
        edt_mobile_number = findViewById(R.id.edt_mobile_number);
        btn_reg_submit = findViewById(R.id.btn_reg_submit);
        company_image_LL = findViewById(R.id.company_image_LL);
        email_error_tv = findViewById(R.id.email_error_tv);
        mobile_error_tv = findViewById(R.id.mobile_error_tv);
        btn_edit_profile = findViewById(R.id.btn_edit_profile);
        title_TV = findViewById(R.id.title_TV);
        create_back = findViewById(R.id.create_back);
        user_profile = findViewById(R.id.user_profile);
        full_name_error_tv = findViewById(R.id.full_name_error_tv);
        comapny_name_error_tv = findViewById(R.id.comapny_name_error_tv);

    }

    private void setData() {

        edt_full_name.setText(sessionManager.getRegisterUser().getFullName());
        edt_company_name.setText(sessionManager.getRegisterUser().getCompanyName());
        edt_email_id.setText(sessionManager.getRegisterUser().getEmail());
        edt_mobile_number.setText(sessionManager.getRegisterUser().getMobile());

        if (sessionManager.getRegisterUser().getProfile_img() != null) {
            Glide.with(getApplicationContext())
                    .load(ApiClient.BASE_URL + sessionManager.getRegisterUser().getProfile_img())
                    .into(user_profile);

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

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == GALLERY) {
            Uri imageUri = data.getData();

            user_profile.setImageURI(imageUri);
        } else if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            user_profile.setImageBitmap(bitmap);


        }
    }
    private void UpdateCourierProfileMethod() {
        final Dialog dialog = Utils.initProgressDialog(this);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, ApiClient.BASE_URL + "update_driver_profile", new com.android.volley.Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                dialog.dismiss();
                String resultResponse = new String(response.data);

                Utils.E("resultResponse::for:DriverUpdate:" + resultResponse);

                try {
                    JSONObject result = new JSONObject(resultResponse);
                    Utils.E("DriverUpdate::" + result);
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
                        mCourierDTO.setOtherServices(data.getString("other_services"));
                        mCourierDTO.setProfile_img(data.getString("profile_img"));
                        mCourierDTO.setVehicle_type(data.getString("vehicle_type"));

                        sessionManager.setRegisterUser(mCourierDTO);

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

                hm.put("driver_name", edt_full_name.getText().toString());
                hm.put("email", edt_email_id.getText().toString());
                hm.put("mobile", edt_mobile_number.getText().toString());
              //  hm.put("national_id", edt_national_Id.getText().toString());
                hm.put("password", sessionManager.getRegisterUser().getPassword());
                hm.put("id", sessionManager.getRegisterUser().getId());
                hm.put("password", sessionManager.getRegisterUser().getPassword());
                hm.put("courier_id", sessionManager.getRegisterUser().getId());
                hm.put("token", sessionManager.getTokenFCM());
                Utils.E("for:DriverUpdate:::::" + hm);
                return hm;

            }

            @Override
            protected Map<String, DataPart> getByteData() throws IOException {
                Map<String, DataPart> params = new HashMap<>();

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
    private void registrationMethod() {
        String m_deviceId = getDeviceId(UpdateCourierActivity.this);


        final Dialog dialog = Utils.initProgressDialog(this);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, ApiClient.BASE_URL + "courier_register", new com.android.volley.Response.Listener<NetworkResponse>() {
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
                    String otp = result.getString("data");
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
                        mCourierDTO.setProfile_img(data.getString("profile_img"));

                        sessionManager.setRegisterUser(mCourierDTO);

                        //  edt_full_name.setEnabled(false);
                        //  edt_company_name.setEnabled(false);
                        //    edt_email_id.setEnabled(false);
                        //   edt_mobile_number.setEnabled(false);
                        //   btn_reg_submit.setVisibility(View.GONE);
                        //   title_TV.setText("Personal Details");
                        //    btn_edit_profile.setVisibility(View.VISIBLE);

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

                hm.put("full_name", edt_full_name.getText().toString());
                hm.put("company_name", edt_company_name.getText().toString());
                hm.put("email", edt_email_id.getText().toString());
                hm.put("mobile", edt_mobile_number.getText().toString());
                hm.put("password", sessionManager.getRegisterUser().getPassword());
                hm.put("courier_id", sessionManager.getRegisterUser().getId());
                hm.put("device_id", m_deviceId);
                hm.put("token", sessionManager.getTokenFCM());
                Utils.E("SIGNUPIMAGEINFO" + hm);
                return hm;

            }
            @Override
            protected Map<String, DataPart> getByteData() throws IOException {
                Map<String, DataPart> params = new HashMap<>();

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


}
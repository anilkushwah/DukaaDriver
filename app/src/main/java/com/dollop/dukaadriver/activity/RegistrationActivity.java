package com.dollop.dukaadriver.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.dollop.dukaadriver.model.OTPResponse;
import com.dollop.dukaadriver.model.SignupDTO;
import com.dollop.dukaadriver.model.VehicleTypeDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_PHONE_STATE;
import static com.dollop.dukaadriver.UtilityTools.multipart.AppHelper.getFileDataFromDrawable;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_reg_submit, register_ok_btn;
    ImageView create_back, driver_licence_img, user_profile, indurance_img,show_password_img, show_confirm_password_img;

    EditText edt_driver_name, edt_mobile_number, edt_national_Id, et_reg_modelName, et_reg_vehicle_regiter_num, edt_confirm_password;
    EditText edt_email_id, edt_password;
    String selete_type = "";

    LinearLayout driver_licence_LL, driver_nsurance_LL, signup_LL;
    SessionManager sessionManager;
    TextView tv_GoSignInId, email_error_tv, mobile_error_tv, password_error_tv, or_tv, name_error_tv, nationID_error_tv;
    TextView licence_error_tv, confirm_password_error_tv;
    TextView model_name_error_tv, vehicle_type_error_tv, registeration_number_error_tv;

    public static final int GALLERY = 5;
    protected static final int CAMERA_REQUEST = 6;

    public static final int Driver_Licence_GALLERY = 3;
    protected static final int Driver_Licence_CAMERA_REQUEST = 4;

    public static final int Insurance_GALLERY = 1;
    protected static final int Insurance_CAMERA_REQUEST = 2;
    CourierDTO mCourierDTO;
    LinearLayout company_image_LL, vehicle_details_LL, vehicle_indurance_LL;

    Bitmap profileBitmap, licenceBitmap, insuranceBitmap;
    Uri profileUri, licenceUri, insuranceUri;
    boolean licenCamera, profileCamera, insuranceCamera;
    private boolean permissionGranted;
    ArrayList<String> mStringArrayList;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 100;
    private static final int REQUEST_READ_PHONE_STATE = 90;
    static String m_deviceId = "";
    Spinner vehicle_type_spiner;
    ArrayList<VehicleTypeDTO> mVehicleTypeDTOS;

    String passwordVisiblity = "hide";
    String con_passwordVisiblity = "hide";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_registration);


        Intent intent = getIntent();
        if (intent != null) {
            selete_type = intent.getStringExtra("type");
        }

        sessionManager = new SessionManager(this);
        mVehicleTypeDTOS = new ArrayList<>();
        mStringArrayList = new ArrayList<>();

        initialization();
        TextChangedListenerMethod();


        if (selete_type.equals("Courier")) {
            or_tv.setVisibility(View.GONE);
            signup_LL.setVisibility(View.GONE);
            vehicle_details_LL.setVisibility(View.GONE);
            vehicle_indurance_LL.setVisibility(View.GONE);
        }


        permissionGranted = checkAndRequestPermissions();

        getDeviceId(RegistrationActivity.this);

        getVehicleType();
    }


    public static String getDeviceId(Context context) {


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

    private void TextChangedListenerMethod() {

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

        edt_confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                if (edt_confirm_password.getText().toString().length() > edt_password.getText().toString().length()) {
                    confirm_password_error_tv.setText("Passwords does not match!");
                    confirm_password_error_tv.setVisibility(View.VISIBLE);
                } else if (!edt_confirm_password.getText().toString().equals(edt_password.getText().toString())) {
                    confirm_password_error_tv.setText("Passwords does not match!");
                    confirm_password_error_tv.setVisibility(View.VISIBLE);
                } else {
                    confirm_password_error_tv.setVisibility(View.GONE);
                }
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {


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


        et_reg_modelName.addTextChangedListener(new TextWatcher() {
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
                if (UserAccount.isEmpty(et_reg_modelName)) {
                    model_name_error_tv.setVisibility(View.GONE);
                } else {
                    model_name_error_tv.setText("Enter Model Name");
                    model_name_error_tv.setVisibility(View.VISIBLE);

                }

            }
        });

/*
        et_reg_vehicle_number.addTextChangedListener(new TextWatcher() {
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
                if (UserAccount.isEmpty(et_reg_vehicle_number)) {
                    vehichle_number_error_tv.setVisibility(View.GONE);
                } else {
                    vehichle_number_error_tv.setText("Enter Vehichle Number");
                    vehichle_number_error_tv.setVisibility(View.VISIBLE);

                }

            }
        });
*/

        et_reg_vehicle_regiter_num.addTextChangedListener(new TextWatcher() {
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
                if (UserAccount.isEmpty(et_reg_vehicle_regiter_num)) {
                    registeration_number_error_tv.setVisibility(View.GONE);
                } else {
                    registeration_number_error_tv.setText("Enter Registeration Number");
                    registeration_number_error_tv.setVisibility(View.VISIBLE);

                }

            }
        });


    }

    private void initialization() {

        btn_reg_submit = findViewById(R.id.btn_reg_submit);
        create_back = findViewById(R.id.create_back);
        edt_driver_name = findViewById(R.id.edt_driver_name);
        edt_mobile_number = findViewById(R.id.edt_mobile_number);
        edt_national_Id = findViewById(R.id.edt_national_Id);
        show_password_img = findViewById(R.id.show_password_img);
        show_confirm_password_img = findViewById(R.id.show_confirm_password_img);
        //edt_Sacco_Name = findViewById(R.id.edt_Sacco_Name);

        //   et_reg_other_services = findViewById(R.id.et_reg_other_services);
        name_error_tv = findViewById(R.id.name_error_tv);
        nationID_error_tv = findViewById(R.id.nationID_error_tv);

        et_reg_modelName = findViewById(R.id.et_reg_modelName);
        et_reg_vehicle_regiter_num = findViewById(R.id.et_reg_vehicle_regiter_num);

        email_error_tv = findViewById(R.id.email_error_tv);
        company_image_LL = findViewById(R.id.company_image_LL);
        driver_licence_LL = findViewById(R.id.driver_licence_LL);
        licence_error_tv = findViewById(R.id.licence_error_tv);

        // sacco_name_error_tv = findViewById(R.id.sacco_name_error_tv);
        driver_licence_img = findViewById(R.id.driver_licence_img);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);
        vehicle_details_LL = findViewById(R.id.vehicle_details_LL);

        tv_GoSignInId = findViewById(R.id.tv_GoSignInId);
        edt_email_id = findViewById(R.id.edt_email_id);
        edt_password = findViewById(R.id.edt_password);
        mobile_error_tv = findViewById(R.id.mobile_error_tv);
        password_error_tv = findViewById(R.id.password_error_tv);
        user_profile = findViewById(R.id.user_profile);
        or_tv = findViewById(R.id.or_tv);
        signup_LL = findViewById(R.id.signup_LL);
        confirm_password_error_tv = findViewById(R.id.confirm_password_error_tv);

        driver_nsurance_LL = findViewById(R.id.driver_nsurance_LL);
        indurance_img = findViewById(R.id.indurance_img);
        model_name_error_tv = findViewById(R.id.model_name_error_tv);
        vehicle_type_spiner = findViewById(R.id.vehicle_type_spiner);
        vehicle_type_error_tv = findViewById(R.id.vehicle_type_error_tv);

        registeration_number_error_tv = findViewById(R.id.registeration_number_error_tv);
        vehicle_indurance_LL = findViewById(R.id.vehicle_indurance_LL);

        btn_reg_submit.setOnClickListener(this);
        create_back.setOnClickListener(this);
        driver_licence_LL.setOnClickListener(this);

        tv_GoSignInId.setOnClickListener(this);
        company_image_LL.setOnClickListener(this);
        driver_licence_img.setOnClickListener(this);
        driver_nsurance_LL.setOnClickListener(this);
        show_password_img.setOnClickListener(this);
        show_confirm_password_img.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btn_reg_submit) {

            if (selete_type.equals("Courier")) {
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


                } else if (!UserAccount.isPasswordValid(edt_confirm_password)) {

                    UserAccount.EditTextPointer.setFocusable(true);
                    UserAccount.EditTextPointer.setSelection(0);
                    confirm_password_error_tv.setVisibility(View.VISIBLE);
                    confirm_password_error_tv.setText("Password must be at least 6 characters ");

                } else if (!edt_confirm_password.getText().toString().equals(edt_password.getText().toString())) {
                    confirm_password_error_tv.setText("Passwords does not match!");
                    confirm_password_error_tv.setVisibility(View.VISIBLE);
                } else if (driver_licence_img.getDrawable() == null) {
                    licence_error_tv.setText("Select Driver licence");
                    licence_error_tv.setVisibility(View.VISIBLE);
                    //  Toast.makeText(RegistrationActivity.this, "Select Driver licence", Toast.LENGTH_LONG).show();
                } /*else if (!UserAccount.isEmpty(edt_Sacco_Name)) {

                    UserAccount.EditTextPointer.setFocusable(true);
                    UserAccount.EditTextPointer.setSelection(0);
                    sacco_name_error_tv.setText("Enter Sacco Name");
                    sacco_name_error_tv.setVisibility(View.VISIBLE);


                } */ else if (vehicle_type_spiner.getSelectedItem().toString().equals("")) {

                    //  Toast.makeText(AddNewVehicleActivity.this, "Selete Wheeler Type", Toast.LENGTH_LONG).show();
                    vehicle_type_error_tv.setText("Selete Wheeler Type");
                    vehicle_type_error_tv.setVisibility(View.VISIBLE);

                } else {

                    if (NetworkUtil.isNetworkAvailable(RegistrationActivity.this)) {

                        registrationMethod(m_deviceId);
                    } else {
                        Utility.netConnect(RegistrationActivity.this);

                    }

                }
            }
            else {


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


                } else if (!UserAccount.isPasswordValid(edt_confirm_password)) {

                    UserAccount.EditTextPointer.setFocusable(true);
                    UserAccount.EditTextPointer.setSelection(0);
                    confirm_password_error_tv.setVisibility(View.VISIBLE);
                    confirm_password_error_tv.setText("Password must be at least 6 characters ");

                } else if (!edt_confirm_password.getText().toString().equals(edt_password.getText().toString())) {
                    confirm_password_error_tv.setText("Passwords does not match!");
                    confirm_password_error_tv.setVisibility(View.VISIBLE);
                } else if (driver_licence_img.getDrawable() == null) {
                    licence_error_tv.setText("Select Driver licence");
                    licence_error_tv.setVisibility(View.VISIBLE);
                    //  Toast.makeText(RegistrationActivity.this, "Select Driver licence", Toast.LENGTH_LONG).show();
                }/* else if (!UserAccount.isEmpty(edt_Sacco_Name)) {

                    UserAccount.EditTextPointer.setFocusable(true);
                    UserAccount.EditTextPointer.setSelection(0);
                    sacco_name_error_tv.setText("Enter Sacco Name");
                    sacco_name_error_tv.setVisibility(View.VISIBLE);


                } */ else if (!UserAccount.isEmpty(et_reg_modelName)) {

                    UserAccount.EditTextPointer.setFocusable(true);
                    UserAccount.EditTextPointer.setSelection(0);
                    model_name_error_tv.setVisibility(View.VISIBLE);
                    model_name_error_tv.setText("Enter Model Number");

                }
                /*else if (vehicle_type_spiner.getSelectedItem().toString().equals("Select wheeler type")) {

                    //  Toast.makeText(AddNewVehicleActivity.this, "Selete Wheeler Type", Toast.LENGTH_LONG).show();
                    vehicle_type_error_tv.setText("Selete Wheeler Type");
                    vehicle_type_error_tv.setVisibility(View.VISIBLE);

                }*/
                else if (et_reg_vehicle_regiter_num.getText().toString().equals("")) {
                    et_reg_vehicle_regiter_num.setFocusable(true);
                    registeration_number_error_tv.setText("Enter Registeration Number");
                    registeration_number_error_tv.setVisibility(View.VISIBLE);
                } else if (indurance_img.getDrawable() == null) {
                    //  Toast.makeText(AddNewVehicleActivity.this, "Select Vehicle Insurance", Toast.LENGTH_LONG).show();
                    //ShowDialog("Select Vehicle Insurance");
                    licence_error_tv.setText("Select Vehicle Insurance");
                    licence_error_tv.setVisibility(View.VISIBLE);
                } else {

                    if (NetworkUtil.isNetworkAvailable(RegistrationActivity.this)) {

                        registrationMethod(m_deviceId);
                    } else {
                        Utility.netConnect(RegistrationActivity.this);

                    }

                }

            }
        } else if (v == create_back) {

            if (selete_type.equals("Courier")) {
             //   startActivity(new Intent(RegistrationActivity.this, ManageDriverActivity.class));
              //  finishAffinity();
              //  finish();

                new android.app.AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                      //  .setTitle(" Confirmation !")
                        .setMessage("Are You Sure You Want To Lose Your Data?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                startActivity(new Intent(RegistrationActivity.this, ManageDriverActivity.class));
                                finish();
                                finishAffinity();

                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            } else {
               // startActivity(new Intent(RegistrationActivity.this, SeletedUserTypeActivity.class));
               // finish();
               // finishAffinity();

                new android.app.AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                       // .setTitle(" Confirmation !")
                        .setMessage("Are You Sure You Want To Lose Your Data?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                startActivity(new Intent(RegistrationActivity.this, SeletedUserTypeActivity.class));
                                finish();
                                finishAffinity();

                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }

        } else if (v == driver_licence_LL) {

            MarshMallowPermission marshMallowPermission = new MarshMallowPermission(RegistrationActivity.this);

            if (marshMallowPermission.checkPermissionForCamera()) {
                openGallery1();
            } else {
                marshMallowPermission.requestPermissionForCamera();
            }

        } else if (v == tv_GoSignInId) {



            new android.app.AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                  //  .setTitle(" Confirmation !")
                    .setMessage("Are You Sure You Want To Lose Your Data?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                            finish();
                            finishAffinity();

                        }

                    })
                    .setNegativeButton("No", null)
                    .show();


        } else if (v == company_image_LL) {

            MarshMallowPermission marshMallowPermission = new MarshMallowPermission(RegistrationActivity.this);

            if (marshMallowPermission.checkPermissionForCamera()) {
                profileMethod();
            } else {
                marshMallowPermission.requestPermissionForCamera();
            }

        } else if (v == driver_licence_img) {
            if (licenCamera) {
                ShowFullSizeImage(licenceBitmap);
            } else {
                ShowFullSizeImage(licenceUri);
            }
        } else if (v == driver_nsurance_LL) {
            MarshMallowPermission marshMallowPermission = new MarshMallowPermission(RegistrationActivity.this);

            if (marshMallowPermission.checkPermissionForCamera()) {

                insuranceMethod();
            } else {
                marshMallowPermission.requestPermissionForCamera();
            }

        }else if (v == show_password_img) {
                if (passwordVisiblity.equals("hide")) {
                    passwordVisiblity = "show";
                    show_password_img.setBackgroundResource(R.drawable.ic_baseline_visibility_off_24);
                    edt_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                } else if (passwordVisiblity.equals("show")) {
                    passwordVisiblity = "hide";
                    show_password_img.setBackgroundResource(R.drawable.ic_baseline_visibility_24);
                    edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            } else if (v == show_confirm_password_img) {
                if (con_passwordVisiblity.equals("hide")) {
                    con_passwordVisiblity = "show";
                    show_confirm_password_img.setBackgroundResource(R.drawable.ic_baseline_visibility_off_24);
                    edt_confirm_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                } else if (con_passwordVisiblity.equals("show")) {
                    con_passwordVisiblity = "hide";
                    show_confirm_password_img.setBackgroundResource(R.drawable.ic_baseline_visibility_24);
                    edt_confirm_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
        }
    }

    private void insuranceMethod() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Insurance_CAMERA_REQUEST);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(gallery, Insurance_GALLERY);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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


    private void openGallery1() {


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
            profileCamera = false;
        } else if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {

            profileBitmap = (Bitmap) data.getExtras().get("data");
            user_profile.setImageBitmap(profileBitmap);
            profileCamera = true;

        } else if (resultCode == RESULT_OK && requestCode == Driver_Licence_CAMERA_REQUEST) {
            licence_error_tv.setVisibility(View.GONE);
            licenceBitmap = (Bitmap) data.getExtras().get("data");
            driver_licence_img.setImageBitmap(licenceBitmap);
            driver_licence_img.setVisibility(View.VISIBLE);

            licenCamera = true;

        } else if (resultCode == RESULT_OK && requestCode == Driver_Licence_GALLERY) {
            licence_error_tv.setVisibility(View.GONE);
            licenCamera = false;

            licenceUri = data.getData();
            driver_licence_img.setVisibility(View.VISIBLE);
            driver_licence_img.setImageURI(licenceUri);


        } else if (resultCode == RESULT_OK && requestCode == Insurance_CAMERA_REQUEST) {
            licence_error_tv.setVisibility(View.GONE);
            insuranceBitmap = (Bitmap) data.getExtras().get("data");
            indurance_img.setImageBitmap(insuranceBitmap);
            indurance_img.setVisibility(View.VISIBLE);

            insuranceCamera = true;

        } else if (resultCode == RESULT_OK && requestCode == Insurance_GALLERY) {

            insuranceCamera = false;
            licence_error_tv.setVisibility(View.GONE);
            insuranceUri = data.getData();
            indurance_img.setVisibility(View.VISIBLE);
            indurance_img.setImageURI(insuranceUri);


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (selete_type.equals("Courier")) {
            startActivity(new Intent(RegistrationActivity.this, ManageDriverActivity.class));
            finishAffinity();
            finish();
        } else {
            startActivity(new Intent(RegistrationActivity.this, SeletedUserTypeActivity.class));
            finish();
            finishAffinity();
        }

    }


    private void registrationMethod(String m_deviceId) {
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
                    String otp = result.getString("data");
                    if (status == 200) {


                        if (selete_type.equals("Courier")) {
                            startActivity(new Intent(RegistrationActivity.this, ManageDriverActivity.class));
                            finishAffinity();
                            finish();
                        } else {
                            startActivity(new Intent(RegistrationActivity.this, OTPActivity.class)
                                    .putExtra("otp", "" + otp)
                                    .putExtra("mobile", edt_mobile_number.getText().toString())
                                    .putExtra("DriverType", "driver"));
                        }

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
                hm.put("device_id", m_deviceId);
                hm.put("token", sessionManager.getTokenFCM());
               hm.put("vehicle_type", vehicle_type_spiner.getSelectedItem().toString());
               hm.put("type", vehicle_type_spiner.getSelectedItem().toString());
                hm.put("type","Driver");

                if (selete_type.equals("Courier")) {
                    hm.put("delivery_partner_id", sessionManager.getRegisterUser().getId());
                } else {
                    hm.put("vehicle_name", et_reg_modelName.getText().toString());
                    hm.put("vehicle_registrion_number", et_reg_vehicle_regiter_num.getText().toString());
                    hm.put("model_name", et_reg_modelName.getText().toString());

                }


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

                if (indurance_img.getDrawable() != null) {
                    params.put("vehicle_insurance", new DataPart(System.currentTimeMillis() + ".png",
                            getFileDataFromDrawable(getApplicationContext(),
                                    indurance_img.getDrawable()), "image/png"));


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

    public void ShowFullSizeImage(Bitmap bitmap) {

        Utils.E("bitmap>>>" + bitmap);
        final Dialog dialog1 = new Dialog(RegistrationActivity.this, R.style.dialogstyle); // Context, this, etc.
        dialog1.setContentView(R.layout.registrationsucces);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.setCancelable(true);
        ImageView full_size_image = dialog1.findViewById(R.id.full_size_image);
        full_size_image.setImageBitmap(bitmap);

        dialog1.show();

    }

    public void ShowFullSizeImage(final Uri mUri) {

        Utils.E("uri>>>" + mUri);
        final Dialog dialog1 = new Dialog(RegistrationActivity.this, R.style.dialogstyle); // Context, this, etc.
        dialog1.setContentView(R.layout.registrationsucces);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.setCancelable(true);
        ImageView full_size_image = dialog1.findViewById(R.id.full_size_image);
        full_size_image.setImageURI(mUri);

        dialog1.show();
    }

    private boolean checkAndRequestPermissions() {

        int permissionReadStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCallPhone = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int locationcoarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int read_phone_stage = ContextCompat.checkSelfPermission(this, READ_PHONE_STATE);


        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionReadStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionCallPhone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (locationcoarse != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (read_phone_stage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(READ_PHONE_STATE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                break;

            default:
                break;
        }
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

                        ArrayAdapter array = new ArrayAdapter(RegistrationActivity.this, R.layout.spinner_item, mStringArrayList);
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

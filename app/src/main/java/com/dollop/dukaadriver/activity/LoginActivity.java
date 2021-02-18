package com.dollop.dukaadriver.activity;

import androidx.annotation.RequiresApi;
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
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.NetworkUtil;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.UserAccount;
import com.dollop.dukaadriver.UtilityTools.Utility;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.firebase.MyFirebaseMessagingService;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.CourierDTO;
import com.dollop.dukaadriver.model.OTPResponse;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_GoSignUp_Id, password_error_tv, email_error_tv;
    Button btn_login;
    EditText et_login_UserName, et_login_password;
    ImageView show_password_img;
    SessionManager sessionManager;
    private boolean permissionGranted;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 100;
    private static final int REQUEST_READ_PHONE_STATE = 90;

    CourierDTO mCourierDTO;
    static String m_deviceId = "";
    boolean isLogin = false;
    String passwordVisiblity = "hide";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(this);

        initialization();
        TextChangedListenerMethod();

        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }


        permissionGranted = checkAndRequestPermissions();
        MyFirebaseMessagingService.GenerateToken(LoginActivity.this);


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
        Log.e("m_deviceId>", m_deviceId);
        return m_deviceId;
    }

    private void TextChangedListenerMethod() {

        et_login_UserName.addTextChangedListener(new TextWatcher() {
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
                if (UserAccount.isPhoneNumberLength(et_login_UserName)) {
                    email_error_tv.setVisibility(View.GONE);
                } else {
                    email_error_tv.setVisibility(View.VISIBLE);
                    email_error_tv.setText("Enter Valid User Number");
                }

            }
        });


        et_login_password.addTextChangedListener(new TextWatcher() {
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
                if (UserAccount.isPasswordValid(et_login_password)) {
                    password_error_tv.setVisibility(View.GONE);
                } else {
                    password_error_tv.setVisibility(View.VISIBLE);
                    password_error_tv.setText("Password must be at least 6 characters ");
                }

            }
        });

    }

    private void initialization() {
        tv_GoSignUp_Id = findViewById(R.id.tv_GoSignUp_Id);
        et_login_UserName = findViewById(R.id.et_login_UserName);
        et_login_password = findViewById(R.id.et_login_password);
        btn_login = findViewById(R.id.btn_login);
        email_error_tv = findViewById(R.id.email_error_tv);
        password_error_tv = findViewById(R.id.password_error_tv);
        show_password_img = findViewById(R.id.show_password_img);

        tv_GoSignUp_Id.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        show_password_img.setOnClickListener(this);

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


    @Override
    public void onClick(View v) {
        if (v == tv_GoSignUp_Id) {
            Intent intent = new Intent(LoginActivity.this, SeletedUserTypeActivity.class);
            startActivity(intent);
        } else if (v == btn_login) {

            getDeviceId(LoginActivity.this);


            if (UserAccount.isEmpty(et_login_UserName)) {
                if (UserAccount.isPhoneNumberLength(et_login_UserName)) {

                    if (UserAccount.isPasswordValid(et_login_password)) {


                        if (NetworkUtil.isNetworkAvailable(LoginActivity.this)) {


                            loginMethod(m_deviceId);

                        } else {
                            Utility.netConnect(LoginActivity.this);

                        }


                    } else {
                        UserAccount.EditTextPointer.setFocusable(true);
                        password_error_tv.setVisibility(View.VISIBLE);
                        password_error_tv.setText("Password must be at least 6 characters");
                    }
                } else {
                    UserAccount.EditTextPointer.setFocusable(true);
                    email_error_tv.setVisibility(View.VISIBLE);
                    email_error_tv.setText("Enter Valid User Number");
                }

            } else {
                email_error_tv.setVisibility(View.VISIBLE);
                email_error_tv.setText("Enter phone number ");
            }


        }else if (v==show_password_img){
            if (passwordVisiblity.equals("hide")) {
                passwordVisiblity = "show";
                show_password_img.setBackgroundResource(R.drawable.ic_baseline_visibility_off_24);
                et_login_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

            } else if (passwordVisiblity.equals("show")) {
                passwordVisiblity = "hide";
                show_password_img.setBackgroundResource(R.drawable.ic_baseline_visibility_24);
                et_login_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }


    private void loginMethod(String m_deviceId) {

        final Dialog dialog = Utils.initProgressDialog(this);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("mobile", et_login_UserName.getText().toString());
        hm.put("password", et_login_password.getText().toString());
        hm.put("type", "driver");
        hm.put("device_id", m_deviceId);
        hm.put("token", sessionManager.getTokenFCM());

        Call<OTPResponse> call = apiService.login(hm);
        call.enqueue(new Callback<OTPResponse>() {
            @Override
            public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
                dialog.dismiss();
                try {

                    OTPResponse body = response.body();

                    if (body.getStatus() == 200) {

                        mCourierDTO = body.getData();
                        sessionManager.setLoginSession(true);
                        sessionManager.setRegisterUser(mCourierDTO);

                        if (sessionManager.getRegisterUser().getType().equals("Driver")) {
                            sessionManager.set_DELIVERY_TYPE_DRIVER(true);
                        } else {
                            sessionManager.set_DELIVERY_TYPE_DRIVER(false);
                        }

                        if (!sessionManager.getRegisterUser().getDeliveryPartnerId().equals("0")) {
                            sessionManager.COMPANY_DRIVER(true);
                        }

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        if (body.getMessage().equals("Your Account is Deactivated. Please Contact to Admin!")){
                            final Dialog dialog1 = new Dialog(LoginActivity.this, R.style.dialogstyle); // Context, this, etc.
                            dialog1.setContentView(R.layout.activity_successfull_registrion);
                            dialog1.setCanceledOnTouchOutside(true);
                            dialog1.setCancelable(true);
                            Button register_ok_btn = dialog1.findViewById(R.id.register_ok_btn);
                            register_ok_btn = dialog1.findViewById(R.id.register_ok_btn);
                            register_ok_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog1.dismiss();
                                }
                            });
                            dialog1.show();
                        }else {
                            ShowDialog(body.getMessage());
                        }

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


    private boolean checkAndRequestPermissions() {

        int permissionReadStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCallPhone = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int locationcoarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int read_phone_stage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);


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
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
           // Log.e("m_deviceId> 1", m_deviceId);
            return false;
        }
        return true;
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


}

package com.dollop.dukaadriver.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.NetworkUtil;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.Utility;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.CourierDTO;
import com.dollop.dukaadriver.model.VehicalDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    static String m_deviceId;
    EditText current_password_edt, new_password_edt, confirm_password_edt;
    TextView confirm_password_error_, new_password_error_, Current_password_error_;
    Button changePassword_btn;
    SessionManager sessionManager;
    ImageView old_password_img;
    ImageView new_pass_img;
    ImageView confirm_pass_img;
    ImageView back_home_img;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_change_password);

        sessionManager = new SessionManager(this);

        initialization();
        TextChangedListenerMethod();
        getDeviceId(ChangePasswordActivity.this);

    }

    private void TextChangedListenerMethod() {
        current_password_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (current_password_edt.getText().toString().length() < 5) {
                    Current_password_error_.setVisibility(View.VISIBLE);
                    Current_password_error_.setText("password must be at least 6 characters ");
                } else if (current_password_edt.getText().toString().length() > 5) {
                    Current_password_error_.setVisibility(View.GONE);
                }
            }
        });


        new_password_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (new_password_edt.getText().toString().length() < 5) {
                    new_password_error_.setVisibility(View.VISIBLE);
                    new_password_error_.setText("password must be at least 6 characters ");
                } else if (new_password_edt.getText().toString().length() > 5) {
                    new_password_error_.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirm_password_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                if (confirm_password_edt.getText().toString().length() > new_password_edt.getText().toString().length()) {
                    confirm_password_error_.setText("Passwords does not match!");
                    confirm_password_error_.setVisibility(View.VISIBLE);
                } else if (!confirm_password_edt.getText().toString().equals(new_password_edt.getText().toString())) {
                    confirm_password_error_.setText("Passwords does not match!");
                    confirm_password_error_.setVisibility(View.VISIBLE);
                } else {
                    confirm_password_error_.setVisibility(View.GONE);
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


        changePassword_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (current_password_edt.getText().toString().equals("")) {
                    Current_password_error_.setVisibility(View.VISIBLE);
                    Current_password_error_.setText("Enter Current Password");
                } else if (current_password_edt.getText().toString().length() < 6) {
                    Current_password_error_.setVisibility(View.VISIBLE);
                    Current_password_error_.setText("Password must be at least 6 characters ");
                } else if (new_password_edt.getText().toString().equals("")) {
                    new_password_error_.setVisibility(View.VISIBLE);
                    new_password_error_.setText("Enter New Password");
                } else if (new_password_edt.getText().toString().length() < 6) {
                    new_password_error_.setVisibility(View.VISIBLE);
                    new_password_error_.setText("Password must be at least 6 characters ");
                } else if (confirm_password_edt.getText().toString().equals("")) {
                    confirm_password_error_.setText("Enter confirm Password");
                    confirm_password_error_.setVisibility(View.VISIBLE);
                } else if (confirm_password_edt.getText().toString().length() < 6) {
                    confirm_password_error_.setText("Password must be at least 6 characters");
                    confirm_password_error_.setVisibility(View.VISIBLE);
                } else if (!confirm_password_edt.getText().toString().equals(new_password_edt.getText().toString())) {
                    confirm_password_error_.setText("Passwords does not match!");
                    confirm_password_error_.setVisibility(View.VISIBLE);
                } else {

                    if (NetworkUtil.isNetworkAvailable(ChangePasswordActivity.this)) {
                        updatePasswordMethod(current_password_edt.getText().toString(), new_password_edt.getText().toString());
                    } else {
                        Utility.netConnect(ChangePasswordActivity.this);

                    }


                }

            }
        });
    }
String oldpasswordVisiblity="hide";
String newpasswordVisiblity="hide";
String confirmpasswordVisiblity="hide";
    private void initialization() {

        current_password_edt = findViewById(R.id.current_password_edt);
        old_password_img = findViewById(R.id.old_password_img);
        confirm_pass_img = findViewById(R.id.confirm_pass_img);
        new_pass_img = findViewById(R.id.new_pass_img);
        new_password_edt = findViewById(R.id.new_password_edt);
        confirm_password_edt = findViewById(R.id.confirm_password_edt);

        confirm_password_error_ = findViewById(R.id.confirm_password_error_);
        new_password_error_ = findViewById(R.id.new_password_error_);
        Current_password_error_ = findViewById(R.id.Current_password_error_);
        changePassword_btn = findViewById(R.id.changePassword_btn);
        back_home_img = findViewById(R.id.back_home_img);

        back_home_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        old_password_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oldpasswordVisiblity.equals("hide")) {
                    oldpasswordVisiblity = "show";
                    old_password_img.setBackgroundResource(R.drawable.ic_baseline_visibility_off_24);
                    current_password_edt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                } else if (oldpasswordVisiblity.equals("show")) {
                    oldpasswordVisiblity = "hide";
                    old_password_img.setBackgroundResource(R.drawable.ic_baseline_visibility_24);
                    current_password_edt.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });
        new_pass_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newpasswordVisiblity.equals("hide")) {
                    newpasswordVisiblity = "show";
                    new_pass_img.setBackgroundResource(R.drawable.ic_baseline_visibility_off_24);
                    new_password_edt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                } else if (newpasswordVisiblity.equals("show")) {
                    newpasswordVisiblity = "hide";
                    new_pass_img.setBackgroundResource(R.drawable.ic_baseline_visibility_24);
                    new_password_edt.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });
        confirm_pass_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmpasswordVisiblity.equals("hide")) {
                    confirmpasswordVisiblity = "show";
                    confirm_pass_img.setBackgroundResource(R.drawable.ic_baseline_visibility_off_24);
                    confirm_password_edt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                } else if (confirmpasswordVisiblity.equals("show")) {
                    confirmpasswordVisiblity = "hide";
                    confirm_pass_img.setBackgroundResource(R.drawable.ic_baseline_visibility_24);
                    confirm_password_edt.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });
    }

    private void updatePasswordMethod(String oldPassword, String newPassword) {
        final Dialog dialog1 = Utils.initProgressDialog(this);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("old_password", oldPassword);
        hm.put("new_password", newPassword);
        hm.put("driver_id", sessionManager.getRegisterUser().getId());

        Call<AllResponse> call = apiService.driver_change_password(hm);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, retrofit2.Response<AllResponse> response) {
                dialog1.dismiss();
                try {

                    AllResponse body = response.body();

                    if (body.getStatus() == 200) {
                        //startActivity(new Intent(ChangePasswordActivity.this, HomeActivity.class));
                        // finishAffinity();
                        logoutMethod();

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
                dialog1.dismiss();

            }
        });


    }

    private void ShowDialog(String sms) {
        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setMessage(sms)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Utils.I(this, AmountDoneNotificationActivity.class, null);
                        dialog.dismiss();
                    }
                }).show();

    }


    private void logoutMethod() {


        if (sessionManager.is_DRIVER()) {

            getDriverStatus();

            startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
            finishAffinity();
            finish();

            CourierDTO user = new CourierDTO();
            VehicalDTO mVehicalDTO = new VehicalDTO();
            sessionManager.setLoginSession(false);
            sessionManager.setRegisterUser(user);
            sessionManager.DRIVER_ONLINE_STATUS(false);
            // ((HomeActivity) getActivity()).sessionManager.setVehicalData(mVehicalDTO);
            sessionManager.set_DELIVERY_TYPE_DRIVER(false);
            sessionManager.COMPANY_DRIVER(false);
            //((HomeActivity) getActivity()).sessionManager.setTokenFCM("");


        } else {
            logout();
        }
    }

    private void logout() {

        final Dialog dialog = Utils.initProgressDialog(ChangePasswordActivity.this);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("device_id", m_deviceId);
        hm.put("driver_id", sessionManager.getRegisterUser().getId());

        Call<AllResponse> call = apiService.logout(hm);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                dialog.dismiss();
                try {

                    AllResponse body = response.body();

                    if (body.getStatus() == 200) {

                        startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                        finishAffinity();
                        finish();

                        CourierDTO user = new CourierDTO();
                        VehicalDTO mVehicalDTO = new VehicalDTO();

                        sessionManager.setLoginSession(false);
                        sessionManager.setRegisterUser(user);
                        sessionManager.DRIVER_ONLINE_STATUS(false);
                        //  ((HomeActivity) getActivity()).sessionManager.setVehicalData(mVehicalDTO);
                        sessionManager.set_DELIVERY_TYPE_DRIVER(false);
                        sessionManager.COMPANY_DRIVER(false);

                        dialog.cancel();


                    } else {
                        ShowDialog("No Device Id Available ");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                call.cancel();
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }

    private void getDriverStatus() {


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", sessionManager.getRegisterUser().getId());
        hm.put("status", "Offline");

        Call<AllResponse> call = apiService.driver_status(hm);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {

                try {

                    AllResponse body = response.body();

                    if (body.getStatus() == 200) {


                    }

                } catch (
                        Exception e) {
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
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
import android.widget.LinearLayout;
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
import com.dollop.dukaadriver.firebase.MyFirebaseMessagingService;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.CourierDTO;
import com.dollop.dukaadriver.model.SignupDTO;
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

import static com.dollop.dukaadriver.UtilityTools.multipart.AppHelper.getFileDataFromDrawable;

public class SignupCourierCompanyActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edt_full_name, edt_company_name, edt_email_id, edt_mobile_number, edt_confirm_password;
    EditText edt_password;
    Button btn_reg_submit;
    TextView email_error_tv, mobile_error_tv, password_error_tv, tv_GoSignInId, confirm_password_error_tv, full_name_error_tv;
    TextView comapny_name_error_tv;
    LinearLayout company_image_LL;
    ImageView user_profile, create_back, show_password_img, show_confirm_password_img;
    public static final int GALLERY = 0x1;
    protected static final int CAMERA_REQUEST = 0;
    SessionManager sessionManager;
    private boolean permissionGranted;

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 100;
    private static final int REQUEST_READ_PHONE_STATE = 90;
    static String m_deviceId = "";
    String passwordVisiblity = "hide";
    String con_passwordVisiblity = "hide";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_signup_courier_company);

        sessionManager = new SessionManager(this);

        initialization();
        TextChangedListenerMethod();

        permissionGranted = checkAndRequestPermissions();
        MyFirebaseMessagingService.GenerateToken(SignupCourierCompanyActivity.this);

        // TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        // m_deviceId = TelephonyMgr.getDeviceId();
        getDeviceId(SignupCourierCompanyActivity.this);
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
        edt_password = findViewById(R.id.edt_password);
        btn_reg_submit = findViewById(R.id.btn_reg_submit);
        show_password_img = findViewById(R.id.show_password_img);
        show_confirm_password_img = findViewById(R.id.show_confirm_password_img);

        email_error_tv = findViewById(R.id.email_error_tv);
        mobile_error_tv = findViewById(R.id.mobile_error_tv);
        password_error_tv = findViewById(R.id.password_error_tv);
        company_image_LL = findViewById(R.id.company_image_LL);
        user_profile = findViewById(R.id.user_profile);
        create_back = findViewById(R.id.create_back);
        tv_GoSignInId = findViewById(R.id.tv_GoSignInId);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);
        confirm_password_error_tv = findViewById(R.id.confirm_password_error_tv);
        full_name_error_tv = findViewById(R.id.full_name_error_tv);

        comapny_name_error_tv = findViewById(R.id.comapny_name_error_tv);


        btn_reg_submit.setOnClickListener(this);
        company_image_LL.setOnClickListener(this);
        create_back.setOnClickListener(this);
        tv_GoSignInId.setOnClickListener(this);
        show_password_img.setOnClickListener(this);
        show_confirm_password_img.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == btn_reg_submit) {
            if (!UserAccount.isEmpty(edt_full_name)) {

                edt_full_name.setFocusable(true);
                edt_full_name.setSelection(0);
                full_name_error_tv.setVisibility(View.VISIBLE);
                full_name_error_tv.setText("Enter Full Name");

            } else if (!UserAccount.isEmpty(edt_company_name)) {
                edt_company_name.setFocusable(true);
                edt_company_name.setSelection(0);
                comapny_name_error_tv.setText("Enter Company Name");
                comapny_name_error_tv.setVisibility(View.VISIBLE);
            } else if (!UserAccount.isEmailValid(edt_email_id)) {
                edt_email_id.setFocusable(true);
                edt_email_id.setSelection(0);
                email_error_tv.setVisibility(View.VISIBLE);
                email_error_tv.setText("Enter Valid E-mail");
            } else if (!UserAccount.isPhoneNumberLength(edt_mobile_number)) {
                edt_mobile_number.setFocusable(true);
                edt_mobile_number.setSelection(0);
                mobile_error_tv.setVisibility(View.VISIBLE);
                mobile_error_tv.setText("Enter 9 digits number");
            } else if (!UserAccount.isPasswordValid(edt_password)) {
                edt_password.setFocusable(true);
                edt_password.setSelection(0);
                password_error_tv.setVisibility(View.VISIBLE);
                password_error_tv.setText("Password must be at least 6 characters ");
            } else if (!edt_confirm_password.getText().toString().equals(edt_password.getText().toString())) {
                confirm_password_error_tv.setText("Passwords does not match!");
                confirm_password_error_tv.setVisibility(View.VISIBLE);
            } else {

                if (NetworkUtil.isNetworkAvailable(SignupCourierCompanyActivity.this)) {
                    registrationMethod();
                } else {
                    Utility.netConnect(SignupCourierCompanyActivity.this);

                }


            }
        } else if (v == company_image_LL) {
            MarshMallowPermission marshMallowPermission = new MarshMallowPermission(SignupCourierCompanyActivity.this);

            if (marshMallowPermission.checkPermissionForCamera()) {
                openGallery();
            } else {
                marshMallowPermission.requestPermissionForCamera();
            }

        } else if (v == create_back) {
          //  startActivity(new Intent(SignupCourierCompanyActivity.this, SeletedUserTypeActivity.class));
            //finish();
            //finishAffinity();

            new android.app.AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                  //  .setTitle(" Confirmation !")
                    .setMessage("Are You Sure You Want To Lose Your Data?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            startActivity(new Intent(SignupCourierCompanyActivity.this, SeletedUserTypeActivity.class));
                            finish();
                            finishAffinity();

                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        } else if (v == tv_GoSignInId) {

            new android.app.AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                   // .setTitle(" Confirmation !")
                    .setMessage("Are You Sure You Want To Lose Your Data?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            startActivity(new Intent(SignupCourierCompanyActivity.this, LoginActivity.class));
                            finish();
                            finishAffinity();

                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
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

    private void RegistrationMethod() {

        final Dialog dialog = Utils.initProgressDialog(this);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("full_name", edt_full_name.getText().toString());
        hm.put("company_name", edt_company_name.getText().toString());
        hm.put("email", edt_email_id.getText().toString());
        hm.put("mobile", edt_mobile_number.getText().toString());
        hm.put("password", edt_password.getText().toString());


        Call<SignupDTO> call = apiService.courier_register(hm);
        call.enqueue(new Callback<SignupDTO>() {
            @Override
            public void onResponse(Call<SignupDTO> call, Response<SignupDTO> response) {
                dialog.dismiss();
                try {

                    SignupDTO body = response.body();

                    if (body.getStatus() == 200) {

                        Integer otp = body.getData();
                        Log.e("otp", "+" + otp);

                        startActivity(new Intent(SignupCourierCompanyActivity.this, OTPActivity.class)
                                .putExtra("otp", "" + otp)
                                .putExtra("mobile", edt_mobile_number.getText().toString())
                                .putExtra("DriverType", "courier"));
                        finish();
                        finishAffinity();
                    } else {
                        ShowDialog(body.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SignupDTO> call, Throwable t) {
                call.cancel();
                t.printStackTrace();
                dialog.dismiss();
            }
        });
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignupCourierCompanyActivity.this, SeletedUserTypeActivity.class));
        finish();
        finishAffinity();
    }


    private void registrationMethod() {


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

                        startActivity(new Intent(SignupCourierCompanyActivity.this, OTPActivity.class)
                                .putExtra("otp", "" + otp)
                                .putExtra("mobile", edt_mobile_number.getText().toString())
                                .putExtra("DriverType", "driver"));

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
                hm.put("password", edt_password.getText().toString());
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
}
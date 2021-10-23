package com.dollop.dukaadriver.ui.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.service.autofill.UserData;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.SavedData;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.activity.AcceptOrderDriverActivity;
import com.dollop.dukaadriver.activity.AcceptedOrderListActivity;
import com.dollop.dukaadriver.activity.ChangePasswordActivity;
import com.dollop.dukaadriver.activity.DriverUpdateProfileActivity;
import com.dollop.dukaadriver.activity.HomeActivity;
import com.dollop.dukaadriver.activity.LoginActivity;
import com.dollop.dukaadriver.activity.ManageDriverActivity;
import com.dollop.dukaadriver.activity.ManageVehicleActivity;
import com.dollop.dukaadriver.activity.MyRatingActivity;
import com.dollop.dukaadriver.activity.NotificationActivity;
import com.dollop.dukaadriver.activity.RegistrationActivity;
import com.dollop.dukaadriver.activity.SignupCourierCompanyActivity;
import com.dollop.dukaadriver.activity.StaticsActivity;
import com.dollop.dukaadriver.activity.UpdateCourierActivity;
import com.dollop.dukaadriver.adapter.HomeAdapter;
import com.dollop.dukaadriver.fragment.NewOrdersFragment;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.CourierDTO;
import com.dollop.dukaadriver.model.OTPResponse;
import com.dollop.dukaadriver.model.VehicalDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;
import com.dollop.dukaadriver.ui.home.HomeViewModel;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.TELEPHONY_SERVICE;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    TextView toolbar_logout, name_tv;
    Button btn_edit_profile;
    RelativeLayout order_history_RL, Statistic_RL, notification_RL, manage_driver_RL, personal_detail_RL,
            change_password_RL, my_review_RL;
    RelativeLayout accepted_RL, manage_vehicle_RL;
    LinearLayout manage_driver_LL;
    ImageView image;
    RelativeLayout compnay_rating_RL;
    View root;
    static String m_deviceId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);


        initialization(root);


        return root;
    }

    private void initialization(View root) {


        btn_edit_profile = root.findViewById(R.id.btn_edit_profile);
        order_history_RL = root.findViewById(R.id.order_history_RL);
        Statistic_RL = root.findViewById(R.id.Statistic_RL);
        notification_RL = root.findViewById(R.id.notification_RL);
        manage_driver_RL = root.findViewById(R.id.manage_driver_RL);
        personal_detail_RL = root.findViewById(R.id.personal_detail_RL);
        change_password_RL = root.findViewById(R.id.change_password_RL);
        toolbar_logout = root.findViewById(R.id.toolbar_logout);
        manage_driver_LL = root.findViewById(R.id.manage_driver_LL);
        my_review_RL = root.findViewById(R.id.my_review_RL);
        name_tv = root.findViewById(R.id.name_tv);
        image = root.findViewById(R.id.image);
        compnay_rating_RL = root.findViewById(R.id.compnay_rating_RL);
        accepted_RL = root.findViewById(R.id.accepted_RL);
        manage_vehicle_RL = root.findViewById(R.id.manage_vehicle_RL);


        if (((HomeActivity) requireActivity()).sessionManager.is_DRIVER()) {
            manage_driver_LL.setVisibility(View.GONE);
            compnay_rating_RL.setVisibility(View.GONE);
            accepted_RL.setVisibility(View.GONE);
        } else {
            my_review_RL.setVisibility(View.GONE);
            accepted_RL.setVisibility(View.VISIBLE);
        }

        btn_edit_profile.setOnClickListener(this);
        notification_RL.setOnClickListener(this);
        order_history_RL.setOnClickListener(this);
        personal_detail_RL.setOnClickListener(this);
        manage_driver_RL.setOnClickListener(this);
        toolbar_logout.setOnClickListener(this);
        my_review_RL.setOnClickListener(this);
        change_password_RL.setOnClickListener(this);
        compnay_rating_RL.setOnClickListener(this);
        Statistic_RL.setOnClickListener(this);
        accepted_RL.setOnClickListener(this);
        manage_vehicle_RL.setOnClickListener(this);

        name_tv.setText(((HomeActivity) requireActivity()).sessionManager.getRegisterUser().getFullName());

        if (((HomeActivity) requireActivity()).sessionManager.getRegisterUser().getProfile_img() != null) {
            Glide.with(requireActivity())
                    .load(ApiClient.BASE_URL + ((HomeActivity) requireActivity()).sessionManager.getRegisterUser().getProfile_img())
                    .error(R.drawable.individual_driver)
                    .into(image);

        }
        getDeviceId(requireActivity());

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


    @Override
    public void onClick(View v) {
        if (v == btn_edit_profile) {
            if (((HomeActivity) requireActivity()).sessionManager.is_DRIVER()) {
                startActivity(new Intent(getContext(), DriverUpdateProfileActivity.class).putExtra("type", "edit"));
            } else {
                startActivity(new Intent(getContext(), UpdateCourierActivity.class).putExtra("type", "edit"));
            }
        } else if (v == notification_RL) {
            startActivity(new Intent(requireActivity(), NotificationActivity.class));
        } else if (v == order_history_RL) {
            ((HomeActivity) requireActivity()).replaceFragmentWithoutBack(new NewOrdersFragment(), "");
        } else if (v == personal_detail_RL) {
            if (((HomeActivity) requireActivity()).sessionManager.is_DRIVER()) {
                startActivity(new Intent(getContext(), DriverUpdateProfileActivity.class).putExtra("type", "profile"));
            } else {
                startActivity(new Intent(getContext(), UpdateCourierActivity.class).putExtra("type", "profile"));
            }
        } else if (v == manage_driver_RL) {
            startActivity(new Intent(requireActivity(), ManageDriverActivity.class));
        } else if (v == toolbar_logout) {
            logoutMethod();
        } else if (v == my_review_RL) {
            startActivity(new Intent(requireActivity(), MyRatingActivity.class)
                    .putExtra("id", ((HomeActivity) requireActivity()).sessionManager.getRegisterUser().getId()).putExtra("type", "Driver"));
        } else if (v == change_password_RL) {
            startActivity(new Intent(requireActivity(), ChangePasswordActivity.class));
        } else if (v == compnay_rating_RL) {
            startActivity(new Intent(requireActivity(), MyRatingActivity.class)
                    .putExtra("id", ((HomeActivity) requireActivity()).sessionManager.getRegisterUser().getId()).putExtra("type", "Company"));
        } else if (v == Statistic_RL) {
            startActivity(new Intent(getContext(), StaticsActivity.class));
        } else if (v == accepted_RL) {
            startActivity(new Intent(requireActivity(), AcceptedOrderListActivity.class));
        } else if (v == manage_vehicle_RL) {
            startActivity(new Intent(requireActivity(), ManageVehicleActivity.class));
        }
    }

    private void logoutMethod() {

        AlertDialog.Builder alertdialog = new AlertDialog.Builder(requireActivity());
       // alertdialog.setTitle("Sure");
        alertdialog.setMessage("Are you sure you want to Logout ??");
        alertdialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if (((HomeActivity) requireActivity()).sessionManager.is_DRIVER()) {

                    getDriverStatus();

                    startActivity(new Intent(requireActivity(), LoginActivity.class));
                    requireActivity().finishAffinity();
                    requireActivity().finish();
                    CourierDTO user = new CourierDTO();
                    ((HomeActivity) requireActivity()).sessionManager.setLoginSession(false);
                    ((HomeActivity) requireActivity()).sessionManager.setRegisterUser(user);
                    ((HomeActivity) requireActivity()).sessionManager.DRIVER_ONLINE_STATUS(false);
                    // ((HomeActivity) requireActivity()).sessionManager.setVehicalData(mVehicalDTO);
                    ((HomeActivity) requireActivity()).sessionManager.set_DELIVERY_TYPE_DRIVER(false);
                    ((HomeActivity) requireActivity()).sessionManager.COMPANY_DRIVER(false);
                    //((HomeActivity) requireActivity()).sessionManager.setTokenFCM("");
                    SavedData.saveCourierDriver(false);
                    SavedData.saveDriver(false);
                    SavedData.saveCourier(false);


                } else {
                    logout();
                }


            }
        });

        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        alertdialog.show();
    }


    private void logout() {

        final Dialog dialog = Utils.initProgressDialog(requireActivity());
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("device_id", m_deviceId);
        hm.put("driver_id", ((HomeActivity) requireActivity()).sessionManager.getRegisterUser().getId());

        Call<AllResponse> call = apiService.logout(hm);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                dialog.dismiss();
                try {

                    AllResponse body = response.body();

                    if (body.getStatus() == 200) {

                        startActivity(new Intent(requireActivity(), LoginActivity.class));
                        requireActivity().finish();
                        CourierDTO user = new CourierDTO();
                        ((HomeActivity) requireActivity()).sessionManager.setLoginSession(false);
                        ((HomeActivity) requireActivity()).sessionManager.setRegisterUser(user);
                        ((HomeActivity) requireActivity()).sessionManager.DRIVER_ONLINE_STATUS(false);
                        //  ((HomeActivity) requireActivity()).sessionManager.setVehicalData(mVehicalDTO);
                        ((HomeActivity) requireActivity()).sessionManager.set_DELIVERY_TYPE_DRIVER(false);
                        ((HomeActivity) requireActivity()).sessionManager.COMPANY_DRIVER(false);





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

    private void ShowDialog(String sms) {
        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                .setMessage(sms)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Utils.I(this, AmountDoneNotificationActivity.class, null);
                        dialog.dismiss();
                    }
                }).show();

    }


    private void getDriverStatus() {


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", ((HomeActivity) requireActivity()).sessionManager.getRegisterUser().getId());
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

    @Override
    public void onResume() {
        super.onResume();
        initialization(root);
    }
}

package com.dollop.dukaadriver.UtilityTools;

import android.content.Context;
import android.content.SharedPreferences;
import android.service.autofill.UserData;


import com.dollop.dukaadriver.model.CourierDTO;
import com.dollop.dukaadriver.model.VehicalDTO;

import java.util.HashMap;

import static android.provider.MediaStore.Video.VideoColumns.LANGUAGE;

public class SessionManager {

    private static final String IS_LOGIN = "IsLogin";
    private static final String PREFER_NAME = "CUSTOIMER_APP";
    private static final String PREFER_NAME_GLOBEL = "CUSTOMER_APP_GLOBEL";
    private static final String DRIVER_USER_TYPE = "user_type";

    private static final String id = "id";
    private static final String fullName = "fullName";
    private static final String companyName = "companyName";
    private static final String email = "email";
    private static final String mobile = "mobile";
    private static final String password = "password";
    private static final String otp = "otp";
    private static final String isActive = "isActive";
    private static final String isDelete = "isDelete";
    private static final String createDate = "createDate";
    private static final String type = "type";
    private static final String deliveryPartnerId = "deliveryPartnerId";
    private static final String selfService = "selfService";
    private static final String nationalId = "nationalId";
    private static final String nationalIdImg = "nationalIdImg";
    private static final String licenseImg = "licenseImg";
    private static final String vehicleInsuranceImg = "vehicleInsuranceImg";
    private static final String saccoName = "saccoName";
    private static final String saccoMembershipNumber = "saccoMembershipNumber";
    private static final String otherServices = "otherServices";
    private static final String DRIVER_ONLINE_STATUS = "online_status";
    private static final String profile_img = "profile_img";
    private static final String tokenFcm = "fcm_id";
    private static final String vehical_id = "vehical_id";
    private static final String driverId = "driverId";
    private static final String vehicleName = "vehicleName";
    private static final String vehicleNum = "vehicleNum";
    private static final String vehicleType = "vehicleType";
    private static final String modelName = "modelName";
    private static final String vehicleRegistrionNumber = "vehicleRegistrionNumber";
    private static final String vehicleInsurance = "vehicleInsurance";

    private static final String COMPANY_DRIVER = "company_driver";
    private static final String COMPANY_LAT = "company_lat";
    private static final String COMPANY_LONG = "company_long";

    private static final String WELCOME_SCREEN = "false";

    Context _context;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor_globel;
    SharedPreferences pref;
    SharedPreferences pref_globel;//
    int PRIVATE_MODE = 0;


    public SessionManager(Context context) {
        this._context = context;
        this.pref = this._context.getSharedPreferences(PREFER_NAME, this.PRIVATE_MODE);
        this.pref_globel = this._context.getSharedPreferences(PREFER_NAME_GLOBEL, this.PRIVATE_MODE);
        this.editor = this.pref.edit();
        this.editor_globel = this.pref_globel.edit();

    }


    public void setLoginSession(boolean bool) {
        editor.putBoolean(IS_LOGIN, bool);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }


    public void set_DELIVERY_TYPE_DRIVER(boolean bool) {
        editor.putBoolean(DRIVER_USER_TYPE, bool);
        editor.commit();
    }

    public boolean is_DRIVER() {
        return pref.getBoolean(DRIVER_USER_TYPE, false);
    }

/*
    public void setRegisterUser(CourierDTO mUser) {

        editor.putString(id, mUser.getId());
        editor.putString(fullName, mUser.getFullName());
        editor.putString(companyName, mUser.getCompanyName());
        editor.putString(email, mUser.getEmail());
        editor.putString(mobile, mUser.getMobile());
        editor.putString(password, mUser.getPassword());
        editor.putString(otp, mUser.getOtp());
        editor.putString(type, mUser.getType());
        editor.putString(isActive, mUser.getIsActive());
        editor.putString(isDelete, mUser.getIsDelete());
        editor.putString(createDate, mUser.getCreateDate());

        editor.commit();

    }

    public CourierDTO getRegisterUser() {
        if (pref.getString(id, null) != null) {

            String str_id, str_fullName, str_companyName, str_email, str_mobile, str_password, str_otp,
                    str_isActive, str_isDelete, str_createDate, str_type;


            str_id = pref.getString(id, null);
            str_fullName = pref.getString(fullName, null);
            str_companyName = pref.getString(companyName, null);
            str_email = pref.getString(email, null);
            str_mobile = pref.getString(mobile, null);
            str_password = pref.getString(password, null);
            str_otp = pref.getString(otp, null);
            str_type = pref.getString(type, null);
            str_isActive = pref.getString(isActive, null);
            str_isDelete = pref.getString(isDelete, null);
            str_createDate = pref.getString(createDate, null);


            CourierDTO user = new CourierDTO(str_id, str_fullName, str_companyName, str_email,
                    str_mobile, str_password, str_otp, str_type,
                    str_isActive, str_isDelete, str_createDate);

            return user;
        }
        return null;
    }
*/


    public void setRegisterUser(CourierDTO mUser) {

        editor.putString(id, mUser.getId());
        editor.putString(fullName, mUser.getFullName());
        editor.putString(companyName, mUser.getCompanyName());
        editor.putString(email, mUser.getEmail());
        editor.putString(mobile, mUser.getMobile());
        editor.putString(password, mUser.getPassword());
        editor.putString(otp, mUser.getOtp());
        editor.putString(type, mUser.getType());
        editor.putString(isActive, mUser.getIsActive());
        editor.putString(isDelete, mUser.getIsDelete());
        editor.putString(createDate, mUser.getCreateDate());
        editor.putString(deliveryPartnerId, mUser.getDeliveryPartnerId());
        editor.putString(selfService, mUser.getSelfService());
        editor.putString(nationalId, mUser.getNationalId());
        editor.putString(nationalIdImg, mUser.getNationalIdImg());
        editor.putString(licenseImg, mUser.getLicenseImg());
        editor.putString(vehicleInsuranceImg, mUser.getVehicleInsuranceImg());
        editor.putString(saccoName, mUser.getSaccoName());
        editor.putString(saccoMembershipNumber, mUser.getSaccoMembershipNumber());
        editor.putString(otherServices, mUser.getOtherServices());
        editor.putString(profile_img, mUser.getProfile_img());
        editor.putString(vehicleType, mUser.getVehicle_type());

        editor.commit();

    }

    public CourierDTO getRegisterUser() {
        if (pref.getString(id, null) != null) {

            String str_id, str_fullName, str_companyName, str_email, str_mobile, str_password, str_otp,
                    str_isActive, str_isDelete, str_createDate, str_type, str_deliveryPartnerId, str_selfService, str_nationalId,
                    str_nationalIdImg, str_licenseImg, str_vehicleInsuranceImg, str_saccoName, str_saccoMembershipNumber, str_otherServices;
            String str_profile, str_vehicle_type;

            str_id = pref.getString(id, null);
            str_fullName = pref.getString(fullName, null);
            str_companyName = pref.getString(companyName, null);
            str_email = pref.getString(email, null);
            str_mobile = pref.getString(mobile, null);
            str_password = pref.getString(password, null);
            str_otp = pref.getString(otp, null);
            str_type = pref.getString(type, null);
            str_isActive = pref.getString(isActive, null);
            str_isDelete = pref.getString(isDelete, null);
            str_createDate = pref.getString(createDate, null);

            str_deliveryPartnerId = pref.getString(deliveryPartnerId, null);
            str_selfService = pref.getString(selfService, null);
            str_nationalId = pref.getString(nationalId, null);
            str_nationalIdImg = pref.getString(nationalIdImg, null);
            str_licenseImg = pref.getString(licenseImg, null);
            str_vehicleInsuranceImg = pref.getString(vehicleInsuranceImg, null);
            str_saccoName = pref.getString(saccoName, null);
            str_saccoMembershipNumber = pref.getString(saccoMembershipNumber, null);
            str_otherServices = pref.getString(otherServices, null);
            str_profile = pref.getString(profile_img, null);
            str_vehicle_type = pref.getString(vehicleType, null);


            CourierDTO user = new CourierDTO(str_id, str_fullName, str_companyName, str_email, str_mobile, str_password, str_otp,
                    str_isActive, str_isDelete, str_createDate, str_type, str_deliveryPartnerId, str_selfService,
                    str_nationalId, str_nationalIdImg, str_licenseImg, str_vehicleInsuranceImg,
                    str_saccoName, str_saccoMembershipNumber, str_otherServices, str_profile, str_vehicle_type);

            return user;
        }
        return null;
    }


    public void DRIVER_ONLINE_STATUS(boolean bool) {
        editor.putBoolean(DRIVER_ONLINE_STATUS, bool);
        editor.commit();
    }

    public boolean is_DRIVER_ONLINE_STATUS() {
        return pref.getBoolean(DRIVER_ONLINE_STATUS, false);
    }
/*

    public void setVehicalData(VehicalDTO mVehical) {
        // Storing login value as TRUE
        editor.putBoolean(vehical_id, true);
        editor.putString(vehical_id, mVehical.getId());
        editor.putString(vehicleName, mVehical.getVehicleName());
        editor.putString(vehicleNum, mVehical.getVehicleNum());
        editor.putString(driverId, mVehical.getDriverId());
        editor.putString(vehicleType, mVehical.getVehicleType());
        editor.putString(modelName, mVehical.getModelName());
        editor.putString(vehicleRegistrionNumber, mVehical.getVehicleRegistrionNumber());
        editor.putString(vehicleInsurance, mVehical.getVehicleInsurance());
        // commit changes
        editor.commit();
    }


    public VehicalDTO getVehicalData() {
        if (pref.getString(vehical_id, null) != null) {

            String str_id, str_driverId, str_vehicleName, str_vehicleNum, str_isActive, str_isDelete, str_createDate;
            String str_vehicleType, str_modelName, str_vehicleRegistrionNumber, str_vehicleInsurance;

            str_id = pref.getString(vehical_id, null);
            str_driverId = pref.getString(driverId, null);
            str_vehicleName = pref.getString(vehicleName, null);
            str_vehicleNum = pref.getString(vehicleNum, null);
            str_vehicleType = pref.getString(vehicleType, null);
            str_modelName = pref.getString(modelName, null);
            str_vehicleRegistrionNumber = pref.getString(vehicleRegistrionNumber, null);
            str_vehicleInsurance = pref.getString(vehicleInsurance, null);

            VehicalDTO user = new VehicalDTO(str_id, str_driverId, str_vehicleName, str_vehicleNum, str_vehicleType,
                    str_modelName, str_vehicleRegistrionNumber, str_vehicleInsurance);

            return user;
        }
        return null;
    }
*/

    public void setTokenFCM(String token) {
        editor.putString(tokenFcm, token);
        editor.commit();

    }

    public String getTokenFCM() {

        Utils.E("GETTOKEN" + pref.getString(tokenFcm, null));
        return pref.getString(tokenFcm, null);

    }

    public void COMPANY_DRIVER(boolean bool) {
        editor.putBoolean(COMPANY_DRIVER, bool);
        editor.commit();
    }

    public boolean is_COMPANY_DRIVER() {
        return pref.getBoolean(COMPANY_DRIVER, false);
    }


    public void current_LAT(String lat) {
        editor.putString(COMPANY_LAT, lat);
        editor.commit();

    }

    public String getCurrent_LAT() {
        return pref.getString(COMPANY_LAT, null);
    }

    public void current_LONG(String lat) {
        editor.putString(COMPANY_LONG, lat);
        editor.commit();
    }

    public String getCurrent_LONG() {
        return pref.getString(COMPANY_LONG, null);
    }



    public void set_WELCOME_SCREEN(boolean bool) {
        editor.putBoolean(WELCOME_SCREEN, bool);
        editor.commit();
    }

    public boolean is_WELCOME_SCREEN() {
        return pref.getBoolean(WELCOME_SCREEN, false);
    }
}

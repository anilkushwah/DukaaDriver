package com.dollop.dukaadriver.UtilityTools;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class SavedData {

    private static final String firebaseToken = "firebaseToken";
    private static SharedPreferences prefs;
    private static final String LatitudeService = "latitudeService";
    private static final String LongitudeService = "LongitudeService";
    private static final String OrderId = "OrderId";
    private static final String CustomerMobile = "customerMobile";
    private static final String VehicalName = "vehicalName";
    private static final String VehicalModal = "vehicalModal";
    private static final String CustomerName = "customerName";
    private static final String CustomerAddress = "customerAddress";
    private static final String CustomerPic = "customerPic";
    private static final String CustomerLat = "customerLat";
    private static final String CustomerLong = "customerLong";
    private static final String Driver = "Driver";
    private static final String Courier = "Courier";
    private static final String CourierDriver = "CourierDriver";
    private static final String Path = "Path";

    public static SharedPreferences getInstance() {
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(AppController.getInstance());
        }
        return prefs;
    }
    public static boolean getCourierDriver() {
        return getInstance().getBoolean(CourierDriver, false);
    }

    public static void saveCourierDriver(boolean startKm) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putBoolean(CourierDriver, startKm);
        editor.apply();
    }public static boolean getPath() {
        return getInstance().getBoolean(Path, false);
    }

    public static void savePath(boolean startKm) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putBoolean(Path, startKm);
        editor.apply();
    }

    public static boolean getCourier() {
        return getInstance().getBoolean(Courier, false);
    }

    public static void saveCourier(boolean startKm) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putBoolean(Courier, startKm);
        editor.apply();
    }

    public static boolean getDriver() {
        return getInstance().getBoolean(Driver, false);
    }

    public static void saveDriver(boolean startKm) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putBoolean(Driver, startKm);
        editor.apply();
    }


    public static String getFirebaseToken() {
        return getInstance().getString(firebaseToken, "");
    }

    public static void saveFirebaseToken(String startKm) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(firebaseToken, startKm);
        editor.apply();
    }


    public static String getLatitudeFromService() {
        return getInstance().getString(LatitudeService, "");
    }

    public static void saveLatitudeFromService(String latitude) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(LatitudeService, latitude);
        editor.apply();
    }


    public static String getLongitudeFromService() {
        return getInstance().getString(LongitudeService, "");
    }

    public static void saveLongitudeFromService(String longitude) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(LongitudeService, longitude);
        editor.apply();
    }

    public static String getOrderId() {
        return getInstance().getString(OrderId, "");
    }

    public static void saveOrderId(String orderId) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(OrderId, orderId);
        editor.apply();
    }

    public static String getCustomerMobile() {
        return getInstance().getString(CustomerMobile, "");
    }

    public static void saveCustomerMobile(String customerMobile) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(CustomerMobile, customerMobile);
        editor.apply();
    }

    public static String getVehicalName() {
        return getInstance().getString(VehicalName, "");
    }

    public static void saveVehicalName(String vehicalName) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(VehicalName, vehicalName);
        editor.apply();
    }

    public static String getVehicalModal() {
        return getInstance().getString(VehicalModal, "");
    }

    public static void saveVehicalModal(String vehicalModal) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(VehicalModal, vehicalModal);
        editor.apply();
    }

    public static String getCustomerName() {
        return getInstance().getString(CustomerName, "");
    }

    public static void saveCustomerName(String customerName) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(CustomerName, customerName);
        editor.apply();
    }

    public static String getCustomerAddress() {
        return getInstance().getString(CustomerAddress, "");
    }

    public static void saveCustomerAddress(String customerAddress) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(customerAddress, customerAddress);
        editor.apply();
    }


    public static String getCustomerPic() {
        return getInstance().getString(CustomerPic, "");
    }

    public static void saveCustomerPic(String customerPic) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(customerPic, customerPic);
        editor.apply();
    }


    public static String getCustomerLat() {
        return getInstance().getString(CustomerLat, "");
    }

    public static void saveCustomerLat(String customerLat) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(customerLat, customerLat);
        editor.apply();
    }


    public static String getCustomerLong() {
        return getInstance().getString(CustomerLong, "");
    }

    public static void saveCustomerLong(String customerLong) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(customerLong, customerLong);
        editor.apply();
    }

}
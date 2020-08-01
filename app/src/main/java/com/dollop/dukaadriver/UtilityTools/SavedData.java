package com.dollop.dukaadriver.UtilityTools;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class SavedData {



    private static final String adminlogin = "adminlogin";
    private static final String emp_name = "empname";
    private static final String emp_image = "empimage";
    private static final String active_user = "active_admin";


    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;





    /*Order ID 146381161*/





    public static SharedPreferences getInstance() {
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(AppController.getInstance());
        }
        return prefs;
    }






    public static String getActive_user() {
        return getInstance().getString(active_user, "");
    }

    public static void saveactiveUser(String startKm) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(active_user, startKm);
        editor.commit();
        editor.apply();
    }

    public static String getEmp_name() {
        return getInstance().getString(emp_name, "0");
    }

    public static String getEmp_image() {
        return getInstance().getString(emp_image, "0");
    }

    public static void saveimage(String startKm) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(emp_image, startKm);
        editor.commit();
        editor.apply();
    }

    public static String getAdminlogin() {
        return getInstance().getString(adminlogin, "0");
    }


    public static void saveempname(String startKm) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(emp_name, startKm);
        editor.commit();
        editor.apply();
    }

    public static void saveadminlogin(String startKm) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(adminlogin, startKm);
        editor.commit();
        editor.apply();
    }


}
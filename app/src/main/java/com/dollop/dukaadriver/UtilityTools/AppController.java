package com.dollop.dukaadriver.UtilityTools;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.dollop.dukaadriver.database.LocaleHelper;
import com.dollop.dukaadriver.database.UserDataHelper;


/**
 * Created by androidsys1 on 3/31/2017.
 */


public class AppController extends MultiDexApplication {

    private static AppController mInstance;

    public static int invoiceId = 0;
    private RequestQueue mRequestQueue;
    public static final String TAG = AppController.class.getSimpleName();

    ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
    boolean connected = false;
    static Context context;
    public static synchronized AppController getInstance() {
        return mInstance;
    }
    public static AppController getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return mInstance;
    }


/*    SharedPreferences detailscheck =  getSharedPreferences("DriverTaxiDetails", Context.MODE_PRIVATE);
    String setlanguage  = detailscheck.getString("language_setting","");*/

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, LocaleHelper.getLanguage(base)));
      //  super.attachBaseContext(base);
//        MultiDex.install(this);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleHelper.setLocale(context,LocaleHelper.getLanguage(context));
        Utils.E("Appcontrller language"+LocaleHelper.getLanguage(context));
    }

    public static final int GRANTED = 0;
    public static final int DENIED = 1;
    public static final int BLOCKED = 2;



    public boolean isOnline() {
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;

        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }


    @Override
    public void onCreate() {
//        MultiDex.install(this);
        super.onCreate();
        mInstance = this;
        new UserDataHelper(this);





    }

    public void handleUncaughtException(Thread thread, Throwable e) {
        e.printStackTrace();
    }

}


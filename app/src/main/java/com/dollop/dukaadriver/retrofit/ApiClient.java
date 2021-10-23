package com.dollop.dukaadriver.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Anil 02/10/2020.
 */

public class ApiClient {

   // public static final String BASE_URL = "https://app.dukaa.co.ke/";
   // public static final String BASE_URL = "https://dukaa.bennito254.com/";
    public static final String BASE_URL = "http://116.75.243.44:8080/dukaa/";

     private static Retrofit retrofit = null;
    public static final String REGISTRATION_COMPLETE = "registration_complete";
    public static final String order_history = BASE_URL + "order_history";
    public static final String driver_arrived = BASE_URL + "driver_arrived";
    public static final String driver_resend_otp = BASE_URL + "driver_resend_otp";
    public static final String driver_change_password = BASE_URL + "driver_change_password";
    public static final String driver_forgot_password = BASE_URL + "driver_forgot_password";
    public static final String match_otp = BASE_URL + "match_otp";

    public static final String payment_notification_for_retailer = BASE_URL + "payment_notification_for_retailer";
    public static final String requestPayment = BASE_URL + "requestPayment";
    public static final String update_transaction_id = BASE_URL + "update_transaction_id";
    public static final String generate_transaction_id = BASE_URL + "generate_transaction_id";


    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES).build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

}

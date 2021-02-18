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

    public static final String BASE_URL =  "https://app.dukaa.co.ke/";
  //  public static final String BASE_URL = "http://116.75.243.44:8080/dukaa/";
 //   public static final String BASE_URLImage = "http://116.75.243.44:8080/dukka/uploads/driver/driver_profile/";
 //   public static final String BASE_URL_licence = "http://116.75.243.44:8080/dukka/uploads/driver/licence/";
  //  public static final String BASE_URL_vehicle_insurance = "http://116.75.243.44:8080/dukka/uploads/vehicle_insurance/";
    private static Retrofit retrofit = null;
    public static final String REGISTRATION_COMPLETE = "registration_complete";
    public static final String order_history = BASE_URL+"order_history";
    public static final String driver_resend_otp = BASE_URL+"order_history";


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

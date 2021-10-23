package com.dollop.dukaadriver.retrofit;
import com.dollop.dukaadriver.model.AccpetOrderDTO;
import com.dollop.dukaadriver.model.DistanceDTO;
import com.dollop.dukaadriver.model.HomeDTO;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.OTPResponse;
import com.dollop.dukaadriver.model.RatingDTO;
import com.dollop.dukaadriver.model.SignupDTO;
import com.dollop.dukaadriver.model.StaticsDTO;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
/**
 * Created by Anil 02/10/2020.
 */
public interface ApiInterface {

    @FormUrlEncoded
    @POST("login")
    Call<OTPResponse> login(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("courier_register")
    Call<SignupDTO> courier_register(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("driver_register")
    Call<SignupDTO> driver_register(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("match_otp")
    Call<OTPResponse> match_otp(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("add_vehicle")
    Call<AllResponse> add_vehicle(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("view_vehicle")
    Call<AllResponse> view_vehicle(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("driver_accept_order")
    Call<AccpetOrderDTO> accept_order(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("driver_view_order")
    Call<HomeDTO> view_orders(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("courier_view_order")
    Call<HomeDTO> courier_view_order(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("get_driver_by_companyId")
    Call<AllResponse> get_driver_by_companyId(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("driver_status")
    Call<AllResponse> driver_status(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("courier_register")
    Call<OTPResponse> courier_register1(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("driver_change_password")
    Call<AllResponse> driver_change_password(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("route_jobs_order")
    Call<HomeDTO> route_jobs_order(@FieldMap HashMap<String, String> hm);


    @FormUrlEncoded
    @POST("delete_vehicle")
    Call<AllResponse> delete_vehicle(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("get_driver_status")
    Call<AllResponse> get_driver_status(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("logout")
    Call<AllResponse> logout(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("driver_distance")
    Call<DistanceDTO> driver_distance(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("courier_accept_order")
    Call<AccpetOrderDTO> courier_accept_order(@FieldMap HashMap<String, String> hm);


    @FormUrlEncoded
    @POST("pending_assign_driver")
    Call<AllResponse> pending_assign_driver(@FieldMap HashMap<String, String> hm);


    @FormUrlEncoded
    @POST("courier_delete_driver")
    Call<AllResponse> courier_delete_driver(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("start_job")
    Call<AllResponse> start_job(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("picked_order")
    Call<AllResponse> picked_order(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("delivered_order")
    Call<AllResponse> delivered_order(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("get_driver_rating")
    Call<RatingDTO> get_driver_rating(@FieldMap HashMap<String, String> hm);


    @FormUrlEncoded
    @POST("driver_earning")
    Call<AllResponse> driver_earning(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("courier_earning")
    Call<AllResponse> courier_earning(@FieldMap HashMap<String, String> hm);


    @FormUrlEncoded
    @POST("get_driver_accept_order")
    Call<AllResponse> get_driver_accept_order(@FieldMap HashMap<String, String> hm);


    @FormUrlEncoded
    @POST("courierOrder_assign_driver")
    Call<HomeDTO> courierOrder_assign_driver(@FieldMap HashMap<String, String> hm);


    @FormUrlEncoded
    @POST("statics_driver_earning")
    Call<StaticsDTO> statics_driver_earning(@FieldMap HashMap<String, String> hm);

    @GET("get_vehicle_type")
    Call<AllResponse> get_vehicle_type();

    @FormUrlEncoded
    @POST("courier_assign_vehicle")
    Call<AllResponse> courier_assign_vehicle(@FieldMap HashMap<String, String> hm);


    @FormUrlEncoded
    @POST("driver_update_lat_long")
    Call<AllResponse> driver_update_lat_long(@FieldMap HashMap<String, String> hm);


    @FormUrlEncoded
    @POST("get_all_notificaton")
    Call<AllResponse> get_all_notificaton(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("driver_update_token")
    Call<AllResponse> driver_update_token(@FieldMap HashMap<String, String> hm);


    @FormUrlEncoded
    @POST("order_history")
    Call<AllResponse> order_history(@FieldMap HashMap<String, String> hm);
}

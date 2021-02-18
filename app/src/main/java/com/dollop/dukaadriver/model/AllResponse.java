package com.dollop.dukaadriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AllResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;


    @SerializedName("total_earning")
    @Expose
    private String total_earning;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @SerializedName("vehicleList")
    @Expose
    private ArrayList<VehicalDTO> mVehicalDTOS = null;

    public ArrayList<VehicalDTO> getmVehicalDTOS() {
        return mVehicalDTOS;
    }

    public void setmVehicalDTOS(ArrayList<VehicalDTO> mVehicalDTOS) {
        this.mVehicalDTOS = mVehicalDTOS;
    }

    @SerializedName("driverList")
    @Expose
    private ArrayList<DriverDTO> driverList = null;

    public ArrayList<DriverDTO> getDriverList() {
        return driverList;
    }

    public void setDriverList(ArrayList<DriverDTO> driverList) {
        this.driverList = driverList;
    }

    @SerializedName("assignDriver")
    @Expose
    private ArrayList<DriverDTO> assignDriver = null;

    public ArrayList<DriverDTO> getAssignDriver() {
        return assignDriver;
    }

    public void setAssignDriver(ArrayList<DriverDTO> assignDriver) {
        this.assignDriver = assignDriver;
    }


    @SerializedName("rating")
    @Expose
    private ArrayList<MyReviewModel> rating = null;

    public ArrayList<MyReviewModel> getRating() {
        return rating;
    }

    public void setRating(ArrayList<MyReviewModel> rating) {
        this.rating = rating;
    }



    @SerializedName("earning")
    @Expose
    private ArrayList<TotalEarnModel> earning = null;

    public ArrayList<TotalEarnModel> getEarning() {
        return earning;
    }

    public void setEarning(ArrayList<TotalEarnModel> earning) {
        this.earning = earning;
    }

    public String getTotal_earning() {
        return total_earning;
    }

    public void setTotal_earning(String total_earning) {
        this.total_earning = total_earning;
    }



    @SerializedName("acceptedOrderList")
    @Expose
    private ArrayList<OrderDTO> acceptedOrderList = null;


    public ArrayList<OrderDTO> getAcceptedOrderList() {
        return acceptedOrderList;
    }

    public void setAcceptedOrderList(ArrayList<OrderDTO> acceptedOrderList) {
        this.acceptedOrderList = acceptedOrderList;
    }

    @SerializedName("vehicleType")
    @Expose
    private ArrayList<VehicleTypeDTO> vehicleType = null;


    public ArrayList<VehicleTypeDTO> getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(ArrayList<VehicleTypeDTO> vehicleType) {
        this.vehicleType = vehicleType;
    }
    @SerializedName("notification")
    @Expose
    private ArrayList<NotificationDTO> notification = null;

    public ArrayList<NotificationDTO> getNotification() {
        return notification;
    }

    public void setNotification(ArrayList<NotificationDTO> notification) {
        this.notification = notification;
    }
}

package com.dollop.dukaadriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleTypeDTO {

    @SerializedName("vehicle_type_id")
    @Expose
    private String vehicleTypeId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("min_weight")
    @Expose
    private String minWeight;
    @SerializedName("max_weight")
    @Expose
    private String maxWeight;
    @SerializedName("weight_fare")
    @Expose
    private String weightFare;
    @SerializedName("distance_fare")
    @Expose
    private String distanceFare;
    @SerializedName("fare_status")
    @Expose
    private String fareStatus;

    public String getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(String vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(String minWeight) {
        this.minWeight = minWeight;
    }

    public String getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(String maxWeight) {
        this.maxWeight = maxWeight;
    }

    public String getWeightFare() {
        return weightFare;
    }

    public void setWeightFare(String weightFare) {
        this.weightFare = weightFare;
    }

    public String getDistanceFare() {
        return distanceFare;
    }

    public void setDistanceFare(String distanceFare) {
        this.distanceFare = distanceFare;
    }

    public String getFareStatus() {
        return fareStatus;
    }

    public void setFareStatus(String fareStatus) {
        this.fareStatus = fareStatus;
    }

}

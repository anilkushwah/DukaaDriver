package com.dollop.dukaadriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VehicalDTO implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("vehicle_name")
    @Expose
    private String vehicleName;
    @SerializedName("vehicle_num")
    @Expose
    private String vehicleNum;
    @SerializedName("vehicle_type")
    @Expose
    private String vehicleType;
    @SerializedName("model_name")
    @Expose
    private String modelName;
    @SerializedName("vehicle_registrion_number")
    @Expose
    private String vehicleRegistrionNumber;
    @SerializedName("vehicle_insurance")
    @Expose
    private String vehicleInsurance;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("is_delete")
    @Expose
    private String isDelete;
    @SerializedName("create_date")
    @Expose
    private String createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleNum() {
        return vehicleNum;
    }

    public void setVehicleNum(String vehicleNum) {
        this.vehicleNum = vehicleNum;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getVehicleRegistrionNumber() {
        return vehicleRegistrionNumber;
    }

    public void setVehicleRegistrionNumber(String vehicleRegistrionNumber) {
        this.vehicleRegistrionNumber = vehicleRegistrionNumber;
    }

    public String getVehicleInsurance() {
        return vehicleInsurance;
    }

    public void setVehicleInsurance(String vehicleInsurance) {
        this.vehicleInsurance = vehicleInsurance;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public VehicalDTO(String id, String driverId, String vehicleName,
                      String vehicleNum, String vehicleType, String modelName, String vehicleRegistrionNumber, String vehicleInsurance) {
        this.id = id;
        this.driverId = driverId;
        this.vehicleName = vehicleName;
        this.vehicleNum = vehicleNum;
        this.vehicleType = vehicleType;
        this.modelName = modelName;
        this.vehicleRegistrionNumber = vehicleRegistrionNumber;
        this.vehicleInsurance = vehicleInsurance;

    }

    public VehicalDTO() {
    }
}

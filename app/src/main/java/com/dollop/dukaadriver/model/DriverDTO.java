package com.dollop.dukaadriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DriverDTO implements Serializable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("order_id")
    @Expose
    public String order_id;
    @SerializedName("delivery_partner_id")
    @Expose
    private String deliveryPartnerId;
    @SerializedName("self_service")
    @Expose
    private Object selfService;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("national_id")
    @Expose
    private String nationalId;
    @SerializedName("national_id_img")
    @Expose
    private Object nationalIdImg;
    @SerializedName("license_img")
    @Expose
    private String licenseImg;
    @SerializedName("profile_img")
    @Expose
    private String profileImg;
    @SerializedName("sacco_name")
    @Expose
    private String saccoName;
    @SerializedName("sacco_membership_number")
    @Expose
    private String saccoMembershipNumber;
    @SerializedName("other_services")
    @Expose
    private String otherServices;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("is_delete")
    @Expose
    private String isDelete;
    @SerializedName("create_date")
    @Expose
    private String createDate;

    @SerializedName("vehicle_type")
    @Expose
    private String vehicle_type;

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeliveryPartnerId() {
        return deliveryPartnerId;
    }

    public void setDeliveryPartnerId(String deliveryPartnerId) {
        this.deliveryPartnerId = deliveryPartnerId;
    }

    public Object getSelfService() {
        return selfService;
    }

    public void setSelfService(Object selfService) {
        this.selfService = selfService;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public Object getNationalIdImg() {
        return nationalIdImg;
    }

    public void setNationalIdImg(Object nationalIdImg) {
        this.nationalIdImg = nationalIdImg;
    }

    public String getLicenseImg() {
        return licenseImg;
    }

    public void setLicenseImg(String licenseImg) {
        this.licenseImg = licenseImg;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getSaccoName() {
        return saccoName;
    }

    public void setSaccoName(String saccoName) {
        this.saccoName = saccoName;
    }

    public String getSaccoMembershipNumber() {
        return saccoMembershipNumber;
    }

    public void setSaccoMembershipNumber(String saccoMembershipNumber) {
        this.saccoMembershipNumber = saccoMembershipNumber;
    }

    public String getOtherServices() {
        return otherServices;
    }

    public void setOtherServices(String otherServices) {
        this.otherServices = otherServices;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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


}

package com.dollop.dukaadriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CourierDTO {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("is_delete")
    @Expose
    private String isDelete;
    @SerializedName("create_date")
    @Expose
    private String createDate;

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("profile_img")
    @Expose
    private String profile_img;

    @SerializedName("vehicle_type")
    @Expose
    private String vehicle_type;


    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public CourierDTO(String id, String fullName, String companyName, String email,
                      String mobile, String password,
                      String otp, String type, String isActive, String isDelete, String createDate, String profile_img) {
        this.id = id;
        this.fullName = fullName;
        this.companyName = companyName;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.otp = otp;
        this.type = type;
        this.isActive = isActive;
        this.isDelete = isDelete;
        this.createDate = createDate;
        this.profile_img = profile_img;

    }

    public CourierDTO() {
    }


    @SerializedName("delivery_partner_id")
    @Expose
    private String deliveryPartnerId;
    @SerializedName("self_service")
    @Expose
    private String selfService;

    @SerializedName("national_id")
    @Expose
    private String nationalId;
    @SerializedName("national_id_img")
    @Expose
    private String nationalIdImg;
    @SerializedName("license_img")
    @Expose
    private String licenseImg;
    @SerializedName("vehicle_insurance_img")
    @Expose
    private String vehicleInsuranceImg;
    @SerializedName("sacco_name")
    @Expose
    private String saccoName;
    @SerializedName("sacco_membership_number")
    @Expose
    private String saccoMembershipNumber;
    @SerializedName("other_services")
    @Expose
    private String otherServices;


    public String getDeliveryPartnerId() {
        return deliveryPartnerId;
    }

    public void setDeliveryPartnerId(String deliveryPartnerId) {
        this.deliveryPartnerId = deliveryPartnerId;
    }

    public String getSelfService() {
        return selfService;
    }

    public void setSelfService(String selfService) {
        this.selfService = selfService;
    }


    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getNationalIdImg() {
        return nationalIdImg;
    }

    public void setNationalIdImg(String nationalIdImg) {
        this.nationalIdImg = nationalIdImg;
    }

    public String getLicenseImg() {
        return licenseImg;
    }

    public void setLicenseImg(String licenseImg) {
        this.licenseImg = licenseImg;
    }

    public String getVehicleInsuranceImg() {
        return vehicleInsuranceImg;
    }

    public void setVehicleInsuranceImg(String vehicleInsuranceImg) {
        this.vehicleInsuranceImg = vehicleInsuranceImg;
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

    public CourierDTO(String id, String fullName, String companyName, String email, String mobile, String password, String otp,
                      String isActive, String isDelete, String createDate, String type, String deliveryPartnerId, String selfService,
                      String nationalId, String nationalIdImg, String licenseImg, String vehicleInsuranceImg,
                      String saccoName, String saccoMembershipNumber, String otherServices, String profile_img, String vehicle_type) {
        this.id = id;
        this.fullName = fullName;
        this.companyName = companyName;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.otp = otp;
        this.isActive = isActive;
        this.isDelete = isDelete;
        this.createDate = createDate;
        this.type = type;
        this.deliveryPartnerId = deliveryPartnerId;
        this.selfService = selfService;
        this.nationalId = nationalId;
        this.nationalIdImg = nationalIdImg;
        this.licenseImg = licenseImg;
        this.vehicleInsuranceImg = vehicleInsuranceImg;
        this.saccoName = saccoName;
        this.saccoMembershipNumber = saccoMembershipNumber;
        this.otherServices = otherServices;
        this.profile_img = profile_img;
        this.vehicle_type = vehicle_type;
    }
}

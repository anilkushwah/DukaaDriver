package com.dollop.dukaadriver.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderDTO implements Serializable  {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("gen_order_id")
    @Expose
    private Object genOrderId;

    @SerializedName("retailer_id")
    @Expose
    private String retailerId;

    @SerializedName("distributor_id")
    @Expose
    private String distributorId;

    @SerializedName("offer_id")
    @Expose
    private String offerId;

    @SerializedName("offer_json")
    @Expose
    private String offerJson;

    @SerializedName("retailer_landmark")
    @Expose
    private String retailer_landmark;

    @SerializedName("coupon_id")
    @Expose
    private String couponId;

    @SerializedName("total_amount")
    @Expose
    private String totalAmount;

    @SerializedName("offer_discount_amount")
    @Expose
    private String offerDiscountAmount;

    @SerializedName("coupon_discount_amount")
    @Expose
    private String couponDiscountAmount;
    @SerializedName("product_discounted_price")
    @Expose
    private String productDiscountedPrice;
    @SerializedName("paid_amount")
    @Expose
    private String paidAmount;
    @SerializedName("address_id")
    @Expose
    private String addressId;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("transaction_mode")
    @Expose
    private String transactionMode;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("service_charge")
    @Expose
    private String serviceCharge;
    @SerializedName("delivery_charge")
    @Expose
    private String deliveryCharge;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("retailer_address")
    @Expose
    private String retailerAddress;
    @SerializedName("distributor_address")
    @Expose
    private String distributorAddress;
    @SerializedName("dist_lat")
    @Expose
    private String storeLat;
    @SerializedName("dist_long")
    @Expose
    private String storeLong;
    @SerializedName("itemCount")
    @Expose
    private String itemCount;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("distribuor_mobile")
    @Expose
    private String distribuorMobile;
    @SerializedName("distributor_image")
    @Expose
    private Object distributorImage;
    @SerializedName("retailer_name")
    @Expose
    private String retailerName;
    @SerializedName("retailer_mobile")
    @Expose
    private String retailerMobile;
    @SerializedName("retailer_email")
    @Expose
    private String retailerEmail;
    @SerializedName("retailer_image")
    @Expose
    private Object retailerImage;
    @SerializedName("retailer_lat")
    @Expose
    private String retailerLat;
    @SerializedName("retailer_long")
    @Expose
    private String retailerLong;
    @SerializedName("order_status_data")
    @Expose
    private String orderStatusData;

    @SerializedName("delivered")
    @Expose
    private String delivered;

    @SerializedName("vehicle_type")
    @Expose
    private String vehicle_type;

    public OrderDTO() {
    }

    public String getTotal_weight() {
        return total_weight;
    }



    @SerializedName("total_weight")
    @Expose
    private String total_weight;

    public String getWeight_unit() {
        return weight_unit;
    }

    @SerializedName("weight_unit")
    @Expose
    private String weight_unit;

    @SerializedName("vehicle")
    @Expose
    private ArrayList<VehicalDTO> vehicle = null;

    public String getRetailer_landmark() {
        return retailer_landmark;
    }

    public void setRetailer_landmark(String retailer_landmark) {
        this.retailer_landmark = retailer_landmark;
    }


  /*  public ArrayList<ItemModel> getItem() {
        return item;
    }

    public void setItem(ArrayList<ItemModel> item) {
        this.item = item;
    }*/

   /* @SerializedName("item")
    @Expose
    private ArrayList<ItemModel> item = null;
*/
    public ArrayList<VehicalDTO> getVehicle() {
        return vehicle;
    }

    public void setVehicle(ArrayList<VehicalDTO> vehicle) {
        this.vehicle = vehicle;
    }

    /*   @SerializedName("distributor_lat")
        @Expose
        private String distributor_lat;
        @SerializedName("distributor_long")
        @Expose
        private String distributor_long;

        @SerializedName("retailer_lat")
        @Expose
        private String retailer_lat;
        @SerializedName("retailer_long")
        @Expose
        private String retailer_long;

    */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getGenOrderId() {
        return genOrderId;
    }

    public void setGenOrderId(Object genOrderId) {
        this.genOrderId = genOrderId;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getOfferJson() {
        return offerJson;
    }

    public void setOfferJson(String offerJson) {
        this.offerJson = offerJson;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOfferDiscountAmount() {
        return offerDiscountAmount;
    }

    public void setOfferDiscountAmount(String offerDiscountAmount) {
        this.offerDiscountAmount = offerDiscountAmount;
    }

    public String getCouponDiscountAmount() {
        return couponDiscountAmount;
    }

    public void setCouponDiscountAmount(String couponDiscountAmount) {
        this.couponDiscountAmount = couponDiscountAmount;
    }

    public String getProductDiscountedPrice() {
        return productDiscountedPrice;
    }

    public void setProductDiscountedPrice(String productDiscountedPrice) {
        this.productDiscountedPrice = productDiscountedPrice;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionMode() {
        return transactionMode;
    }

    public void setTransactionMode(String transactionMode) {
        this.transactionMode = transactionMode;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getRetailerAddress() {
        return retailerAddress;
    }

    public void setRetailerAddress(String retailerAddress) {
        this.retailerAddress = retailerAddress;
    }

    public String getDistributorAddress() {
        return distributorAddress;
    }

    public void setDistributorAddress(String distributorAddress) {
        this.distributorAddress = distributorAddress;
    }

    public String getStoreLat() {
        return storeLat;
    }

    public void setStoreLat(String storeLat) {
        this.storeLat = storeLat;
    }

    public String getStoreLong() {
        return storeLong;
    }

    public void setStoreLong(String storeLong) {
        this.storeLong = storeLong;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistribuorMobile() {
        return distribuorMobile;
    }

    public void setDistribuorMobile(String distribuorMobile) {
        this.distribuorMobile = distribuorMobile;
    }

    public Object getDistributorImage() {
        return distributorImage;
    }

    public void setDistributorImage(Object distributorImage) {
        this.distributorImage = distributorImage;
    }

    public String getRetailerName() {
        return retailerName;
    }

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }

    public String getRetailerMobile() {
        return retailerMobile;
    }

    public void setRetailerMobile(String retailerMobile) {
        this.retailerMobile = retailerMobile;
    }

    public String getRetailerEmail() {
        return retailerEmail;
    }

    public void setRetailerEmail(String retailerEmail) {
        this.retailerEmail = retailerEmail;
    }

    public Object getRetailerImage() {
        return retailerImage;
    }

    public void setRetailerImage(Object retailerImage) {
        this.retailerImage = retailerImage;
    }

    public String getRetailerLat() {
        return retailerLat;
    }

    public void setRetailerLat(String retailerLat) {
        this.retailerLat = retailerLat;
    }

    public String getRetailerLong() {
        return retailerLong;
    }

    public void setRetailerLong(String retailerLong) {
        this.retailerLong = retailerLong;
    }

    public String getOrderStatusData() {
        return orderStatusData;
    }

    public void setOrderStatusData(String orderStatusData) {
        this.orderStatusData = orderStatusData;
    }



    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    /*    public String getDistributor_lat() {
        return distributor_lat;
    }

    public void setDistributor_lat(String distributor_lat) {
        this.distributor_lat = distributor_lat;
    }

    public String getDistributor_long() {
        return distributor_long;
    }

    public void setDistributor_long(String distributor_long) {
        this.distributor_long = distributor_long;
    }

    public String getRetailer_lat() {
        return retailer_lat;
    }

    public void setRetailer_lat(String retailer_lat) {
        this.retailer_lat = retailer_lat;
    }

    public String getRetailer_long() {
        return retailer_long;
    }

    public void setRetailer_long(String retailer_long) {
        this.retailer_long = retailer_long;
    }*/
}

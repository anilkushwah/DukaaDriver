package com.dollop.dukaadriver.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @SerializedName("distributor_landmark")
    @Expose
    private String distributor_landmark;

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
    public String addressId;
    @SerializedName("transaction_id")
    @Expose
    public String transactionId;
    @SerializedName("transaction_mode")
    @Expose
    public String transactionMode;
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

    @SerializedName("shop_name")
    @Expose
    private String shop_name;

    @SerializedName("transaction_status")
    @Expose
    public String transaction_status;



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

    @SerializedName("product_data")
    @Expose
    private ArrayList<ProductDatum> productData = null;

    @SerializedName("order_item")
    @Expose
    public ArrayList<OrderItem> orderItem = null;


    public String getRetailer_landmark() {
        return retailer_landmark;
    }

    public String getDistributor_landmark() {
        return distributor_landmark;
    }


    public ArrayList<VehicalDTO> getVehicle() {
        return vehicle;
    }


    public String getId() {
        return id;
    }



    public Object getGenOrderId() {
        return genOrderId;
    }


    public String getRetailerId() {
        return retailerId;
    }


    public String getDistributorId() {
        return distributorId;
    }


    public String getOfferId() {
        return offerId;
    }


    public String getOfferJson() {
        return offerJson;
    }


    public String getCouponId() {
        return couponId;
    }

   public String getTotalAmount() {
        return totalAmount;
    }

    public String getOfferDiscountAmount() {
        return offerDiscountAmount;
    }


    public String getCouponDiscountAmount() {
        return couponDiscountAmount;
    }


    public String getProductDiscountedPrice() {
        return productDiscountedPrice;
    }


    public String getPaidAmount() {
        return paidAmount;
    }

    public String getAddressId() {
        return addressId;
    }
  public String getTransactionId() {
        return transactionId;
    }


    public String getTransactionMode() {
        return transactionMode;
    }


    public String getOrderStatus() {
        return orderStatus;
    }


    public String getServiceCharge() {
        return serviceCharge;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

     public String getDriverId() {
        return driverId;
    }


    public String getCreateDate() {
        return createDate;
    }
    public String getShopName() {
        return shop_name;
    }

    public String getRetailerAddress() {
        return retailerAddress;
    }



    public String getDistributorAddress() {
        return distributorAddress;
    }


    public String getStoreLat() {
        return storeLat;
    }


    public String getStoreLong() {
        return storeLong;
    }


    public String getItemCount() {
        return itemCount;
    }


    public String getName() {
        return name;
    }



    public String getDistribuorMobile() {
        return distribuorMobile;
    }


    public Object getDistributorImage() {
        return distributorImage;
    }


    public String getRetailerName() {
        return retailerName;
    }



    public String getRetailerMobile() {
        return retailerMobile;
    }


    public String getRetailerEmail() {
        return retailerEmail;
    }



    public Object getRetailerImage() {
        return retailerImage;
    }


    public String getRetailerLat() {
        return retailerLat;
    }


    public String getRetailerLong() {
        return retailerLong;
    }


    public String getOrderStatusData() {
        return orderStatusData;
    }



    public String getDelivered() {
        return delivered;
    }


    public String getVehicle_type() {
        return vehicle_type;
    }


    public ArrayList<ProductDatum> getProductData() {
        return productData;
    }
}

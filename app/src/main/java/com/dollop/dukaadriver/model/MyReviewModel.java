package com.dollop.dukaadriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyReviewModel {
    @SerializedName("rating_id")
    @Expose
    private String ratingId;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("reatiler_id")
    @Expose
    private String reatilerId;
    @SerializedName("order_id")
    @Expose
    private String orderId;
   /* @SerializedName("titile")
    @Expose
    private String titile;*/
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("create_id")
    @Expose
    private String createId;
    @SerializedName("driver_name")
    @Expose
    private String driverName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("retailer_image")
    @Expose
    private String retailerImage;
    @SerializedName("gen_order_id")
    @Expose
    private String genOrderId;

    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getReatilerId() {
        return reatilerId;
    }

    public void setReatilerId(String reatilerId) {
        this.reatilerId = reatilerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

  /*  public String getTitile() {
        return titile;
    }

    public void setTitile(String titile) {
        this.titile = titile;
    }*/

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRetailerImage() {
        return retailerImage;
    }

    public void setRetailerImage(String retailerImage) {
        this.retailerImage = retailerImage;
    }

    public String getGenOrderId() {
        return genOrderId;
    }

    public void setGenOrderId(String genOrderId) {
        this.genOrderId = genOrderId;
    }

}

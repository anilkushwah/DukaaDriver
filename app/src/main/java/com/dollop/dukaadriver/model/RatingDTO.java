package com.dollop.dukaadriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RatingDTO {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("rating")
    @Expose
    private ArrayList<MyReviewModel> rating = null;
    @SerializedName("avg_rating")
    @Expose
    private String avgRating;
    @SerializedName("total_rating")
    @Expose
    private String totalRating;
    @SerializedName("review")
    @Expose
    private Integer review;

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

    public ArrayList<MyReviewModel> getRating() {
        return rating;
    }

    public void setRating(ArrayList<MyReviewModel> rating) {
        this.rating = rating;
    }

    public String getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(String avgRating) {
        this.avgRating = avgRating;
    }

    public String getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(String totalRating) {
        this.totalRating = totalRating;
    }

    public Integer getReview() {
        return review;
    }

    public void setReview(Integer review) {
        this.review = review;
    }

}

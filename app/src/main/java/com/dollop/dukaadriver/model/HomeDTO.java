package com.dollop.dukaadriver.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeDTO implements Serializable {


    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("AllOrder")
    @Expose
    private ArrayList<OrderDTO> allOrder = null;

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

    public ArrayList<OrderDTO> getAllOrder() {
        return allOrder;
    }

    public void setAllOrder(ArrayList<OrderDTO> allOrder) {
        this.allOrder = allOrder;
    }

}

package com.dollop.dukaadriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StaticEarning {

    @SerializedName("x")
    @Expose
    private ArrayList<String> x = null;
    @SerializedName("y")
    @Expose
    private ArrayList<String> y = null;

    public ArrayList<String> getX() {
        return x;
    }

    public void setX(ArrayList<String> x) {
        this.x = x;
    }

    public ArrayList<String> getY() {
        return y;
    }

    public void setY(ArrayList<String> y) {
        this.y = y;
    }

}

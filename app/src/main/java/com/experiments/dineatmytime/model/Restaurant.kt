package com.experiments.dineatmytime.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Restaurant {

    @SerializedName("res_id")
    @Expose
    private String resId;

    @SerializedName("res_name")
    @Expose
    private String resName;

    @SerializedName("res_image")
    @Expose
    private String resImage;


    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getResImage() {
        return resImage;
    }

    public void setResImage(String resImage) {
        this.resImage = resImage;
    }


}

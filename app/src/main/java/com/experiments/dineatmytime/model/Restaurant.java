package com.experiments.dineatmytime.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

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


    public Restaurant(String resId, String resName, String resImage) {
        this.resId = resId;
        this.resName = resName;
        this.resImage = resImage;
    }

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant)) return false;
        Restaurant that = (Restaurant) o;
        return getResId().equals(that.getResId()) &&
                getResName().equals(that.getResName()) &&
                getResImage().equals(that.getResImage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getResId(), getResName(), getResImage());
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "resId='" + resId + '\'' +
                ", resName='" + resName + '\'' +
                ", resImage='" + resImage + '\'' +
                '}';
    }
}

package com.experiments.dineatmytime.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RestaurantData(
        @SerializedName("res_id")
        @Expose
        val resId: Int,

        @SerializedName("res_name")
        @Expose
        val resName: String,

        @SerializedName("res_image")
        @Expose
        val resImage: String
)
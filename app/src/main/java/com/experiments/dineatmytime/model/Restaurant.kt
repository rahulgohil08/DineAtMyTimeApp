package com.experiments.dineatmytime.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Restaurant {
    @SerializedName("res_id")
    @Expose
    var resId: String? = null

    @SerializedName("res_name")
    @Expose
    var resName: String? = null

    @SerializedName("res_image")
    @Expose
    var resImage: String? = null
}
package com.experiments.dineatmytime.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("cust_id")
        @Expose
        val id: Int,

        @SerializedName("cust_name")
        @Expose
        val name: String,

        @SerializedName("cust_email")
        @Expose
        val email: String,

        @SerializedName("cust_contact")
        @Expose
        val contact: String,

        @SerializedName("cust_address")
        @Expose
        val address: String,

        @SerializedName("cust_image")
        @Expose
        val profileImage: String?,

        @SerializedName("registration_time")
        @Expose
        val created: String,
)
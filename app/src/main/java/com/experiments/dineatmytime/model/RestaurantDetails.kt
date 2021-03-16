package com.experiments.dineatmytime.model


import com.google.gson.annotations.SerializedName

data class RestaurantDetails(
        @SerializedName("registration_status")
        val registrationStatus: String,

        @SerializedName("registration_time")
        val registrationTime: String,

        @SerializedName("res_address")
        val resAddress: String,

        @SerializedName("res_contact")
        val resContact: String,

        @SerializedName("res_email")
        val resEmail: String,

        @SerializedName("res_id")
        val resId: String,

        @SerializedName("res_image")
        val resImage: String,

        @SerializedName("res_name")
        val resName: String,

        @SerializedName("res_password")
        val resPassword: String,

        @SerializedName("seat_image")
        val seatImage: String,

        @SerializedName("status")
        val status: String,
)
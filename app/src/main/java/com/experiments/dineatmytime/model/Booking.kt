package com.experiments.dineatmytime.model


import com.google.gson.annotations.SerializedName

data class Booking(
        @SerializedName("amount")
        val amount: String,

        @SerializedName("booking_date_time")
        val bookingDateTime: String,

        @SerializedName("cust_name")
        val custName: String,

        @SerializedName("menu")
        val menu: String,

        @SerializedName("order_id")
        val orderId: String,

        @SerializedName("res_name")
        val resName: String,

        @SerializedName("table_no")
        val tableNo: String,
)
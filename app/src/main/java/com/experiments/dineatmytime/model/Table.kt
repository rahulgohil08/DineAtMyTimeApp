package com.experiments.dineatmytime.model


import com.google.gson.annotations.SerializedName

data class Table(
        @SerializedName("res_id")
        val resId: String,

        @SerializedName("table_id")
        val tableId: Int,

        @SerializedName("table_no")
        val tableNo: String,
)
package com.experiments.dineatmytime.model


import com.google.gson.annotations.SerializedName

data class Menu(
    @SerializedName("amount")
    val amount: Int,

    @SerializedName("menu_id")
    val menuId: String,

    @SerializedName("menu_name")
    val menuName: String,

    @SerializedName("res_id")
    val resId: String,

    var isSelected:Boolean = false,
)
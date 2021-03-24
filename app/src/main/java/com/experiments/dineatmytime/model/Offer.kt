package com.experiments.dineatmytime.model


import com.google.gson.annotations.SerializedName

data class Offer(
    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("discount_amount")
    val discountAmount: Int,

    @SerializedName("is_active")
    val isActive: Int,

    @SerializedName("min_purchase")
    val minPurchase: Int,

    @SerializedName("offer_id")
    val offerId: Int,

    @SerializedName("promo_code")
    val promoCode: String,

    @SerializedName("res_id")
    val resId: Int
)
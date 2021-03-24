package com.experiments.dineatmytime.network

import com.experiments.dineatmytime.model.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ServerResponse {
    @SerializedName("error")
    @Expose
    var error: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("user")
    @Expose
    var user: User? = null

    @SerializedName("restaurants")
    @Expose
    var restaurantList: List<Restaurant>? = null

    @SerializedName("restaurant")
    @Expose
    var restaurantDetails: RestaurantDetails? = null

    @SerializedName("menus")
    @Expose
    var menuList: List<Menu>? = null

    @SerializedName("tables")
    @Expose
    var tableList: List<Table>? = null

    @SerializedName("bookings")
    @Expose
    var bookingList: List<Booking>? = null

    @SerializedName("offers")
    @Expose
    var offerList: List<Offer>? = null
}
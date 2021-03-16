package com.experiments.dineatmytime.network

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    companion object {
        const val apiUrl = "Api.php?apicall="
    }


    /*------------------------------------ Restaurant List ------------------------------------*/

    @FormUrlEncoded
    @POST(apiUrl + "get_restaurant_details")
    suspend fun getRestaurantDetails(
            @Field("res_id") resId: Int,
    ): ServerResponse


    /*------------------------------------ Table List ------------------------------------*/

    @FormUrlEncoded
    @POST(apiUrl + "get_restaurant_tables")
    suspend fun getTables(
            @Field("res_id") resId: Int,
    ): ServerResponse


    /*------------------------------------ Menu List ------------------------------------*/

    @FormUrlEncoded
    @POST(apiUrl + "get_restaurant_menus")
    suspend fun getMenus(
            @Field("res_id") resId: Int,
    ): ServerResponse


    /*------------------------------------ Place Order ------------------------------------*/

    @FormUrlEncoded
    @POST(apiUrl + "place_order")
    suspend fun placeOrder(
            @Field("cust_id") custId: Int,
            @Field("res_id") resId: Int,
            @Field("table_id") tableId: Int,
            @Field("amount") amount: Int,
            @Field("menu") menu: String,
            @Field("datetime") datetime: String,
    ): ServerResponse


    /*------------------------------------ Booking List ------------------------------------*/

    @FormUrlEncoded
    @POST(apiUrl + "get_booking_history")
    suspend fun getBookings(
            @Field("cust_id") custId: Int,
    ): ServerResponse


}
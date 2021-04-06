package com.experiments.dineatmytime.network

import com.experiments.dineatmytime.network.ApiService.Companion.apiUrl
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    companion object {
        const val apiUrl = "Api.php?apicall="
    }


    /*------------------------------------User Profile  ------------------------------------*/

    @FormUrlEncoded
    @POST(apiUrl + "get_user_profile")
    suspend fun getUserProfile(
            @Field("cust_id") custId: Int,
    ): ServerResponse


/*------------------------------------ Edit User Profile  ------------------------------------*/

    @FormUrlEncoded
    @POST(apiUrl + "edit_user_profile")
    suspend fun editUserProfile(
            @Field("cust_name") name: String,
            @Field("cust_address") address: String,
            @Field("cust_id") custId: Int,
    ): ServerResponse


/*------------------------------------ Edit Profile Image ------------------------------------*/

    @FormUrlEncoded
    @POST(apiUrl + "update_profile_image")
    suspend fun updateProfileImage(
            @Field("profile_image") profileImage: String,
            @Field("cust_id") custId: Int,
    ): ServerResponse


/*------------------------------------ Change Password---------------------------------------------------*/

    @FormUrlEncoded
    @POST(apiUrl + "change_password")
    suspend fun changePassword(
            @Field("old") oldPassword: String,
            @Field("newpwd") newPassword: String,
            @Field("cust_id") custId: Int,
    ): ServerResponse


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


/*------------------------------------ Is Already Booked ------------------------------------*/

    @FormUrlEncoded
    @POST(apiUrl + "is_already_slot")
    suspend fun isAlreadySlot(
            @Field("res_id") resId: Int,
            @Field("table_id") tableId: Int,
            @Field("datetime") datetime: String,
    ): ServerResponse


/*------------------------------------ Place Order ------------------------------------*/

    @FormUrlEncoded
    @POST(apiUrl + "place_order")
    suspend fun placeOrder(
            @Field("cust_id") custId: Int,
            @Field("res_id") resId: Int,
            @Field("table_id") tableId: Int,
            @Field("amount") amount: Int,
            @Field("discount") discount: Int,
            @Field("menu") menu: String,
            @Field("datetime") datetime: String,
    ): ServerResponse


/*------------------------------------ Booking List ------------------------------------*/

    @FormUrlEncoded
    @POST(apiUrl + "get_booking_history")
    suspend fun getBookings(
            @Field("cust_id") custId: Int,
    ): ServerResponse


/*------------------------------------ Offer List ------------------------------------*/

    @FormUrlEncoded
    @POST(apiUrl + "get_offer_list")
    suspend fun getOffers(
            @Field("res_id") resId: Int,
    ): ServerResponse


}
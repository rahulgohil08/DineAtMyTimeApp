package com.experiments.dineatmytime.network;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    String my_url = "Api.php?apicall=";

    /*--------------------------- Login --------------------------------*/

    @FormUrlEncoded
    @POST(my_url + "cust_login")
    Call<ServerResponse> login(
            @Field("cust_email") String email,
            @Field("cust_password") String password
    );


    /*----------------------------- Register ----------------------------------*/

    @FormUrlEncoded
    @POST(my_url + "cust_register")
    Call<ServerResponse> register(
            @Field("cust_name") String cName,
            @Field("cust_email") String cEmail,
            @Field("cust_password") String cPwd,
            @Field("cust_contact") String cphone,
            @Field("cust_address") String caddress
    );


    /*--------------------------- GET Restaurant List --------------------------------*/

    @FormUrlEncoded
    @POST(my_url + "get_restaurant_list")
    Call<ServerResponse> getRestaurantList(
            @Field("cust_id") int id
    );

}
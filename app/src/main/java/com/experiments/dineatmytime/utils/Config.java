package com.experiments.dineatmytime.utils;

import android.content.Context;
import android.widget.Toast;

public class Config {

    public static int user_id = -1;

    public static String url = "http://192.168.0.140/dine_my_time/";

//    http://192.168.0.112/dine_my_time/restaurants/95502ebb7eea5108dc43ed65970151a8.jpg

    public static String restaurantImageUrl = url + "restaurants/";
    public static String restaurantSeatUrl = url + "seat_map/";

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}

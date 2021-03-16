package com.experiments.dineatmytime.ui.restaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.experiments.dineatmytime.databinding.ActivityRestaurantDetailsBinding
import com.experiments.dineatmytime.model.RestaurantDetails
import com.experiments.dineatmytime.network.ApiService
import com.experiments.dineatmytime.network.RetroClass
import com.experiments.dineatmytime.utils.Config
import kotlinx.coroutines.launch

class RestaurantDetailsActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "RestaurantDetails"
    }

    private lateinit var binding: ActivityRestaurantDetailsBinding
    private val context = this

    private var resId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resId = intent.getIntExtra("res_id", 0)

        getRestaurantDetails()
        handleToolbar()
        clickListener()

    }


    private fun clickListener() {

        binding.btnBookTable.setOnClickListener {
            val intent = Intent(context, BookingActivity::class.java).also {
                it.putExtra("res_id", resId)
            }
            startActivity(intent)
        }

    }


    /*--------------------------------- Handle Toolbar --------------------------------*/

    private fun handleToolbar() {
        binding.includedToolbar.title.text = "Restaurant Details"
        binding.includedToolbar.backBtn.setOnClickListener { finish() }
    }


    /*--------------------------------- Restaurant Details --------------------------------*/


    private fun getRestaurantDetails() {
        lifecycleScope.launch {
            try {

                val apiInterface: ApiService = RetroClass.createService(ApiService::class.java)
                val response = apiInterface.getRestaurantDetails(resId)

                Log.d(TAG, "getRestaurantDetails: $response")

                handleRestaurantDetails(response.restaurantDetails)

            } catch (exception: Exception) {
                Config.showToast(context, exception.message.toString())
            }
        }
    }


    private fun handleRestaurantDetails(restaurantDetails: RestaurantDetails?) {

        restaurantDetails?.let {
            binding.apply {
                tvRestaurantName.text = restaurantDetails.resName
                tvDesc.text = restaurantDetails.resAddress

                Glide.with(context)
                        .load(Config.restaurantImageUrl + restaurantDetails.resImage)
                        .into(restaurantImage)
            }
        }
    }
}
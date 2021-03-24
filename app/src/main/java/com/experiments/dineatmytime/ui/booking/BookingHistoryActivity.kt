package com.experiments.dineatmytime.ui.booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.experiments.dineatmytime.adapters.BookingHistoryAdapter
import com.experiments.dineatmytime.databinding.ActivityBookingBinding
import com.experiments.dineatmytime.databinding.ActivityBookingHistoryBinding
import com.experiments.dineatmytime.model.Booking
import com.experiments.dineatmytime.model.Table
import com.experiments.dineatmytime.network.ApiService
import com.experiments.dineatmytime.network.RetroClass
import com.experiments.dineatmytime.ui.restaurant.BookingActivity
import com.experiments.dineatmytime.utils.Config
import kotlinx.coroutines.launch

class BookingHistoryActivity : AppCompatActivity() {


    companion object {
        private const val TAG = "BookingActivity"
    }

    private lateinit var binding: ActivityBookingHistoryBinding
    private val context = this

    private lateinit var bookingHistoryAdapter: BookingHistoryAdapter
    private val bookingList = mutableListOf<Booking>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityBookingHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleToolbar()
        init()
        getBookings()
    }


    /*--------------------------------- Handle Toolbar --------------------------------*/

    private fun handleToolbar() {
        binding.includedToolbar.title.text = "Booking History"
        binding.includedToolbar.backBtn.setOnClickListener { finish() }
    }

    private fun init() {

        bookingHistoryAdapter = BookingHistoryAdapter(bookingList)
        binding.recyclerView.adapter = bookingHistoryAdapter
    }


    private fun getBookings() {
        lifecycleScope.launch {
            try {

                val apiInterface: ApiService = RetroClass.createService(ApiService::class.java)
                val response = apiInterface.getBookings(Config.user_id)

                Log.d(TAG, "getRestaurantDetails: $response")

                bookingList.clear()
                bookingList.addAll(response.bookingList!!)
                bookingHistoryAdapter.notifyDataSetChanged()

            } catch (exception: Exception) {
                Config.showToast(context, exception.message.toString())
            }
        }
    }

}
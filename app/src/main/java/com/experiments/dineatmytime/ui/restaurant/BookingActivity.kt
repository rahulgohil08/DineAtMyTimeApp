package com.experiments.dineatmytime.ui.restaurant

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.experiments.dineatmytime.R
import com.experiments.dineatmytime.adapters.ChipAdapter
import com.experiments.dineatmytime.databinding.ActivityBookingBinding
import com.experiments.dineatmytime.databinding.LayoutSelectProductsDialogBinding
import com.experiments.dineatmytime.model.Menu
import com.experiments.dineatmytime.model.RestaurantDetails
import com.experiments.dineatmytime.model.Table
import com.experiments.dineatmytime.network.ApiService
import com.experiments.dineatmytime.network.RetroClass
import com.experiments.dineatmytime.ui.payment.PaymentActivity
import com.experiments.dineatmytime.utils.Config
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BookingActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "BookingActivity"
    }

    private lateinit var binding: ActivityBookingBinding
    private val context = this

    private val menuList = mutableListOf<Menu>()
    private val tableList = mutableListOf<Table>()

    private lateinit var alertDialog: AlertDialog


    private var resId = 0
    private var tableId = 0

    private var totalBill = 0

    private val chipAdapter = ChipAdapter()


    private lateinit var dateTime: String

    private lateinit var date: String
    private lateinit var time: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resId = intent.getIntExtra("res_id", 0)

        getRestaurantDetails()
        handleToolbar()
        clickListener()
        getTables()
        getMenus()
    }


    private fun clickListener() {


        binding.edtMenu.editText!!.setOnClickListener {
            displaySelectMenuDialog()
        }

        binding.edtTable.setOnItemClickListener { _, _, position, _ ->
            tableId = tableList.get(position).tableId
        }

        binding.btnBookTable.setOnClickListener {

            if (totalBill <= 0) {
                Config.showToast(context, "Please Select Menu")
                return@setOnClickListener
            }


            val intent = Intent(context, PaymentActivity::class.java).also {
                it.putExtra("table_id", tableId)
                it.putExtra("res_id", resId)
                it.putExtra("amount", totalBill)
                it.putExtra("datetime", dateTime)
                it.putExtra("menu", binding.edtMenu.editText!!.text.toString().trim())
            }
            startActivity(intent)

        }


        binding.edtDate.setOnClickListener {
            displayDatePicker()
        }

    }


    /*--------------------------------- Handle Toolbar --------------------------------*/

    private fun handleToolbar() {
        binding.includedToolbar.title.text = "Booking"
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


    /*--------------------------------- Tables List --------------------------------*/


    private fun getTables() {
        lifecycleScope.launch {
            try {

                val apiInterface: ApiService = RetroClass.createService(ApiService::class.java)
                val response = apiInterface.getTables(resId)

                tableList.clear()
                tableList.addAll(response.tableList!!)


                val lData: ArrayList<String> = ArrayList()

                for (myData in tableList) {
                    lData.add(myData.tableNo)
                }

                val arrayAdapter = ArrayAdapter(
                        context,
                        R.layout.dropdown_item,
                        lData
                )

                binding.edtTable.setText(lData[0])
                tableId = tableList[0].tableId
                binding.edtTable.setAdapter(arrayAdapter)

            } catch (exception: Exception) {
                Config.showToast(context, exception.message.toString())
            }
        }
    }


    private fun handleRestaurantDetails(restaurantDetails: RestaurantDetails?) {

        restaurantDetails?.let {
            binding.apply {

                Glide.with(context)
                        .load(Config.restaurantSeatUrl + restaurantDetails.seatImage)
                        .into(seatImage)
            }
        }
    }


    /*----------------------------------------- Select Products Dialog -------------------------------*/

    private fun displaySelectMenuDialog() {

        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = layoutInflater


        val dialogBinding = LayoutSelectProductsDialogBinding.inflate(inflater)
        dialogBuilder.setView(dialogBinding.root)
        alertDialog = dialogBuilder.create()
        alertDialog.show()


        dialogBinding.recyclerView.adapter = chipAdapter
        chipAdapter.submitList(menuList)

        var products = ""

        dialogBinding.btnOk.setOnClickListener {

            totalBill = 0

            if (menuList.isNotEmpty()) {

                menuList.forEach {
                    Log.d(TAG, "handleLoop: $it")

                    if (it.isSelected) {
                        products = if (products.isEmpty()) {
                            it.menuName
                        } else {
                            products + ", " + it.menuName
                        }

                        totalBill += it.amount
                    }

                }
            }


            binding.btnBookTable.text = "Pay Rs. $totalBill"

            binding.edtMenu.editText!!.setText(products)
            binding.edtMenu.editText!!.setSelection(binding.edtMenu.editText!!.length())
            alertDialog.dismiss()
        }

    }


    /*----------------------------------------- Fetch Menus -------------------------------*/

    private fun getMenus() {
        lifecycleScope.launch {
            try {

                val apiInterface: ApiService = RetroClass.createService(ApiService::class.java)
                val response = apiInterface.getMenus(resId)

                menuList.clear()
                menuList.addAll(response.menuList!!)

            } catch (exception: Exception) {
                Config.showToast(context, exception.message.toString())
            }
        }

    }

    /*----------------------------------------- Display Date Picker -------------------------------*/

    private fun displayDatePicker() {

        val materialDateBuilder: MaterialDatePicker.Builder<*> =
                MaterialDatePicker.Builder.datePicker()
        materialDateBuilder.setTitleText("Select Date")

        val materialDatePicker = materialDateBuilder.build()

        materialDatePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")

        materialDatePicker.addOnPositiveButtonClickListener {
            var spf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            var newDate: Date? = null
            try {
                newDate = spf.parse(materialDatePicker.headerText)

                spf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                date = spf.format(newDate ?: "")

                displayTimePicker()

            } catch (e: ParseException) {
                e.printStackTrace()
                Config.showToast(context, e.message)
            }

        }

    }


    /*----------------------------------------- Display  Time Picker -------------------------------*/

    private fun displayTimePicker() {

        val materialTimePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

        materialTimePicker.addOnPositiveButtonClickListener {
            val newHour: Int = materialTimePicker.hour
            val newMinute: Int = materialTimePicker.minute

            time = if (newHour < 9) {
                "0$newHour:$newMinute"
            } else {
                "$newHour:$newMinute"
            }

            dateTime = "$date $time"

            binding.edtDate.setText(dateTime)
        }

        materialTimePicker.show(supportFragmentManager, "fragment_tag")

    }

}
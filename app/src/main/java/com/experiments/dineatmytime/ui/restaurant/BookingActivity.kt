package com.experiments.dineatmytime.ui.restaurant

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.experiments.dineatmytime.R
import com.experiments.dineatmytime.adapters.ChipAdapter
import com.experiments.dineatmytime.databinding.ActivityBookingBinding
import com.experiments.dineatmytime.databinding.LayoutSelectProductsDialogBinding
import com.experiments.dineatmytime.model.Menu
import com.experiments.dineatmytime.model.Offer
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
    private val offerList = mutableListOf<Offer>()

    private val minimumPurchase = 500
    private var discount = 0

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
        getOffers()
    }


    private fun clickListener() {


        binding.seatImage.setOnClickListener {
            handleZoomedImage()
        }

        binding.edtMenu.editText!!.setOnClickListener {
            displaySelectMenuDialog()
        }

        binding.edtTable.setOnItemClickListener { _, _, position, _ ->
            tableId = tableList[position].tableId
        }

        binding.edtOffer.setOnItemClickListener { _, _, position, _ ->
            discount = offerList[position].discountAmount
            manageOfferDisplay(true)
        }

        binding.btnBookTable.setOnClickListener {

            if (totalBill <= 0) {
                Config.showToast(context, "Please Select Menu")
                return@setOnClickListener
            }



            checkIfTableBooked()


        }



        binding.edtDate.setOnClickListener {
            displayDatePicker()
        }

    }

    /*--------------------------------- Handle Zoomed Image --------------------------------*/


    private fun handleZoomedImage() {

        binding.zoomedImage.isVisible = !binding.zoomedImage.isVisible
        binding.scrollView.isVisible = !binding.zoomedImage.isVisible

    }

    /*--------------------------------- Check Whether Table Available --------------------------------*/

    private fun checkIfTableBooked() {

        lifecycleScope.launch {
            try {

                val apiInterface: ApiService = RetroClass.createService(ApiService::class.java)
                val response = apiInterface.isAlreadySlot(
                        resId = resId,
                        tableId = tableId,
                        datetime = dateTime
                )

                Log.d(TAG, "getRestaurantDetails: $response")

                if (response.error!!) {
                    Config.showToast(context, response.message!!)
                } else {

                    val intent = Intent(context, PaymentActivity::class.java).also {
                        it.putExtra("table_id", tableId)
                        it.putExtra("res_id", resId)
                        it.putExtra("amount", totalBill)
                        it.putExtra("discount", discount)
                        it.putExtra("datetime", dateTime)
                        it.putExtra("menu", binding.edtMenu.editText!!.text.toString().trim())
                    }
                    startActivity(intent)
                }

            } catch (exception: Exception) {
                Config.showToast(context, exception.message.toString())
            }
        }


    }


    private fun manageOfferDisplay(hasDiscount: Boolean = false) {
        binding.edtOfferContainer.isVisible = totalBill >= minimumPurchase

        if (totalBill >= minimumPurchase) {

            discount = if (!hasDiscount) {
                offerList[0].discountAmount
            } else {
                discount
            }

            binding.btnBookTable.text = "Pay Rs. ${totalBill - discount} (Discount $discount)"

        } else {
            discount = 0
            binding.btnBookTable.text = "Pay Rs. $totalBill"
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

                Glide.with(context)
                        .load(Config.restaurantSeatUrl + restaurantDetails.seatImage)
                        .into(zoomedImage)
            }
        }
    }


    /*--------------------------------- Offer List --------------------------------*/


    private fun getOffers() {
        lifecycleScope.launch {
            try {

                val apiInterface: ApiService = RetroClass.createService(ApiService::class.java)
                val response = apiInterface.getOffers(resId)

                offerList.clear()
                offerList.addAll(response.offerList!!)


                val lData: ArrayList<String> = ArrayList()

                for (myData in offerList) {
                    lData.add("${myData.promoCode} - Rs. ${myData.discountAmount}")
                }

                val arrayAdapter = ArrayAdapter(
                        context,
                        R.layout.dropdown_item,
                        lData
                )

                binding.edtOffer.setText(lData[0])
                discount = offerList[0].discountAmount
                binding.edtOffer.setAdapter(arrayAdapter)

            } catch (exception: Exception) {
                Config.showToast(context, exception.message.toString())
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

            manageOfferDisplay()



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

            try {

                val spf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                date = spf.format(it)

                displayTimePicker()

            } catch (e: ParseException) {
                Log.d(TAG, "displayDatePicker: ${e.message}")
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


    override fun onBackPressed() {

        if (binding.zoomedImage.isVisible) {
            handleZoomedImage()
            return
        }
        super.onBackPressed()
    }

}
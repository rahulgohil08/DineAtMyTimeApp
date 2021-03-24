package com.experiments.dineatmytime.ui.payment

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.experiments.dineatmytime.databinding.ActivityPaymentBinding
import com.experiments.dineatmytime.network.ApiService
import com.experiments.dineatmytime.network.RetroClass
import com.experiments.dineatmytime.ui.booking.BookingHistoryActivity
import com.experiments.dineatmytime.utils.Config
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class PaymentActivity : AppCompatActivity(), PaymentResultListener {

    companion object {
        private const val TAG = "PaymentActivity"
    }

    private lateinit var binding: ActivityPaymentBinding
    private val context = this

    private var tableId = 0
    private var resId = 0
    private var amount = 0
    private var discount = 0
    private lateinit var datetime: String
    private lateinit var menu: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)


        init()

    }


    private fun init() {


        tableId = intent.getIntExtra("table_id", 0)
        resId = intent.getIntExtra("res_id", 0)
        amount = intent.getIntExtra("amount", 0)
        discount = intent.getIntExtra("discount", 0)
        datetime = intent.getStringExtra("datetime").toString()
        menu = intent.getStringExtra("menu").toString()


        doPayment(amount)
    }


    private fun doPayment(amount: Int) {
        //Initialize RazorPay Checkout
        val checkout = Checkout()

        //set key id
        checkout.setKeyID("rzp_test_Cf3sVOF5OHtsqX")


        //initialize json object
        val obj = JSONObject()
        try {
            //put name
            obj.put("name", "Dine My Time")
            //put description
            obj.put("description", "Test Payment")
            //put theme color
            obj.put("theme.color", "#0093DD")
            //put currency unit
            obj.put("currency", "INR")
            //put amount
            obj.put("amount", (amount * 100))

            checkout.open(context, obj)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(s: String?) {

        /* //Initialize alert dialog
         val builder = AlertDialog.Builder(context)

         //Set title
         builder.setTitle("Payment ID")

         //Set message
         builder.setMessage(s)

         //Show alert dialog
         builder.show()*/



        sendOrderDataTOServer()
    }

    override fun onPaymentError(i: Int, s: String?) {
        //Display Toast
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
        finish()
    }


/*----------------------------- Send Order Data to Server ---------------------*/

    private fun sendOrderDataTOServer() {

        lifecycleScope.launch {
            try {

                val apiInterface: ApiService = RetroClass.createService(ApiService::class.java)
                val response = apiInterface.placeOrder(
                        resId = resId,
                        tableId = tableId,
                        custId = Config.user_id,
                        amount = amount,
                        menu = menu,
                        datetime = datetime,
                        discount = discount
                )


                Config.showToast(context, response.message)
                openActivity(BookingHistoryActivity::class.java)
                finish()

            } catch (exception: Exception) {
                Config.showToast(context, exception.message.toString())
                finish()
            }


        }
    }


    private fun openActivity(aclass: Class<*>) {
        val intent = Intent(context, aclass)
        startActivity(intent)
    }


}
package com.experiments.dineatmytime.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.experiments.dineatmytime.databinding.ActivityChangePasswordBinding
import com.experiments.dineatmytime.network.ApiService
import com.experiments.dineatmytime.network.RetroClass
import com.experiments.dineatmytime.utils.Config
import kotlinx.coroutines.launch

class ChangePasswordActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ChangePasswordActivity"
    }

    private lateinit var binding: ActivityChangePasswordBinding
    private val context = this

    private lateinit var currentPassword: String
    private lateinit var password: String
    private lateinit var confirmPassword: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)



        handleToolbar()
        clickListener()

    }


    private fun clickListener() {

        binding.btnSubmit.setOnClickListener {

            if (!validateCurrentPassword() or !validatePassword() or !validateConfirmPassword()) {
                return@setOnClickListener
            }

            updatePassword()

        }


    }


    /*--------------------------------- Handle Toolbar --------------------------------*/

    private fun handleToolbar() {
        binding.includedToolbar.title.text = "Change Password"
        binding.includedToolbar.backBtn.setOnClickListener { finish() }
    }


    /*----------------------------------------- do Validations -------------------------------*/


    private fun validateCurrentPassword(): Boolean {

        currentPassword = binding.edtCurrentPassword.editText!!.text.toString().trim()
        return when {
            currentPassword.isEmpty() -> {
                binding.edtCurrentPassword.error = "Field can't be empty"
                false
            }
            else -> {
                binding.edtCurrentPassword.error = null
                true
            }
        }
    }


    private fun validatePassword(): Boolean {
        password = binding.edtNewPassword.editText!!.text.toString().trim()
        return when {
            password.isEmpty() -> {
                binding.edtNewPassword.error = "Field can't be empty"
                false
            }
            else -> {
                binding.edtNewPassword.error = null
                true
            }
        }
    }


    private fun validateConfirmPassword(): Boolean {
        confirmPassword = binding.edtConfirmPassword.editText!!.text.toString().trim()
        return when {
            confirmPassword.isEmpty() -> {
                binding.edtConfirmPassword.error = "Field can't be empty"
                false
            }
            confirmPassword != password -> {
                binding.edtConfirmPassword.error = "Please Confirm Password"
                false
            }
            else -> {
                binding.edtConfirmPassword.error = null
                true
            }
        }
    }

    /*---------------------------------Update Password --------------------------------*/


    private fun updatePassword() {
        lifecycleScope.launch {
            try {

                val apiInterface: ApiService = RetroClass.createService(ApiService::class.java)
                val response = apiInterface.changePassword(
                        custId = Config.user_id,
                        oldPassword = currentPassword,
                        newPassword = password,
                )


                Config.showToast(context, response.message!!)

                Log.d(TAG, "Update Password: $response")

            } catch (exception: Exception) {
                Config.showToast(context, exception.message.toString())
            }
        }
    }


}
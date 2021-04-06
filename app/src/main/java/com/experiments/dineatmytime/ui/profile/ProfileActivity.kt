package com.experiments.dineatmytime.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.experiments.dineatmytime.R
import com.experiments.dineatmytime.databinding.ActivityProfileBinding
import com.experiments.dineatmytime.model.User
import com.experiments.dineatmytime.network.ApiService
import com.experiments.dineatmytime.network.RetroClass
import com.experiments.dineatmytime.utils.Config
import com.experiments.dineatmytime.utils.SharedPrefManager
import com.experiments.dineatmytime.utils.getStringImage
import kotlinx.coroutines.launch
import java.io.IOException

class ProfileActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ProfileActivity"
        const val PROFILE_IMAGE = 101
    }

    private lateinit var binding: ActivityProfileBinding
    private val context = this

    private lateinit var name: String
    private lateinit var address: String
    private lateinit var sharedPrefManager: SharedPrefManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefManager = SharedPrefManager(context)


        handleToolbar()
        getProfile()
        clickListener()

    }


    private fun clickListener() {

        binding.btnSubmit.setOnClickListener {
            name = binding.edtFullName.editText!!.text.toString().trim()
            address = binding.edtAddress.editText!!.text.toString().trim()


            if (name.isEmpty() || address.isEmpty()) {
                Config.showToast(context, "Name and Address is required")
                return@setOnClickListener
            }

            binding.loadingSpinner.isVisible = true
            updateProfileDetails()

        }


        binding.imgEdit.setOnClickListener {
            chooseImage()
        }
    }


    /*------------------------------------- Choose Image -------------------------------------------*/


    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
                Intent.createChooser(intent, "Select Image"),
                PROFILE_IMAGE
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PROFILE_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val filePath = data.data
            try {
                val bitmap =
                        MediaStore.Images.Media.getBitmap(contentResolver, filePath)

                binding.profileImage.apply {
                    setImageBitmap(bitmap)
                }

                updateProfileImage(bitmap.getStringImage())

            } catch (e: IOException) {
                e.printStackTrace()
                Log.d(TAG, "onActivityResult: ${e.message}")
            }
        }
    }


    /*--------------------------------- Handle Toolbar --------------------------------*/

    private fun handleToolbar() {
        binding.includedToolbar.title.text = "Profile"
        binding.includedToolbar.backBtn.setOnClickListener { finish() }
    }


    /*--------------------------------- Profile Details --------------------------------*/


    private fun getProfile() {
        lifecycleScope.launch {
            try {

                val apiInterface: ApiService = RetroClass.createService(ApiService::class.java)
                val response = apiInterface.getUserProfile(Config.user_id)

                Log.d(TAG, "Profile: $response")

                binding.loadingSpinner.isVisible = false
                handleProfileData(response.user)

            } catch (exception: Exception) {
                Config.showToast(context, exception.message.toString())
            }
        }
    }


    private fun handleProfileData(user: User?) {

        user?.let {
            binding.apply {
                edtFullName.editText!!.setText(user.name)
                edtAddress.editText!!.setText(user.address)
                edtEmail.editText!!.setText(user.email)
                edtMobileNo.editText!!.setText(user.contact)


                sharedPrefManager.setString("name", user.name)
                sharedPrefManager.setString("profile_image", user.profileImage)

                binding.profileImage.load(Config.profileImageUrl + user.profileImage) {
                    crossfade(true)
                    placeholder(R.drawable.ic_profile)
                    error(R.drawable.ic_profile)
                    transformations(
                            CircleCropTransformation()
                    )
                }

            }
        } ?: kotlin.run {
            Config.showToast(context, "User is NULL")
        }
    }


    /*---------------------------------Update Profile Details --------------------------------*/


    private fun updateProfileDetails() {
        lifecycleScope.launch {
            try {

                val apiInterface: ApiService = RetroClass.createService(ApiService::class.java)
                val response = apiInterface.editUserProfile(
                        custId = Config.user_id,
                        address = address,
                        name = name,
                )

                Log.d(TAG, "Update Profile: $response")

                getProfile()

            } catch (exception: Exception) {
                Config.showToast(context, exception.message.toString())
            }
        }
    }


    /*---------------------------------Update Profile Image  --------------------------------*/


    private fun updateProfileImage(stringImage: String) {
        lifecycleScope.launch {
            try {

                val apiInterface: ApiService = RetroClass.createService(ApiService::class.java)
                val response = apiInterface.updateProfileImage(
                        custId = Config.user_id,
                        profileImage = stringImage,
                )

                Log.d(TAG, "Update Profile: $response")

                getProfile()

            } catch (exception: Exception) {
                Config.showToast(context, exception.message.toString())
            }
        }
    }


}
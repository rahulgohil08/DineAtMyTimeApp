package com.experiments.dineatmytime.utils

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

/*------------------------------------- Bitmap to String ------------------------------*/

fun Bitmap.getStringImage(): String {
    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 50, baos)
    val imageBytes = baos.toByteArray()
    return Base64.encodeToString(imageBytes, Base64.DEFAULT)
}
package fr.rnbe.android.core.extensions

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import java.util.*

fun Context?.sendSms(phoneNumber: String) {
    this?.apply {
        if (phoneNumber.isBlank()) {
            Log.w(TAG, "Trying to send sms to invalid number")
            return
        }

        val number = if (!phoneNumber.startsWith("sms:")) "sms:$phoneNumber" else phoneNumber
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(number))
        startActivity(intent)
    } ?: Log.w("ContextExtKt", "Trying to send sms with invalid context")
}

fun Context?.launchVocalCall(phoneNumber: String) {
    this?.apply {
        if (phoneNumber.isBlank()) {
            Log.w(TAG, "Trying to call an invalid number")
            return
        }

        val number = if (!phoneNumber.startsWith("tel:")) "tel:$phoneNumber" else phoneNumber
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse(number))
            startActivity(intent)
        } catch (e: Exception) {
            AlertDialog.Builder(this)
                    .setTitle(number.replace("tel:", ""))
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
        }
    } ?: Log.w("ContextExtKt", "Trying to launchVocalCall with invalid context")
}



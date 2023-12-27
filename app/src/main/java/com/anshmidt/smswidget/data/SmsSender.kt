package com.anshmidt.smswidget.data

import android.telephony.SmsManager
import android.util.Log

class SmsSender {
    fun sendSMS() {
        val phoneNumber = "9011"
        val message = "A90"

        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Log.d("SmsWidget", "SMS sent successfully")
        } catch (e: Exception) {
            Log.d("SmsWidget", "Failed to send SMS: $e")
        }
    }
}
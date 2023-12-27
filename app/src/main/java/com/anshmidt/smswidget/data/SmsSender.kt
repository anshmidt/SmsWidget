package com.anshmidt.smswidget.data

import android.telephony.SmsManager
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SmsSender {
    fun sendSMS(): Flow<Result<Unit>> = flow {
        try {
            val phoneNumber = "9011"
            val message = "A90"
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Log.d("SmsWidget", "SMS sent successfully")
            emit(Result.success(Unit))
        } catch (e: Exception) {
            Log.d("SmsWidget", "Failed to send SMS: $e")
            emit(Result.failure(e))
        }
    }
}
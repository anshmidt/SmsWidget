package com.anshmidt.smswidget.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import com.anshmidt.smswidget.data.CountDownTimer
import com.anshmidt.smswidget.data.RowState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion

class UnlockButtonClickCallback : ActionCallback {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[SmsWidget.rowStateKey] = RowState.LOADING.value
        }
        SmsWidget().update(context, glanceId)


        CountDownTimer.tickerFlow(1L)
            .onCompletion {
                updateAppWidgetState(context, glanceId) { prefs ->
                    prefs[SmsWidget.rowStateKey] = RowState.NORMAL.value
                }
                SmsWidget().update(context, glanceId)
            }
            .launchIn(coroutineScope)

        // Locking automatically after certain period of time if no actions were performed
        CountDownTimer.tickerFlow(9L)
            .onCompletion {
                updateAppWidgetState(context, glanceId) { prefs ->
                    val currentRowState = prefs[SmsWidget.rowStateKey]
                    if (currentRowState == RowState.NORMAL.value) {
                        prefs[SmsWidget.rowStateKey] = RowState.LOCKED.value
                    }

                }
                SmsWidget().update(context, glanceId)
            }
            .launchIn(coroutineScope)


            checkAndRequestSendSmsPermission(context)


    }

    private fun checkAndRequestSendSmsPermission(context: Context) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is already granted, send SMS
                Log.d("SmsPermission", "SEND_SMS granted")
            }
            else -> {
                Log.d("SmsPermission", "SEND_SMS not granted")
            }
        }
    }

}
package com.anshmidt.smswidget.ui

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import com.anshmidt.smswidget.data.CountDownTimer
import com.anshmidt.smswidget.data.RowState
import com.anshmidt.smswidget.data.SmsSender
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion

class SendButtonClickCallback : ActionCallback {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        /**
         * When Send button is clicked, it first changes to progress icon,
         * then displays "Message sent" message, and after a few seconds it gets locked.
         */
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[SmsWidget.rowStateKey] = RowState.LOADING.value
        }
        SmsWidget().update(context, glanceId)

        SmsSender().sendSMS()

        CountDownTimer.tickerFlow(2L)
            .onCompletion {
                updateAppWidgetState(context, glanceId) { prefs ->
                    prefs[SmsWidget.rowStateKey] = RowState.MESSAGE_SENT.value
                }
                SmsWidget().update(context, glanceId)
            }
            .launchIn(coroutineScope)


        CountDownTimer.tickerFlow(8L)
            .onCompletion {
                updateAppWidgetState(context, glanceId) { prefs ->
                    prefs[SmsWidget.rowStateKey] = RowState.LOCKED.value
                }
                SmsWidget().update(context, glanceId)
            }
            .launchIn(coroutineScope)
    }





}
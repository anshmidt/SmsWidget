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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
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

        combine(SmsSender().sendSMS(), flow {
            delay(2000)
            emit(Unit)
        }) { result, _ ->
            result
        }
            .collect { result ->
                if (result.isSuccess) {
                    updateAppWidgetState(context, glanceId) { prefs ->
                        prefs[SmsWidget.rowStateKey] = RowState.MESSAGE_SENT.value
                    }
                } else {
                    updateAppWidgetState(context, glanceId) { prefs ->
                        prefs[SmsWidget.rowStateKey] = RowState.MESSAGE_SENDING_FAILED.value
                    }
                }
                SmsWidget().update(context, glanceId)
            }


        System.currentTimeMillis()

//        CountDownTimer.tickerFlow(2L)
//            .onCompletion {
//                updateAppWidgetState(context, glanceId) { prefs ->
//                    prefs[SmsWidget.rowStateKey] = RowState.MESSAGE_SENT.value
//                }
//                SmsWidget().update(context, glanceId)
//            }
//            .launchIn(coroutineScope)


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
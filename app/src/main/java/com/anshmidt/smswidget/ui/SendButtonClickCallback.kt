package com.anshmidt.smswidget.ui

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import com.anshmidt.smswidget.data.CountDownTimer
import com.anshmidt.smswidget.data.RowState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class SendButtonClickCallback : ActionCallback {
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
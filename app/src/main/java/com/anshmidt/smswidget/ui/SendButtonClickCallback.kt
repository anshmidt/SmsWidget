package com.anshmidt.smswidget.ui

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import com.anshmidt.smswidget.RowState
import com.anshmidt.smswidget.SmsWidget
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import java.time.ZoneOffset

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


        simpleTickerFlow(2L)
            .onCompletion {
                updateAppWidgetState(context, glanceId) { prefs ->
                    prefs[SmsWidget.rowStateKey] = RowState.MESSAGE_SENT.value
                }
                SmsWidget().update(context, glanceId)
            }
            .launchIn(coroutineScope)


    }

    private fun simpleTickerFlow(seconds: Long) = flow {
        val expireTime = LocalDateTime.now().plusSeconds(seconds).toEpochSecond(ZoneOffset.UTC)
        val currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        val start = expireTime - currentTime
        val end = 0L
        for (i in start downTo end) {
            emit(i)
            delay(1000)
        }
    }
}
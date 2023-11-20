package com.anshmidt.smswidget.ui

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
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
        val expireTime = LocalDateTime.now().plusSeconds(2L).toEpochSecond(ZoneOffset.UTC)
        val currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        tickerFlow(expireTime - currentTime)
            .onCompletion {
                updateAppWidgetState(context, glanceId) { prefs ->
                    prefs[SmsWidget.isLoadingKey] = false
                }
                SmsWidget().update(context, glanceId)
            }
            .launchIn(coroutineScope)

        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[SmsWidget.isLoadingKey] = true
        }
        SmsWidget().update(context, glanceId)
    }

    private fun tickerFlow(start: Long, end: Long = 0L) = flow {
        for (i in start downTo end) {
            emit(i)
            delay(1_000)
        }
    }
}
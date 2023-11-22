package com.anshmidt.smswidget.ui

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import com.anshmidt.smswidget.RowState
import com.anshmidt.smswidget.SmsWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import java.time.LocalDateTime
import java.time.ZoneOffset

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


        simpleTickerFlow(2L)
            .onCompletion {
                updateAppWidgetState(context, glanceId) { prefs ->
                    prefs[SmsWidget.rowStateKey] = RowState.NORMAL.value
                }
                SmsWidget().update(context, glanceId)
            }
            .launchIn(coroutineScope)

        // Locking automatically after certain period of time if no actions were performed
        simpleTickerFlow(9L)
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
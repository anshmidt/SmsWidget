package com.anshmidt.smswidget.ui

import android.content.Context
import android.util.Log
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
    }

}
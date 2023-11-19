package com.anshmidt.smswidget.ui

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import com.anshmidt.smswidget.SmsWidget

class SendButtonClickCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            val currentIsLoadingValue = prefs[SmsWidget.isLoadingKey] ?: false
            prefs[SmsWidget.isLoadingKey] = currentIsLoadingValue.not()
        }
        SmsWidget.update(context, glanceId)
    }
}
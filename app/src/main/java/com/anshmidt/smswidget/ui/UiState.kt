package com.anshmidt.smswidget.ui

import com.anshmidt.smswidget.datasources.database.MessageEntity

data class UiState(
    val messages: List<MessageEntity>
)
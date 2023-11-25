package com.anshmidt.smswidget.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import java.time.ZoneOffset

object CountDownTimer {

    fun tickerFlow(seconds: Long) = flow {
        val expireTime = LocalDateTime.now().plusSeconds(seconds).toEpochSecond(ZoneOffset.UTC)
        val currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        val start = expireTime - currentTime
        val end = 0L
        for (i in start downTo end + 1) {
            emit(i)
            delay(1000)
        }
    }

}
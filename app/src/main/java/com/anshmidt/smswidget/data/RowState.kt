package com.anshmidt.smswidget.data

enum class RowState(val value: Int) {
    NORMAL(0),
    LOADING(1),
    MESSAGE_SENT(2),
    LOCKED(3);

    companion object {
        val DEFAULT_ROW_STATE = LOCKED
    }
}

fun Int.toRowState(): RowState {
    return RowState.values().firstOrNull { it.value == this } ?: RowState.DEFAULT_ROW_STATE
}

fun RowState.toInt(): Int {
    return this.value
}
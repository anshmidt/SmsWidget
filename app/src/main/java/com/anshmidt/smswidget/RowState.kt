package com.anshmidt.smswidget

enum class RowState(val value: Int) {
    NORMAL(1),
    LOADING(2),
    MESSAGE_SENT(3);

    companion object {
        val DEFAULT_ROW_STATE = NORMAL
    }
}

fun Int.toRowState(): RowState {
    return RowState.values().firstOrNull { it.value == this } ?: RowState.DEFAULT_ROW_STATE
}

fun RowState.toInt(): Int {
    return this.value
}
package com.anshmidt.smswidget.datasources.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = MessageEntity.TABLE)
data class MessageEntity(
    @ColumnInfo(name = ID_COLUMN)
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = PHONE_NUMBER_COLUMN)
    val phoneNumber: String,

    @ColumnInfo(name = MESSAGE_TEXT_COLUMN)
    val messageText: String,

    @ColumnInfo(name = CAPTION_COLUMN)
    val caption: String
) {
    companion object {
        const val TABLE = "messages"
        const val ID_COLUMN = "id"
        const val PHONE_NUMBER_COLUMN = "phoneNumber"
        const val MESSAGE_TEXT_COLUMN = "messageText"
        const val CAPTION_COLUMN = "column"
    }
}
package com.anshmidt.smswidget.datasources.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface MessageDao {

    @Insert
    suspend fun insertMessage(messageEntity: MessageEntity)

    @Update
    suspend fun updateMessage(messageEntity: MessageEntity)

    @Delete
    suspend fun deleteMessage(messageEntity: MessageEntity)

}
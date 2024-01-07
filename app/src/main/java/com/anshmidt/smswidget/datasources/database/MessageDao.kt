package com.anshmidt.smswidget.datasources.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Query("SELECT * FROM ${MessageEntity.TABLE}")
    fun getAllMessages(): Flow<List<MessageEntity>>

    @Query("SELECT * FROM ${MessageEntity.TABLE} WHERE ${MessageEntity.ID_COLUMN}=:messageId")
    fun getMessage(messageId: Int): Flow<MessageEntity>

    @Insert
    suspend fun insertMessage(messageEntity: MessageEntity)

    @Update
    suspend fun updateMessage(messageEntity: MessageEntity)

    @Delete
    suspend fun deleteMessage(messageEntity: MessageEntity)

}
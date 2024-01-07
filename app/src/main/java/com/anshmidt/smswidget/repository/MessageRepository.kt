package com.anshmidt.smswidget.repository

import com.anshmidt.smswidget.datasources.database.AppDatabase
import com.anshmidt.smswidget.datasources.database.MessageEntity
import kotlinx.coroutines.flow.Flow

class MessageRepository(
    val appDatabase: AppDatabase
) {

    fun getAllMessages(): Flow<List<MessageEntity>> {
        return appDatabase.messageDao().getAllMessages()
    }

    fun getMessage(messageId: Int): Flow<MessageEntity> {
        return appDatabase.messageDao().getMessage(messageId = messageId)
    }

    suspend fun addMessage(messageEntity: MessageEntity) {
        return appDatabase.messageDao().insertMessage(messageEntity)
    }

    suspend fun updateMessage(messageEntity: MessageEntity) {
        return appDatabase.messageDao().updateMessage(messageEntity)
    }

    suspend fun deleteMessage(messageEntity: MessageEntity) {
        return appDatabase.messageDao().deleteMessage(messageEntity)
    }

}
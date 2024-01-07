package com.anshmidt.smswidget.di

import androidx.room.Room
import com.anshmidt.smswidget.datasources.database.AppDatabase
import com.anshmidt.smswidget.repository.MessageRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    single { MessageRepository(get()) }

}
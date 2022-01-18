package com.cyberfox21.tinkoffmessanger.di.module

import android.content.Context
import androidx.room.Room
import com.cyberfox21.tinkoffmessanger.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "database").build()

}

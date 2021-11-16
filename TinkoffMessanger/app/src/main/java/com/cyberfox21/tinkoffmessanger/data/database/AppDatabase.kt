package com.cyberfox21.tinkoffmessanger.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cyberfox21.tinkoffmessanger.data.database.dao.ChannelsDao
import com.cyberfox21.tinkoffmessanger.data.database.dao.MessagesDao
import com.cyberfox21.tinkoffmessanger.data.database.dao.TopicsDao
import com.cyberfox21.tinkoffmessanger.data.database.model.ChannelDBModel
import com.cyberfox21.tinkoffmessanger.data.database.model.MessageDBModel
import com.cyberfox21.tinkoffmessanger.data.database.model.TopicDBModel

@Database(
    entities = [MessageDBModel::class,
        ChannelDBModel::class,
        TopicDBModel::class],
    version = 1,
)
@TypeConverters(DateConverter::class, ReactionsConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun messagesDao(): MessagesDao

    abstract fun channelsDao(): ChannelsDao

    abstract fun topicsDao(): TopicsDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        const val MESSAGES_DB_NAME = "messages.db"
        const val CHANNELS_DB_NAME = "channels.db"
        const val TOPICS_DB_NAME = "topics.db"
        private val LOCK = Any()

        fun getInstance(application: Application, dbName: String): AppDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    dbName
                ).build()
                INSTANCE = db
                return db
            }
        }


    }

}
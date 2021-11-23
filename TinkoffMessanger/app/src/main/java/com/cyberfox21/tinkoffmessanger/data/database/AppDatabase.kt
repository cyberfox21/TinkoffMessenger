package com.cyberfox21.tinkoffmessanger.data.database

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cyberfox21.tinkoffmessanger.data.database.dao.*
import com.cyberfox21.tinkoffmessanger.data.database.model.*

@Database(
    entities = [MessageDBModel::class,
        ChannelDBModel::class,
        SubscribedChannelDBModel::class,
        TopicDBModel::class,
        ReactionListDBModel::class,
        UserDBModel::class,
        CurrentUserDBModel::class],
    version = 1,
)
@TypeConverters(DateConverter::class, ReactionsConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun messagesDao(): MessagesDao

    abstract fun allChannelsDao(): AllChannelsDao

    abstract fun subscribedChannelsDao(): SubscribedChannelsDao

    abstract fun topicsDao(): TopicsDao

    abstract fun reactionListDao(): ReactionListDao

    abstract fun usersDao(): UsersDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        const val MESSAGES_DB_NAME = "messages.db"
        const val CHANNELS_DB_NAME = "channels.db"
        const val SUBSCRIBED_CHANNELS_DB_NAME = "subscribed_channels.db"
        const val TOPICS_DB_NAME = "topics.db"
        const val REACTION_LIST_DB_NAME = "reaction_list.db"
        const val USERS_DB_NAME = "users.db"
        private val LOCK = Any()

        fun getInstance(context: Context, dbName: String): AppDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    dbName
                ).build()
                INSTANCE = db
                return db
            }
        }


    }

}
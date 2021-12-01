package com.cyberfox21.tinkoffmessanger.data.database

import androidx.room.Database
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
    exportSchema = false
)
@TypeConverters(DateConverter::class, ReactionsConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun messagesDao(): MessagesDao

    abstract fun allChannelsDao(): AllChannelsDao

    abstract fun subscribedChannelsDao(): SubscribedChannelsDao

    abstract fun topicsDao(): TopicsDao

    abstract fun reactionListDao(): ReactionListDao

    abstract fun usersDao(): UsersDao

}
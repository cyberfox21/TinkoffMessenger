package com.cyberfox21.tinkoffmessanger.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cyberfox21.tinkoffmessanger.data.database.model.SubscribedChannelDBModel
import io.reactivex.Single

@Dao
interface SubscribedChannelsDao {

    @Query("SELECT * FROM subscribedChannels")
    fun getSubscribedChannelsList(): Single<List<SubscribedChannelDBModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSubscribedChannelsListToDB(channelsList: List<SubscribedChannelDBModel>)
}
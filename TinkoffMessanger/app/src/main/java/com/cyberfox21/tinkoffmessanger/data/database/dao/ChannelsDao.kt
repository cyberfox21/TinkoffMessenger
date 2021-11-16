package com.cyberfox21.tinkoffmessanger.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cyberfox21.tinkoffmessanger.data.database.model.ChannelDBModel
import io.reactivex.Single

@Dao
interface ChannelsDao {

    @Query("SELECT * FROM channels")
    fun getAllChannelsList(): Single<List<ChannelDBModel>>

    @Query("SELECT * FROM channels WHERE subscribed = 1")
    fun getSubscribedChannelsList(): Single<List<ChannelDBModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addChannelsListToDB(channelsList: List<ChannelDBModel>)

}
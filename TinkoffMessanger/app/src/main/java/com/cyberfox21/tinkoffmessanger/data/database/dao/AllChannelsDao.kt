package com.cyberfox21.tinkoffmessanger.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cyberfox21.tinkoffmessanger.data.database.model.ChannelDBModel
import io.reactivex.Single

@Dao
interface AllChannelsDao {

    @Query("SELECT * FROM channels")
    fun getAllChannelsList(): Single<List<ChannelDBModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAllChannelsListToDB(channelsList: List<ChannelDBModel>)

}

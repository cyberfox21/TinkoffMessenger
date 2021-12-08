package com.cyberfox21.tinkoffmessanger.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cyberfox21.tinkoffmessanger.data.database.model.TopicDBModel
import io.reactivex.Single

@Dao
interface TopicsDao {

    @Query("SELECT * FROM topics where channelId = :channelId")
    fun getTopicsListForChannel(channelId: Int): Single<List<TopicDBModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTopicsListToDB(topicsList: List<TopicDBModel>)

}

package com.cyberfox21.tinkoffmessanger.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cyberfox21.tinkoffmessanger.data.database.model.MessageDBModel
import io.reactivex.Single

@Dao
interface MessagesDao {

    @Query("SELECT * FROM messages")
    fun getMessageList(): Single<List<MessageDBModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addMessageListToDB(messagesList: List<MessageDBModel>)

}
package com.cyberfox21.tinkoffmessanger.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cyberfox21.tinkoffmessanger.data.database.model.ReactionListDBModel
import io.reactivex.Single

@Dao
interface ReactionListDao {

    @Query("SELECT * FROM reaction_list")
    fun getReactionList(): Single<List<ReactionListDBModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addReactionListToDB(reactionList: List<ReactionListDBModel>)
}

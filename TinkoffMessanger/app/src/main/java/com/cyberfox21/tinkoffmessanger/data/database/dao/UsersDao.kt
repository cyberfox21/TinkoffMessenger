package com.cyberfox21.tinkoffmessanger.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cyberfox21.tinkoffmessanger.data.database.model.CurrentUserDBModel
import com.cyberfox21.tinkoffmessanger.data.database.model.UserDBModel
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface UsersDao {

    @Query("SELECT * FROM users")
    fun getUsersList(): Single<List<UserDBModel>>

    @Query("SELECT * FROM users WHERE id = :userId limit 1")
    fun getUser(userId: Int): Maybe<UserDBModel>

    @Query("SELECT * FROM my_user")
    fun getMyUser(): Maybe<CurrentUserDBModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUsersListToDB(users: List<UserDBModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUserToDB(user: UserDBModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMyUserToDB(user: CurrentUserDBModel)

}

package com.cyberfox21.tinkoffmessanger.domain.repository

import com.cyberfox21.tinkoffmessanger.domain.entity.User
import io.reactivex.Single

interface UsersRepository {

    fun getUsersList(): Single<List<User>>

    fun getMyUser(): Single<User>

}

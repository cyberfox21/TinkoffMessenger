package com.cyberfox21.tinkoffmessanger.domain.repository

import com.cyberfox21.tinkoffmessanger.domain.entity.User
import io.reactivex.Flowable

interface UsersRepository {

    fun getUsersList(): Flowable<List<User>>?

    fun getMyUser(): Flowable<User>

    fun getUser(id: Int): Flowable<User>
}

package com.cyberfox21.tinkoffmessanger.domain.repository

import com.cyberfox21.tinkoffmessanger.domain.entity.User
import io.reactivex.Flowable
import io.reactivex.Observable

interface UsersRepository {

    fun getUsersList(): Observable<List<User>>

    fun getMyUser(): Flowable<User>

    fun getUser(id: Int): Flowable<User>
}

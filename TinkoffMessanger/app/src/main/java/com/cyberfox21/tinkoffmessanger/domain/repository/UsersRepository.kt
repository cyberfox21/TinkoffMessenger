package com.cyberfox21.tinkoffmessanger.domain.repository

import com.cyberfox21.tinkoffmessanger.domain.entity.User
import com.cyberfox21.tinkoffmessanger.domain.enums.UserStatus
import io.reactivex.Observable

interface UsersRepository {

    fun getUsersList(): Observable<Result<List<User>>>

    fun getMyUser(): Observable<Result<User>>

    fun getUser(id: Int): Observable<Result<User>>

    fun getUserPresence(id: Int): Observable<Result<UserStatus>>
}

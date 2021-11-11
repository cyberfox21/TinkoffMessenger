package com.cyberfox21.tinkoffmessanger.data.repository

import com.cyberfox21.tinkoffmessanger.data.api.ApiFactory
import com.cyberfox21.tinkoffmessanger.domain.entity.User
import com.cyberfox21.tinkoffmessanger.domain.repository.UsersRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

object UsersRepositoryImpl : UsersRepository {

    private val api = ApiFactory.api

    override fun getMyUser(): Single<User> {
        return api.getMyUser()
            .map {
                User(
                    id = it.id,
                    avatar = it.avatar_url,
                    name = it.full_name,
                    email = it.email,
                    status = it.is_active
                )
            }.subscribeOn(Schedulers.io())
    }

    override fun getUsersList(): Single<List<User>> {
        return api.getUsers()
            .subscribeOn(Schedulers.io())
            .flatMap { response ->
                val mappedList = response.members.map {
                    User(
                        id = it.id,
                        avatar = it.avatar_url,
                        name = it.full_name,
                        email = it.email,
                        status = it.is_active
                    )
                }
                Single.just(mappedList)
            }
    }
}

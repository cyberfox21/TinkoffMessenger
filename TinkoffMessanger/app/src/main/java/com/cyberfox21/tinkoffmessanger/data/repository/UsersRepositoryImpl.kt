package com.cyberfox21.tinkoffmessanger.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cyberfox21.tinkoffmessanger.data.api.ApiFactory
import com.cyberfox21.tinkoffmessanger.domain.entity.User
import com.cyberfox21.tinkoffmessanger.domain.repository.UsersRepository
import io.reactivex.Single

object UsersRepositoryImpl : UsersRepository {

    private val api = ApiFactory.api

    private var usersList = mutableListOf<User>()

    private var _usersListLD = MutableLiveData<List<User>>()
    private val usersListLD: LiveData<List<User>>
        get() = _usersListLD

    init {
//        for (i in 0 until 15) {
//            val status = Random.nextInt(0, 2)
//            usersList.add(
//                User(
//                    R.drawable.ed,
//                    "Ed Sheeran",
//                    "edsheeran@company.com",
//                    getStatus(status)
//                )
//            )
//        }
//        updateList()
    }

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
            }
    }

    override fun getUsersList(): Single<List<User>> {
        return api.getUsers()
            .flatMap { responce ->
                val mappedList = responce.members.map {
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

    private fun updateList() {
        _usersListLD.value = usersList.toList()
    }

    private fun getStatus(online: Int) = when (online) {
        0 -> false
        1 -> true
        else -> throw RuntimeException("Unknown random number in status $online")
    }
}

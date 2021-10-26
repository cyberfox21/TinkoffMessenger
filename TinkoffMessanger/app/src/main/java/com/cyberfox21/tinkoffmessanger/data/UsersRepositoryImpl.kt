package com.cyberfox21.tinkoffmessanger.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.domain.entity.User
import com.cyberfox21.tinkoffmessanger.domain.repository.UsersRepository
import kotlin.random.Random

object UsersRepositoryImpl : UsersRepository {

    private var usersList = mutableListOf<User>()

    private var _usersListLD = MutableLiveData<List<User>>()
    private val usersListLD: LiveData<List<User>>
        get() = _usersListLD

    init {
        for (i in 0 until 15) {
            val status = Random.nextInt(0, 2)
            usersList.add(
                User(
                    R.drawable.ed,
                    "Ed Sheeran",
                    "edsheeran@company.com",
                    getStatus(status)
                )
            )
        }
        updateList()
    }

    override fun getUsersList(): LiveData<List<User>> {
        return usersListLD
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

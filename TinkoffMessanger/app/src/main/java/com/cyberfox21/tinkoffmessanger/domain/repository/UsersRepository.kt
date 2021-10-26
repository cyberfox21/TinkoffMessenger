package com.cyberfox21.tinkoffmessanger.domain.repository

import androidx.lifecycle.LiveData
import com.cyberfox21.tinkoffmessanger.domain.entity.User

interface UsersRepository {

    fun getUsersList(): LiveData<List<User>>

}

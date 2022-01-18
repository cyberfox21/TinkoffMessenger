package com.cyberfox21.tinkoffmessanger.di.module

import com.cyberfox21.tinkoffmessanger.data.repository.UsersRepositoryImpl
import com.cyberfox21.tinkoffmessanger.domain.repository.UsersRepository
import dagger.Binds
import dagger.Module

@Module
interface UsersBindsModule {

    @Binds
    fun bindUsersRepository(usersRepositoryImpl: UsersRepositoryImpl): UsersRepository

}
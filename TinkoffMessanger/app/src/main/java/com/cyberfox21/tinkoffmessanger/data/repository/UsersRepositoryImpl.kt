package com.cyberfox21.tinkoffmessanger.data.repository

import com.cyberfox21.tinkoffmessanger.data.api.Api
import com.cyberfox21.tinkoffmessanger.data.database.dao.UsersDao
import com.cyberfox21.tinkoffmessanger.data.mapToCurrentUserDBModel
import com.cyberfox21.tinkoffmessanger.data.mapToStatus
import com.cyberfox21.tinkoffmessanger.data.mapToUser
import com.cyberfox21.tinkoffmessanger.data.mapToUserDBModel
import com.cyberfox21.tinkoffmessanger.domain.entity.User
import com.cyberfox21.tinkoffmessanger.domain.enums.UserStatus
import com.cyberfox21.tinkoffmessanger.domain.repository.UsersRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val api: Api,
    private val usersDao: UsersDao
) : UsersRepository {

    override fun getMyUser(): Observable<Result<User>> =
        Observable.concat(
            getMyUserFromDB().toObservable(),
            getMyUserFromNetwork().toObservable()
        ).subscribeOn(Schedulers.io())

    override fun getUser(id: Int): Observable<Result<User>> =
        Observable.concat(
            getUserFromDB(id).toObservable(),
            getUserFromNetwork(id).toObservable()
        ).subscribeOn(Schedulers.io())

    override fun getUsersList(): Observable<Result<List<User>>> =
        Observable.concat(
            getUsersListFromNetwork().toObservable(),
            getUsersListFromDB().toObservable()
        ).subscribeOn(Schedulers.io())

    override fun getUserPresence(id: Int): Observable<Result<UserStatus>> =
        Observable.concat(
            getUserPresenceFromNetwork(id).toObservable(),
            getUserPresenceFromDB().toObservable()
        ).subscribeOn(Schedulers.io())


    private fun getMyUserFromDB(): Single<Result<User>> {
        return usersDao.getMyUser()
            .map { Result.success(it.mapToUser()) }
            .onErrorReturn { Result.failure(it) }
            .subscribeOn(Schedulers.io())
    }

    private fun getMyUserFromNetwork(): Single<Result<User>> {
        return api.getMyUser()
            .map { Result.success(it.mapToUser()) }
            .doOnSuccess { result ->
                result.getOrNull()?.mapToCurrentUserDBModel()?.let { usersDao.addMyUserToDB(it) }
            }
            .onErrorReturn { Result.failure(it) }
            .subscribeOn(Schedulers.io())
    }

    private fun getUserFromDB(userId: Int): Single<Result<User>> {
        return usersDao.getUser(userId)
            .map { Result.success(it.mapToUser()) }
            .onErrorReturn { Result.failure(it) }
            .subscribeOn(Schedulers.io())
    }

    private fun getUserFromNetwork(userId: Int): Single<Result<User>> {
        return api.getUser(userId)
            .map { Result.success(it.mapToUser()) }
            .doOnSuccess { result ->
                result.getOrNull()?.mapToUserDBModel()?.let { usersDao.addUserToDB(it) }
            }
            .onErrorReturn { Result.failure(it) }
            .subscribeOn(Schedulers.io())
    }

    private fun getUsersListFromDB(): Single<Result<List<User>>> {
        return usersDao.getUsersList()
            .map { list ->
                Result.success(list.map { it.mapToUser() }.sortedBy { user -> user.name })
            }
            .onErrorReturn { Result.failure(it) }
            .subscribeOn(Schedulers.io())
    }

    private fun getUsersListFromNetwork(): Single<Result<List<User>>> {
        return api.getUsers()
            .map { response ->
                Result.success(response.members.map { it.mapToUser() }
                    .sortedBy { user -> user.name })
            }
            .doOnSuccess { result ->
                result.getOrNull()?.let { list ->
                    usersDao.addUsersListToDB(list.map { it.mapToUserDBModel() })
                }
            }
            .onErrorReturn { Result.failure(it) }
            .subscribeOn(Schedulers.io())
    }

    private fun getUserPresenceFromNetwork(id: Int): Single<Result<UserStatus>> =
        api.getUserPresence(id)
            .map { response ->
                Result.success(response.presence.client.mapToStatus())
            }
            .onErrorReturn { Result.failure(it) }
            .subscribeOn(Schedulers.io())

    private fun getUserPresenceFromDB(): Single<Result<UserStatus>> =
        Single.just(UserStatus.OFFLINE).map { status -> Result.success(status) }

}

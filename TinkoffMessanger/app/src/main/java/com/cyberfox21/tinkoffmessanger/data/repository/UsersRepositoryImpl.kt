package com.cyberfox21.tinkoffmessanger.data.repository

import com.cyberfox21.tinkoffmessanger.data.api.Api
import com.cyberfox21.tinkoffmessanger.data.database.dao.UsersDao
import com.cyberfox21.tinkoffmessanger.data.mapToCurrentUserDBModel
import com.cyberfox21.tinkoffmessanger.data.mapToUser
import com.cyberfox21.tinkoffmessanger.data.mapToUserDBModel
import com.cyberfox21.tinkoffmessanger.domain.entity.User
import com.cyberfox21.tinkoffmessanger.domain.repository.UsersRepository
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val api: Api,
    private val usersDao: UsersDao
) : UsersRepository {

    private fun getMyUserFromDB() = usersDao.getMyUser().map {
        it.mapToUser()
    }.subscribeOn(Schedulers.io())

    private fun getMyUserFromNetwork() = api.getMyUser()
        .map {
            it.mapToUser()
        }.doOnSuccess {
            usersDao.addMyUserToDB(it.mapToCurrentUserDBModel())
        }.subscribeOn(Schedulers.io())

    private fun getUserFromDB(userId: Int) = usersDao.getUser(userId).map {
        it.mapToUser()
    }.subscribeOn(Schedulers.io())

    private fun getUserFromNetwork(userId: Int) = api.getUser(userId)
        .map {
            it.mapToUser()
        }.doOnSuccess {
            usersDao.addUserToDB(it.mapToUserDBModel())
        }.subscribeOn(Schedulers.io())

    private fun getUsersListFromDB() = usersDao.getUsersList().map { list ->
        list.map {
            it.mapToUser()
        }
    }.subscribeOn(Schedulers.io())

    private fun getUsersListFromNetwork(): Single<List<User>> {
        return api.getUsers()
            .map { response ->
                response.members.map {
                    it.mapToUser()
                }
            }.doOnSuccess { list ->
                usersDao.addUsersListToDB(list.map {
                    it.mapToUserDBModel()
                })
            }.subscribeOn(Schedulers.io())
    }

    override fun getMyUser(): Flowable<User> {
        return getMyUserFromDB().switchIfEmpty(getMyUserFromNetwork()).toFlowable()
    }

    override fun getUser(id: Int): Flowable<User> =
        getUserFromDB(id).switchIfEmpty(getUserFromNetwork(id)).toFlowable()

    override fun getUsersList(): Observable<List<User>> =
        Observable.concat(
            getUsersListFromDB().toObservable().map { it.sortedBy { user -> user.name } },
            getUsersListFromNetwork().toObservable().map { it.sortedBy { user -> user.name } }
        ).subscribeOn(Schedulers.io())

}


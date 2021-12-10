package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.entity.User
import com.cyberfox21.tinkoffmessanger.domain.repository.UsersRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetUsersListUseCase @Inject constructor(private val repository: UsersRepository) {
    operator fun invoke(query: String): Observable<Result<List<User>>> {
        val users: Observable<Result<List<User>>> = repository.getUsersList()
            .debounce(500L, TimeUnit.MILLISECONDS, Schedulers.io())
            .distinctUntilChanged()
            .map { result ->
                result.fold(
                    onSuccess = {
                        result.map { list ->
                            list.filter {
                                return@filter it.name.contains(query, ignoreCase = true)
                            }
                        }
                    },
                    onFailure = { Result.failure(it) }
                )
            }
        return users
    }
}

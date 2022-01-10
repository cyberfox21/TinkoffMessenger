package com.cyberfox21.tinkoffmessanger.data.network.api

import com.cyberfox21.tinkoffmessanger.data.network.response.UserPresenceResponse
import com.cyberfox21.tinkoffmessanger.data.network.response.UsersResponse
import com.cyberfox21.tinkoffmessanger.data.network.response.dto.UserDTO
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface UsersApi {

    @GET("users")
    fun getUsers(): Single<UsersResponse>

    @GET("users/{user_id}")
    fun getUser(@Path("user_id") id: Int): Single<UserDTO>

    @GET("users/me")
    fun getMyUser(): Single<UserDTO>

    @GET("users/{user_id}/presence")
    fun getUserPresence(@Path("user_id") userId: Int): Single<UserPresenceResponse>

}
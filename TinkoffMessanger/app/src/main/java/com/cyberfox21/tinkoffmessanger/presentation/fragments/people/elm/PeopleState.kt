package com.cyberfox21.tinkoffmessanger.presentation.fragments.people.elm

import android.os.Parcelable
import com.cyberfox21.tinkoffmessanger.domain.entity.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class PeopleState(
    val users: List<User>? = null,
    val isEmptyState: Boolean = false,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
) : Parcelable

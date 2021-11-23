package com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm

import android.os.Parcelable
import com.cyberfox21.tinkoffmessanger.domain.entity.User
import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.ProfileMode
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileState(
    val user: User? = null,
    val isEmptyState: Boolean = false,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val profileScreenMode: ProfileMode = ProfileMode.YOUR
) : Parcelable

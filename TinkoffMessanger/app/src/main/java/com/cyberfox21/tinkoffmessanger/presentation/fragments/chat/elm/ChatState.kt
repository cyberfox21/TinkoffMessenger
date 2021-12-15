package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

import android.os.Parcelable
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.presentation.common.ResourceStatus
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatState(
    val messages: List<Message> = listOf(),
    val reactions: List<Reaction> = listOf(),
    val messageError: Throwable? = null,
    val reactionsError: Throwable? = null,
    val messageStatus: ResourceStatus = ResourceStatus.EMPTY,
    val reactionsListStatus: ResourceStatus = ResourceStatus.EMPTY
) : Parcelable

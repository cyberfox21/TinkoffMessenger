package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

import android.os.Parcelable
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatState(
    val messages: List<Message> = listOf(),
    val reactions: List<Reaction> = listOf(),
    val isEmptyMessageList: Boolean = true,
    val isEmptyReactionList: Boolean = true,
    val isMessagesLoading: Boolean = false,
    val isReactionsLoading: Boolean = false,
    val messageError: Throwable? = null,
    val reactionsError: Throwable? = null
) : Parcelable
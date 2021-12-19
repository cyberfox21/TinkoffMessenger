package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

import android.os.Parcelable
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.presentation.common.ResourceStatus
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.ChatDelegateItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatState(
    val currentUserId: Int = UNDEFINED_USER_ID,
    val messages: List<ChatDelegateItem> = listOf(),
    val reactions: List<Reaction> = listOf(),
    val messageError: Throwable? = null,
    val reactionsError: Throwable? = null,
    val messageStatus: ResourceStatus = ResourceStatus.EMPTY,
    val reactionsListStatus: ResourceStatus = ResourceStatus.EMPTY
) : Parcelable {
    companion object {
        const val UNDEFINED_USER_ID = -1
    }
}

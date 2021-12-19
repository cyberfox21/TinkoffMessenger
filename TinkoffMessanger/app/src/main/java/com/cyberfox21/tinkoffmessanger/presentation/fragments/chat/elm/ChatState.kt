package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

import android.os.Parcelable
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.presentation.common.ResourceStatus
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.ChatDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.enums.ScrollStatus
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatState(
    var selectEmoji: Reaction? = null,
    var selectMsgId: Int? = null,
    val savedPosition: Int = INITIAL_SAVED_POSITION,
    val currentUserId: Int = UNDEFINED_USER_ID,
    val messages: List<ChatDelegateItem> = listOf(),
    val reactions: List<Reaction> = listOf(),
    val messageError: Throwable? = null,
    val reactionsError: Throwable? = null,
    val messageStatus: ResourceStatus = ResourceStatus.EMPTY,
    val reactionsListStatus: ResourceStatus = ResourceStatus.EMPTY,
    val scrollStatus: ScrollStatus = ScrollStatus.STAY
) : Parcelable {
    companion object {
        const val INITIAL_SAVED_POSITION = 0
        const val UNDEFINED_USER_ID = -1
    }
}

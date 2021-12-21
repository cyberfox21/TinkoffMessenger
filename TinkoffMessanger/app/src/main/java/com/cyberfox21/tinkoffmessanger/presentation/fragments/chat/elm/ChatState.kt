package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

import android.os.Parcelable
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.presentation.common.ResourceStatus
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.ChatDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.enums.ButtonSendMode
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.enums.PaginationStatus
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.enums.ScrollStatus
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.enums.UpdateType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatState(
    var selectEmoji: Reaction? = null,
    var selectMsgId: Int? = null,
    var selectMsgIdForEmoji: Int? = null,
    val savedPosition: Int = INITIAL_SAVED_POSITION,
    val currentUserId: Int = UNDEFINED_USER_ID,
    val messages: List<Message> = listOf(),
    val chatItems: List<ChatDelegateItem> = listOf(),
    val reactions: List<Reaction> = listOf(),
    val messageError: Throwable? = null,
    val reactionsError: Throwable? = null,
    val messageStatus: ResourceStatus = ResourceStatus.LOADING,
    val reactionsListStatus: ResourceStatus = ResourceStatus.EMPTY,
    val scrollStatus: ScrollStatus = ScrollStatus.SCROLL_TO_BOTTOM,
    val paginationStatus: PaginationStatus = PaginationStatus.PART,
    val updateType: UpdateType = UpdateType.INITIAL,
    var btnSendMode: ButtonSendMode = ButtonSendMode.ADD
) : Parcelable {
    companion object {
        const val INITIAL_SAVED_POSITION = 0
        const val UNDEFINED_USER_ID = -1
    }
}

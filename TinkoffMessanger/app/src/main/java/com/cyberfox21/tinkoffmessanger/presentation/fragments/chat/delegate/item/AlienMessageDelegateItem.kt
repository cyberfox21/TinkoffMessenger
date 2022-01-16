package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item

data class AlienMessageDelegateItem(
    override val id: Int,
    override val text: String,
    override val timeAsId: Long,
    override val time: String,
    override val senderId: Int,
    val senderName: String,
    val senderAvatarUrl: String,
    override val reactions: List<MessageReactionListItem>
) : MessageDelegateItem(
    id = id,
    text = text,
    time = time,
    timeAsId = timeAsId,
    reactions = reactions,
    senderId = senderId
) {

    override fun id(): Any = id

    override fun content() = reactions

}

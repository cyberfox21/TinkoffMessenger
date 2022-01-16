package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item

data class MyMessageDelegateItem(
    override val id: Int,
    val myId: Int,
    override val text: String,
    override val timeAsId: Long,
    override val time: String,
    override val reactions: List<MessageReactionListItem>
) : MessageDelegateItem(
    id = id,
    text = text,
    timeAsId = timeAsId,
    time = time,
    reactions = reactions,
    senderId = myId
) {

    override fun id(): Any = id

    override fun content() = this
}

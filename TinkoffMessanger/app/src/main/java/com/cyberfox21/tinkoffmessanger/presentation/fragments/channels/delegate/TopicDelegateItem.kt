package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate

import com.cyberfox21.tinkoffmessanger.domain.entity.Topic

data class TopicDelegateItem(
    val id: Int,
    val topic: Topic
) : DelegateItem {
    override fun content(): Any = topic
    override fun id(): Int = id
}

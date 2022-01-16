package com.cyberfox21.tinkoffmessanger.presentation.util

import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction

interface MessageHelper {
    fun mapMessageContent(text: String, emojis: List<Reaction>): CharSequence
    fun replaceMessage(list: List<Message>, newMessage: Message): List<Message>
    fun mergeMessages(oldList: List<Message>, newList: List<Message>): List<Message>
}
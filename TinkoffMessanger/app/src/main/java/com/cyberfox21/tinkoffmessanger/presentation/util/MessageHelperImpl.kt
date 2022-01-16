package com.cyberfox21.tinkoffmessanger.presentation.util

import android.text.SpannableString
import androidx.core.text.HtmlCompat
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction

class MessageHelperImpl : MessageHelper {
    override fun mapMessageContent(text: String, emojis: List<Reaction>): CharSequence {
        return SpannableString(
            HtmlCompat.fromHtml(
                if (text.contains(":")) {
                    text.split(":").joinToString("") {
                        val emoji = emojis.find { emoji -> emoji.name == it }
                        val isFirstColon = emoji?.reaction == null
                        emoji?.reaction ?: (if (isFirstColon) ":$it" else it)
                    }
                } else text,
                HtmlCompat.FROM_HTML_MODE_COMPACT
            ).trim()
        )
    }

    override fun replaceMessage(list: List<Message>, newMessage: Message): List<Message> {
        return list.toMutableList().apply {
            val index = this.indexOfFirst { message -> message.id == newMessage.id }
            removeAt(index)
            addAll(index, listOf(newMessage))
        }
    }

    override fun mergeMessages(oldList: List<Message>, newList: List<Message>): List<Message> =
        oldList.toMutableList().apply {
            removeAt(oldList.lastIndex)
            addAll(newList)
        }

}
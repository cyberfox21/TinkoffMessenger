package com.cyberfox21.tinkoffmessanger.presentation.common

import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.ChatItemsMapper
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.AlienMessageDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.DateDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.MessageReactionListItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.MyMessageDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.util.DateFormatter
import com.cyberfox21.tinkoffmessanger.presentation.util.MessageHelper
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class ChatItemsMapperTest {

    private val helper = object : MessageHelper {
        override fun mapMessageContent(text: String, emojis: List<Reaction>): CharSequence {
            return text
        }

        override fun replaceMessage(list: List<Message>, newMessage: Message): List<Message> {
            TODO("Not yet implemented")
        }

        override fun mergeMessages(oldList: List<Message>, newList: List<Message>): List<Message> {
            TODO("Not yet implemented")
        }

    }

    private val formatter = object : DateFormatter {
        override fun utcToDate(utc: Int): Date {
            TODO("Not yet implemented")
        }

        override fun isTheSameDay(date1: Date, date2: Date): Boolean = date1 == date2

        override fun getDateForChatItem(date: Date): String = "${date.time} янв"

        override fun getTimeForMessage(date: Date): String = "${date.time}:00"
    }

    private val messages = listOf(
        Message(
            id = 265726448,
            "message for change topic",
            time = Date(6),
            senderId = 455896,
            senderName = "Татьяна Школьник",
            senderAvatarUrl = "https://zulip-avatars.s3.amazonaws.com/39154/d8ee9e4fa6d8b1253f32824276b460320291f8a8?version=4",
            isCurrentUser = false,
            reactions = listOf()
        ),
        Message(
            id = 266106739,
            message = "2021-12-07-16-35-59-467.jpg",
            time = Date(4),
            senderId = 455561,
            senderName = "Дмитрий Карпенко",
            senderAvatarUrl = "https://secure.gravatar.com/avatar/52224d2c6a45d5017b91bea4c84ab934?d=identicon&version=1",
            isCurrentUser = false,
            reactions = listOf(
                Reaction(
                    userId = 455371,
                    code = "1f4a9",
                    name = "poop",
                    reaction = "💩",
                    type = "unicode_emoji"
                )
            )
        ),
        Message(
            id = 26572645,
            "message topic",
            time = Date(3),
            senderId = 455896,
            senderName = "Татьяна Школьник",
            senderAvatarUrl = "https://zulip-avatars.s3.amazonaws.com/39154/d8ee9e4fa6d8b1253f32824276b460320291f8a8?version=4",
            isCurrentUser = false,
            reactions = listOf()
        ),
        Message(
            id = 26610659,
            message = "Hi there!",
            time = Date(3),
            senderId = 455561,
            senderName = "Дмитрий Карпенко",
            senderAvatarUrl = "https://secure.gravatar.com/avatar/52224d2c6a45d5017b91bea4c84ab934?d=identicon&version=1",
            isCurrentUser = false,
            reactions = listOf(
                Reaction(
                    userId = 455371,
                    code = "1f4a9",
                    name = "poop",
                    reaction = "💩",
                    type = "unicode_emoji"
                ),
                Reaction(
                    userId = 455371,
                    code = "1f910",
                    name = "silence",
                    reaction = "🤐",
                    type = "unicode_emoji"
                )
            )
        )
    )
    private val reactions = listOf(
        Reaction(
            userId = 455371,
            code = "1f4a9",
            name = "poop",
            reaction = "💩",
            type = "unicode_emoji"
        ),
        Reaction(
            userId = 455371,
            code = "1f910",
            name = "silence",
            reaction = "🤐",
            type = "unicode_emoji"
        )
    )
    private val expected = listOf(
        DateDelegateItem("3 янв"),
        AlienMessageDelegateItem(
            id = 26610659,
            text = "Hi there!",
            time = "3:00",
            timeAsId = 3,
            senderId = 455561,
            senderName = "Дмитрий Карпенко",
            senderAvatarUrl = "https://secure.gravatar.com/avatar/52224d2c6a45d5017b91bea4c84ab934?d=identicon&version=1",
            reactions = listOf(
                MessageReactionListItem(
                    code = "1f4a9",
                    name = "poop",
                    reaction = "💩",
                    type = "unicode_emoji",
                    count = 1,
                    senderId = 455371,
                    isSelected = false
                ),
                MessageReactionListItem(
                    code = "1f910",
                    name = "silence",
                    reaction = "🤐",
                    type = "unicode_emoji",
                    count = 1,
                    senderId = 455371,
                    isSelected = false
                )
            )
        ),
        MyMessageDelegateItem(
            id = 26572645,
            text = "message topic",
            time = "3:00",
            myId = 455896,
            reactions = listOf(),
            timeAsId = 3
        ),
        DateDelegateItem("4 янв"),
        AlienMessageDelegateItem(
            id = 266106739,
            text = "2021-12-07-16-35-59-467.jpg",
            time = "4:00",
            timeAsId = 4,
            senderId = 455561,
            senderName = "Дмитрий Карпенко",
            senderAvatarUrl = "https://secure.gravatar.com/avatar/52224d2c6a45d5017b91bea4c84ab934?d=identicon&version=1",
            reactions = listOf(
                MessageReactionListItem(
                    code = "1f4a9",
                    name = "poop",
                    reaction = "💩",
                    type = "unicode_emoji",
                    count = 1,
                    senderId = 455371,
                    isSelected = false
                )
            )
        ),
        DateDelegateItem("6 янв"),
        MyMessageDelegateItem(
            id = 265726448,
            text = "message for change topic",
            time = "6:00",
            myId = 455896,
            reactions = listOf(),
            timeAsId = 6
        )
    ).reversed()


    @Test
    fun mapToChatDelegateItemsListTest() {

        val userId = 455896

        val mapper = ChatItemsMapper(helper, formatter)

        val result = mapper.mapToChatDelegateItemsList(messages, userId, reactions)

        assertEquals(expected, result)

    }

}
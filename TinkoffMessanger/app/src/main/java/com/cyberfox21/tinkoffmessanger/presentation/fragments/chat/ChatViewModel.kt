package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cyberfox21.tinkoffmessanger.data.MessageRepositoryImpl
import com.cyberfox21.tinkoffmessanger.data.ReactionRepositoryImpl
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.domain.usecase.AddMessageUseCase
import com.cyberfox21.tinkoffmessanger.domain.usecase.EditMessageUseCase
import com.cyberfox21.tinkoffmessanger.domain.usecase.GetMessageListUseCase
import com.cyberfox21.tinkoffmessanger.domain.usecase.GetReactionListUseCase
import java.util.*

class ChatViewModel : ViewModel() {

    private var i = 0

    private val messageRepository = MessageRepositoryImpl
    private val reactionsRepository = ReactionRepositoryImpl
    private val getMessageListUseCase = GetMessageListUseCase(messageRepository)
    private val addMessageUseCase = AddMessageUseCase(messageRepository)
    private val editMessageUseCase = EditMessageUseCase(messageRepository)
    private val getReactionListUseCase = GetReactionListUseCase(reactionsRepository)

    val reactionList = getReactionListUseCase()

    private var _messageListLD = getMessageListUseCase()
    val messageList: LiveData<List<Message>>
        get() = _messageListLD

    fun sendMessage(image: Int, name: String, text: String) {
        val time = getTime()
        val message = Message(i, image, name, text, time, mutableListOf<Reaction>())
        i++
        addMessageUseCase(message)
    }

    fun addNewEmoji(message: Message, emoji: String) {
        val newMessage = message.copy(reactions = message.reactions.apply {
            add(
                Reaction(
                    emoji,
                    COUNT_OF_REACTIONS
                )
            )
        })
        editMessageUseCase(newMessage)
    }

    private fun getTime(): String {
        val currentTime: Date = Calendar.getInstance().time
        return currentTime.toString().substring(TIME_START_INDEX, TIME_END_INDEX)
    }

    companion object {
        const val TIME_START_INDEX = 11
        const val TIME_END_INDEX = 16
        const val COUNT_OF_REACTIONS = 3
    }

}
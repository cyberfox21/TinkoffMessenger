package com.cyberfox21.tinkoffmessanger.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cyberfox21.tinkoffmessanger.data.MessageRepositoryImpl
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.domain.usecase.AddMessageUseCase
import com.cyberfox21.tinkoffmessanger.domain.usecase.GetMessageListUseCase
import java.util.*

class ChatViewModel : ViewModel() {

    private var i = 0

    private val repository = MessageRepositoryImpl()
    private val getMessageListUseCase = GetMessageListUseCase(repository)
    private val addMessageUseCase = AddMessageUseCase(repository)

    private var _messageListLD = getMessageListUseCase()
    val messageList: LiveData<List<Message>>
        get() = _messageListLD

    fun sendMessage(image: Int, name: String, text: String) {
        val time = getTime()
        val message = Message(i, image, name, text, time, listOf<Reaction>())
        i++
        addMessageUseCase(message)
    }

    private fun getTime(): String {
        val currentTime: Date = Calendar.getInstance().time
        return currentTime.toString().substring(TIME_START_INDEX, TIME_END_INDEX)
    }

    companion object {
        const val TIME_START_INDEX = 11
        const val TIME_END_INDEX = 16
    }

}

package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic

class ChatViewModelFactory(private val channel: Channel, private val topic: Topic) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(channel, topic) as T
        }
        throw RuntimeException("Unknown view model class $modelClass")
    }
}

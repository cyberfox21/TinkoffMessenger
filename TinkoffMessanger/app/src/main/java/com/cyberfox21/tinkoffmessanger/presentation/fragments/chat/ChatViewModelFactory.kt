package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ChatViewModelFactory(
    private val application: Application,
    private val channelName: String,
    private val topicName: String
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) return ChatViewModel(
            application,
            channelName,
            topicName
        ) as T
        throw RuntimeException("Unknown view model class $modelClass")
    }
}

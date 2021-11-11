package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat

import com.cyberfox21.tinkoffmessanger.domain.entity.Message

sealed class ChatScreenState {

    class Result(val items: List<Message>) : ChatScreenState() {}

    object Loading : ChatScreenState() {}

    class Error(val error: Throwable) : ChatScreenState() {}

}

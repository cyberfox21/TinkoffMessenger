package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat

import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem

sealed class ChatScreenState {

    class Result(val items: List<DelegateItem>) : ChatScreenState() {}

    object Loading : ChatScreenState() {}

    class Error(val error: Throwable) : ChatScreenState() {}

}

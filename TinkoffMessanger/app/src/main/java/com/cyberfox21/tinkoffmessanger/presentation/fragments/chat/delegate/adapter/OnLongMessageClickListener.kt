package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.adapter

import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.MessageDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.enums.BottomDialogMode

interface OnLongMessageClickListener {
    fun onLongMessageClick(message: MessageDelegateItem, mode: BottomDialogMode)
}

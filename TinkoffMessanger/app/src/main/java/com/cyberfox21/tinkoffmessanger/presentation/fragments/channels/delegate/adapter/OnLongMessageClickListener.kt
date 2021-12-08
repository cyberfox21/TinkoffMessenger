package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.adapter

import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem

interface OnLongMessageClickListener {
    fun onLongMessageClick(message: DelegateItem)
}

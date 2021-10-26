package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat

import androidx.recyclerview.widget.DiffUtil
import com.cyberfox21.tinkoffmessanger.domain.entity.Message

class MessageDiffUtilCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return false
    }
}

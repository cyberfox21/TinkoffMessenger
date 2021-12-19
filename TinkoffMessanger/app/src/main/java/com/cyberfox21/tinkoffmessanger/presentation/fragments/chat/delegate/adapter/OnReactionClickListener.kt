package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.adapter

import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.ClickEmojiMode

interface OnReactionClickListener {
    fun onReactionClick(clickMode: ClickEmojiMode, messageId: Int, emoji: Reaction)
}
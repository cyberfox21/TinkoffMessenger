package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.clicklisteners

import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.enums.ClickEmojiMode

interface OnReactionClickListener {
    fun onReactionClick(clickMode: ClickEmojiMode, messageId: Int, emoji: Reaction)
}
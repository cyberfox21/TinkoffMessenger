package com.cyberfox21.tinkoffmessanger.presentation.common

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.item.ChannelDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.item.TopicDelegateItem

fun Channel.mapToChannelDelegateItem(selected: Boolean) = ChannelDelegateItem(
    id = id,
    name = name,
    isSelected = selected
)

fun Topic.mapToTopicDelegateItem() = TopicDelegateItem(
    name = title,
    msgCount = messagesCount
)

fun Context.copyToClipboard(text: CharSequence) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
}

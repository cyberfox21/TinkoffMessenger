package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm

import com.cyberfox21.tinkoffmessanger.presentation.common.ResourceStatus
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.item.ChannelDelegateItem

data class ChannelsState(
    var selectedChannelName: String = UNDEFINED_CHANNEL_NAME,
    var selectedChannelId: Int = UNDEFINED_CHANNEL_ID,
    val delegateItems: List<DelegateItem> = listOf(),
    val delegateChannels: List<ChannelDelegateItem> = listOf(),
    val isFirstLoading: Boolean = true,
    val error: Throwable? = null,
    val channelStatus: ResourceStatus = ResourceStatus.EMPTY,
    val topicStatus: ResourceStatus = ResourceStatus.EMPTY
) {
    companion object {
        const val UNDEFINED_CHANNEL_ID = -1
        const val UNDEFINED_CHANNEL_NAME = ""
    }
}

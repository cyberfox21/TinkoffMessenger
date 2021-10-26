package com.cyberfox21.tinkoffmessanger.domain.repository

import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.enums.Category

interface ChannelsRepository {

    fun getChannelsList(category: Category): List<Channel>

}

package com.cyberfox21.tinkoffmessanger.domain.repository

import androidx.lifecycle.LiveData
import com.cyberfox21.tinkoffmessanger.domain.enums.Category
import com.cyberfox21.tinkoffmessanger.domain.entity.Channel

interface ChannelsRepository {

    fun getChannelsList(category: Category): LiveData<List<Channel>>

}

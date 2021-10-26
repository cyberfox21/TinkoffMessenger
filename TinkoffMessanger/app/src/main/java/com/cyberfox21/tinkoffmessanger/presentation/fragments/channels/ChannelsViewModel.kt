package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels

import androidx.lifecycle.ViewModel
import com.cyberfox21.tinkoffmessanger.data.ChannelsRepositoryImpl
import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.enums.Category
import com.cyberfox21.tinkoffmessanger.domain.usecase.GetChannelsListUseCase

class ChannelsViewModel : ViewModel() {

    private val repository = ChannelsRepositoryImpl

    private val getChannelsListUseCase = GetChannelsListUseCase(repository)

    var channelsList = listOf<Channel>()

    fun getList(category: Category) {
        channelsList = getChannelsListUseCase.invoke(category)
    }

}

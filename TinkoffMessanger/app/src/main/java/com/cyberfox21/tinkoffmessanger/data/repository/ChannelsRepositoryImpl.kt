package com.cyberfox21.tinkoffmessanger.data.repository

import com.cyberfox21.tinkoffmessanger.data.api.ApiFactory
import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.repository.ChannelsRepository
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.Category
import com.cyberfox21.tinkoffmessanger.util.mapToChannel
import io.reactivex.Single

object ChannelsRepositoryImpl : ChannelsRepository {

    private val api = ApiFactory.api

    private fun getChannelsList(): Single<List<Channel>> {
        return api.getChannels().map { response ->
            response.streams.map {
                it.mapToChannel()
            }
        }
    }

    private fun getSubscribedChannelsList(): Single<List<Channel>> {
        return api.getSubscribedChannels().map { response ->
            response.subscriptions.map {
                it.mapToChannel()
            }
        }
    }

    override fun searchChannels(
        searchQuery: String,
        category: Category
    ): Single<List<Channel>> {
        return when (category) {
            Category.SUBSCRIBED -> getSubscribedChannelsList()
            Category.ALL -> getChannelsList()
        }
    }

}

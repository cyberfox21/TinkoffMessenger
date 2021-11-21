package com.cyberfox21.tinkoffmessanger.domain.repository

import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.Category
import io.reactivex.Observable

interface ChannelsRepository {
    fun searchChannels(searchQuery: String, category: Category): Observable<List<Channel>>
}

package com.cyberfox21.tinkoffmessanger.data

import androidx.lifecycle.MutableLiveData
import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import com.cyberfox21.tinkoffmessanger.domain.enums.Category
import com.cyberfox21.tinkoffmessanger.domain.repository.ChannelsRepository
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

object ChannelsRepositoryImpl : ChannelsRepository {

    private var _allChannelsLD = MutableLiveData<List<Channel>>()
    val allChannelsLD get() = _allChannelsLD

    private var _subscribedLD = MutableLiveData<List<Channel>>()
    val subscribedLD get() = _subscribedLD

    private var allChannels = mutableListOf(
        Channel(
            "#general", listOf(
                Topic("#testing", 1345),
                Topic("#bruh", 234)
            )
        ),
        Channel(
            "#Development", listOf(
                Topic("#Android", 4605),
                Topic("#iOS", 4522)
            )
        ),
        Channel(
            "#Design",
            listOf(
                Topic("#figma", 123),
                Topic("#html", 12234),
                Topic("#xml", 2342),
                Topic("#css", 555)
            )
        ),
        Channel("#PR", listOf())
    )


    private var subscribedChannels = mutableListOf(
        Channel(
            "#general", listOf(
                Topic("#testing", 1345),
                Topic("#bruh", 234)
            )
        ),
        Channel(
            "#Development", listOf(
                Topic("#Android", 4605),
                Topic("#iOS", 4522)
            )
        )
    )

    override fun getChannelsList(category: Category): List<Channel> {
        return when (category) {
            Category.ALL -> {
                allChannels
            }
            Category.SUBSCRIBED -> {
                subscribedChannels
            }
        }
    }

    override fun searchChannels(
        searchQuery: String,
        category: Category
    ): Observable<List<Channel>> {
        return Observable.fromCallable {
            when (category) {
                Category.SUBSCRIBED -> subscribedChannels.toList()
                Category.ALL -> allChannels.toList()
            }
        }.delay(500, TimeUnit.MILLISECONDS)

    }
}

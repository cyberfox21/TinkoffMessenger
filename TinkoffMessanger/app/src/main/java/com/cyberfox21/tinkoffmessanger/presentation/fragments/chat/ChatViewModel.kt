package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cyberfox21.tinkoffmessanger.data.repository.MessageRepositoryImpl
import com.cyberfox21.tinkoffmessanger.data.repository.ReactionRepositoryImpl
import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import com.cyberfox21.tinkoffmessanger.domain.usecase.*
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ChatViewModel(private val channel: Channel, private val topic: Topic) : ViewModel() {

    private val messageRepository = MessageRepositoryImpl
    private val reactionsRepository = ReactionRepositoryImpl

    private val getMessageListUseCase = GetMessageListUseCase(messageRepository)
    private val addMessageUseCase = AddMessageUseCase(messageRepository)

    private val getReactionListUseCase = GetReactionListUseCase(reactionsRepository)
    private val addReactionUseCase = AddReactionUseCase(reactionsRepository)
    private val deleteReactionUseCase = DeleteReactionUseCase(reactionsRepository)

    val reactionList = getReactionListUseCase()

    private var numBefore: Int = 100
    private var numAfter: Int = 0

    private val compositeDisposable = CompositeDisposable()

    private var messageObserver: Single<List<Message>>? = null
    private val reactionsObserver: Single<List<Reaction>> = getReactionListUseCase()

    private var _chatScreenStateLD = MutableLiveData<ChatScreenState>()
    val chatScreenStateLD: LiveData<ChatScreenState>
        get() = _chatScreenStateLD

    private var _reactionsListStateLD = MutableLiveData<ReactionsListState>()
    val reactionsListStateLD: LiveData<ReactionsListState>
        get() = _reactionsListStateLD


    init {
        messageObserver = getMessageListUseCase(
            numBefore = numBefore,
            numAfter = numAfter,
            channelName = channel.name,
            topicName = topic.title
        )
        subscribeToGiveReactionList()
        subscribeToGiveMessages()
    }

    private fun subscribeToGiveReactionList() {
        reactionsObserver
            .subscribeOn(Schedulers.io())
            .doOnSuccess { _reactionsListStateLD.postValue(ReactionsListState.Loading) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { _reactionsListStateLD.value = ReactionsListState.Result(it) },
                onError = { _reactionsListStateLD.value = ReactionsListState.Error(it) }
            ).addTo(compositeDisposable)
    }

    private fun subscribeToGiveMessages() {

        Log.d("ChatViewModel", "subscribeToGiveMessages()")
        messageObserver!!
            .subscribeOn(Schedulers.io())
            .doOnSuccess { _chatScreenStateLD.postValue(ChatScreenState.Loading) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { _chatScreenStateLD.value = ChatScreenState.Result(it) },
                onError = { _chatScreenStateLD.value = ChatScreenState.Error(it) },
            ).addTo(compositeDisposable)
    }
//
//    fun sendMessage(image: Int, name: String, text: String) {
//        addMessageUseCase()
//    }
//
//    fun addEmoji(message: Message, emoji: String) {
//        addReactionUseCase()
//    }
//
//    fun deleteEmoji(){
//        deleteReactionUseCase()
//    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

}

package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cyberfox21.tinkoffmessanger.data.repository.MessageRepositoryImpl
import com.cyberfox21.tinkoffmessanger.data.repository.ReactionRepositoryImpl
import com.cyberfox21.tinkoffmessanger.data.repository.UsersRepositoryImpl
import com.cyberfox21.tinkoffmessanger.domain.entity.*
import com.cyberfox21.tinkoffmessanger.domain.usecase.*
import com.cyberfox21.tinkoffmessanger.util.toDelegateChatItemsList
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ChatViewModel(private val channel: Channel, private val topic: Topic) : ViewModel() {

    private val messageRepository = MessageRepositoryImpl
    private val reactionsRepository = ReactionRepositoryImpl
    private val usersRepository = UsersRepositoryImpl

    private val getMessageListUseCase = GetMessageListUseCase(messageRepository)
    private val addMessageUseCase = AddMessageUseCase(messageRepository)

    private val getReactionListUseCase = GetReactionListUseCase(reactionsRepository)
    private val addReactionUseCase = AddReactionUseCase(reactionsRepository)
    private val deleteReactionUseCase = DeleteReactionUseCase(reactionsRepository)

    private val getCurrentUserUseCase = GetCurrentUserUseCase(usersRepository)

    private var numBefore: Int = 100
    private var numAfter: Int = 0

    private val compositeDisposable = CompositeDisposable()

    private var messageObserver: Single<List<Message>>? = null
    private val reactionsObserver: Single<List<Reaction>> = getReactionListUseCase()
    private val currentUserObserver: Single<User> = getCurrentUserUseCase()

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
        Single.zip(messageObserver, currentUserObserver, BiFunction { msgList, user ->
            return@BiFunction msgList to user
        }).subscribeOn(Schedulers.io())
            .doOnSuccess { _chatScreenStateLD.postValue(ChatScreenState.Loading) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    val msgList = it.first
                    val user = it.second
                    val delegateItemList = msgList.toDelegateChatItemsList(user.id)
                    _chatScreenStateLD.value = ChatScreenState.Result(delegateItemList)
                },
                onError = { _chatScreenStateLD.value = ChatScreenState.Error(it) },
            ).addTo(compositeDisposable)
    }

    fun sendMessage(text: Editable) {
        addMessageUseCase(channelName = channel.name, topicName = topic.title, text.toString())
            .subscribeBy(
                onComplete = { Log.d("ChatViewModel", "Sending message successfully") },
                onError = { Log.d("ChatViewModel", "Sending message with error ${it.message}") }
            ).addTo(compositeDisposable)
    }

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

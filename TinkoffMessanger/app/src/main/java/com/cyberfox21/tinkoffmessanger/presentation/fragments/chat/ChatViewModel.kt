package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat

import android.app.Application
import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cyberfox21.tinkoffmessanger.data.repository.MessageRepositoryImpl
import com.cyberfox21.tinkoffmessanger.data.repository.ReactionRepositoryImpl
import com.cyberfox21.tinkoffmessanger.data.repository.UsersRepositoryImpl
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.domain.usecase.*
import com.cyberfox21.tinkoffmessanger.presentation.toDelegateChatItemsList
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class ChatViewModel(
    application: Application,
    private val channelName: String,
    private val topicName: String
) : ViewModel() {

    private val messageRepository = MessageRepositoryImpl(application)
    private val reactionsRepository = ReactionRepositoryImpl(application)
    private val usersRepository = UsersRepositoryImpl(application)

    private val getMessageListUseCase = GetMessageListUseCase(messageRepository)
    private val addMessageUseCase = AddMessageUseCase(messageRepository)

    private val getReactionListUseCase = GetReactionListUseCase(reactionsRepository)
    private val addReactionUseCase = AddReactionUseCase(reactionsRepository)
    private val deleteReactionUseCase = DeleteReactionUseCase(reactionsRepository)

    private val getMyUserUseCase = GetMyUserUseCase(usersRepository)

    private var numBefore: Int = 100
    private var numAfter: Int = 0

    private val compositeDisposable = CompositeDisposable()

    private var selectedChannelName: String? = null

    private val messageBehaviorSubject: BehaviorSubject<String> = BehaviorSubject.create()

    private var messageObserver: Observable<List<Message>> = getMessageListUseCase(
        numBefore = numBefore,
        numAfter = numAfter,
        channelName = channelName,
        topicName = topicName
    )
    private val reactionsObserver: Observable<List<Reaction>> = getReactionListUseCase()

    private var _chatScreenStateLD = MutableLiveData<ChatScreenState>()
    val chatScreenStateLD: LiveData<ChatScreenState>
        get() = _chatScreenStateLD

    private var _reactionsListStateLD = MutableLiveData<ReactionsListState>()
    val reactionsListStateLD: LiveData<ReactionsListState>
        get() = _reactionsListStateLD


    init {
        subscribeToGiveMessages()
        subscribeToGiveReactionList()
    }

    private fun subscribeToGiveReactionList() {
        reactionsObserver
            .subscribeOn(Schedulers.io())
            .doOnNext { _reactionsListStateLD.postValue(ReactionsListState.Loading) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { _reactionsListStateLD.value = ReactionsListState.Result(it) },
                onError = { _reactionsListStateLD.value = ReactionsListState.Error(it) }
            ).addTo(compositeDisposable)
    }

    private fun subscribeToGiveMessages() {
        getMyUserUseCase()
            .doOnNext { _chatScreenStateLD.postValue(ChatScreenState.Loading) }
            .subscribeBy(
                onNext = { user ->
                    messageObserver
                        .subscribeOn(Schedulers.io())
                        .doOnNext { _chatScreenStateLD.postValue(ChatScreenState.Loading) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                            onNext = {
                                _chatScreenStateLD.value =
                                    ChatScreenState.Result(it.toDelegateChatItemsList(user.id))
                            },
                            onError = { _chatScreenStateLD.value = ChatScreenState.Error(it) },
                        ).addTo(compositeDisposable)
                },
                onError = {
                    _chatScreenStateLD.value = ChatScreenState.Error(it)
                }
            ).addTo(compositeDisposable)

//
////        Observable.concat(getMyUserUseCase().toObservable(), messageObserver?.toObservable())
//        messageObserver
//            .subscribeOn(Schedulers.io())
//            .doOnSuccess { _chatScreenStateLD.postValue(ChatScreenState.Loading) }
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeBy(
//                onSuccess = {
////                    var user: User? = null
////                    when (it) {
////                        is User -> {
////                            user = (it as User)
////                        }
////                        is List<*> -> {
////                            user?.let { user ->
//////                                val delegateItemList =
////                                    (it as List<Message>).toDelegateChatItemsList(user.id)
//
////                    val delegateItemList =
////                                    (it as List<Message>).toDelegateChatItemsList(user.id)
//
//                    _chatScreenStateLD.value = ChatScreenState.Result(it.toDelegateChatItemsList())
////                            }
////
////                        }
////                    }
//                },
//                onError = { _chatScreenStateLD.value = ChatScreenState.Error(it) },
//            ).addTo(compositeDisposable)
    }

    fun sendMessage(text: Editable) {
        addMessageUseCase(channelName = channelName, topicName = topicName, text.toString())
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

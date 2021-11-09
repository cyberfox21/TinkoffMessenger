package com.cyberfox21.tinkoffmessanger.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.repository.MessageRepository

object MessageRepositoryImpl : MessageRepository {

    private var messageListLD = MutableLiveData<List<Message>>()

    private val messageList =
        sortedSetOf(comparator = Comparator<Message> { o1, o2 -> (o1.id).compareTo(o2.id) })

    override fun getMessageList(): LiveData<List<Message>> {
        return messageListLD
    }

    override fun getMessage(msgId: Int): Message? {
        return messageList.find { msgId == it.id }
    }

    override fun addMessage(msg: Message) {
        messageList.add(msg)
        updateList()
    }

    override fun addEmojiToMessage(msg: Message) {
        val oldItem = getMessage(msg.id)

        oldItem?.let {
            deleteMessage(oldItem)
            addMessage(msg)
        }
    }

    override fun deleteMessage(msg: Message) {
        messageList.remove(msg)
        updateList()
    }

    private fun updateList() {
        messageListLD.value = messageList.toList()
    }

}

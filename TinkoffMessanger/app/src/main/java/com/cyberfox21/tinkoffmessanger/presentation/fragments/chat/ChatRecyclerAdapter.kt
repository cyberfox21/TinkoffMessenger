package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.CustomEmojiView
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.EmojiMessageViewGroup

class ChatRecyclerAdapter :
    ListAdapter<Message, MessageViewHolder>(MessageDiffUtilCallback()) {

    var onLongMessageClickListener: OnLongMessageClickListener? = null

    interface OnLongMessageClickListener {
        fun onLongMessageClick(message: Message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        Log.d("ChatRecyclerAdapter", "OnCreateViewHolder")
        return MessageViewHolder(EmojiMessageViewGroup(parent.context))
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        Log.d("ChatRecyclerAdapter", "OnBindViewHolder")
        bindMessage(holder, position)
    }

    private fun bindMessage(holder: MessageViewHolder, position: Int) {
        val message = currentList[position]
        with(holder) {
            imageView.setImageResource(message.image)
            name.text = message.name
            text.text = message.msg
            time.text = message.time

            val btnAdd = emojiLayout.getChildAt(0)
            emojiLayout.removeView(btnAdd)
            emojiLayout.addView(btnAdd)

            for (i in 0 until message.reactions.size) {
                val emojiView = CustomEmojiView(emojiLayout.context).apply {
                    onEmojiClickListener = object : CustomEmojiView.OnEmojiClickListener {
                        override fun onEmojiClick(view: CustomEmojiView) {
                            view.isSelected = !view.isSelected
                        }
                    }
                }
                emojiView.apply {
                    emoji = message.reactions[i].reaction
                    count = message.reactions[i].count.toString()

                }
                Log.d("ChatRecyclerAdapter", "childcount ${emojiLayout.childCount}")
                emojiLayout.addView(emojiView)
            }

            emojiMessageViewGroup.setOnLongClickListener {
                onLongMessageClickListener?.onLongMessageClick(message)
                true
            }
        }
    }



}
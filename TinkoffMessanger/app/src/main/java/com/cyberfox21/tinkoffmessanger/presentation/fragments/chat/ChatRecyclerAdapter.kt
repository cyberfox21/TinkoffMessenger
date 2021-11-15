package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.AlienMessageViewHolder
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.CustomEmojiView
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.EmojiMessageViewGroup
import com.cyberfox21.tinkoffmessanger.util.EmojiFormatter

class ChatRecyclerAdapter :
    ListAdapter<Message, AlienMessageViewHolder>(MessageDiffUtilCallback()) {

    var onLongMessageClickListener: OnLongMessageClickListener? = null

    interface OnLongMessageClickListener {
        fun onLongMessageClick(message: Message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlienMessageViewHolder {
        Log.d("ChatRecyclerAdapter", "OnCreateViewHolder")
        return AlienMessageViewHolder(EmojiMessageViewGroup(parent.context))
    }

    override fun onBindViewHolder(holder: AlienMessageViewHolder, position: Int) {
        Log.d("ChatRecyclerAdapter", "OnBindViewHolder")
        bindMessage(holder, position)
    }

    private fun bindMessage(holder: AlienMessageViewHolder, position: Int) {
        val message = currentList[position]
        with(holder) {
            Glide.with(emojiMessageViewGroup.context).load(message.senderAvatarUrl).into(imageView)
            name.text = message.senderName
            text.text = message.message
            time.text = message.time.toString()

            val btnAdd = emojiLayout.getChildAt(0)
            emojiLayout.removeView(btnAdd)
            emojiLayout.addView(btnAdd)

            for (i in message.reactions.indices) {
                val emojiView = CustomEmojiView(emojiLayout.context).apply {
                    onEmojiClickListener = object : CustomEmojiView.OnEmojiClickListener {
                        override fun onEmojiClick(view: CustomEmojiView) {
                            view.isSelected = !view.isSelected
                        }
                    }
                }
                emojiView.apply {
                    emoji = EmojiFormatter.stringToEmoji(message.reactions[i].reaction)
                    count = ""

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

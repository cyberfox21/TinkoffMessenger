package com.cyberfox21.tinkoffmessanger.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.cyberfox21.tinkoffmessanger.domain.entity.Message

class ChatRecyclerAdapter : ListAdapter<Message, MessageViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(EmojiMessageViewGroup(parent.context))
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = currentList[position]
        with(holder) {
            val root = holder.emojiMessageViewGroup
            root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            imageView.setImageResource(message.image)
            name.text = message.name
            text.text = message.msg
            time.text = message.time

            for (reaction in message.reactions) {
                val emojiView = CustomEmojiView(emojiLayout.context).apply {
                    emoji = reaction.reaction
                    count = reaction.count.toString()
                }
                emojiLayout.addView(emojiView)
            }
        }
    }

}
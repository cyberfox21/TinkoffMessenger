package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cyberfox21.tinkoffmessanger.presentation.AdapterDelegate
import com.cyberfox21.tinkoffmessanger.presentation.DelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.CustomEmojiView
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.EmojiMessageViewGroup
import com.cyberfox21.tinkoffmessanger.util.EmojiFormatter

class AlienMessageDelegateAdapter : AdapterDelegate {

    var onLongMessageClickListener: OnLongMessageClickListener? = null

    interface OnLongMessageClickListener {
        fun onLongMessageClick(message: AlienMessageDelegateItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return AlienMessageViewHolder(EmojiMessageViewGroup(parent.context))
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        bindMessage(holder, item)
    }

    private fun bindMessage(holder: RecyclerView.ViewHolder, item: DelegateItem) {
        val message = item as AlienMessageDelegateItem
        with(holder as AlienMessageViewHolder) {
            Glide.with(emojiMessageViewGroup.context).load(message.senderAvatarUrl).into(imageView)
            name.text = message.senderName
            text.text = message.text
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

    override fun isOfViewType(item: DelegateItem): Boolean = item is AlienMessageDelegateItem
}
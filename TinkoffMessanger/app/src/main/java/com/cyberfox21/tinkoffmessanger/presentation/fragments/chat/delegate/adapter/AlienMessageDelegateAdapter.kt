package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.AdapterDelegate
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.adapter.OnLongMessageClickListener
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.AlienMessageDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.viewholder.AlienMessageViewHolder
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.CustomEmojiView
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.EmojiMessageViewGroup
import com.cyberfox21.tinkoffmessanger.presentation.util.EmojiFormatter

class AlienMessageDelegateAdapter(private val onLongMessageClickListener: OnLongMessageClickListener) :
    AdapterDelegate {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = EmojiMessageViewGroup(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        return AlienMessageViewHolder(view)
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
            time.text = message.time

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
                EmojiFormatter.stringToEmoji(message.reactions[i].reaction)?.let{
                    emojiView.apply {
                        emoji = it
                        count = ""
                    }
                }

                Log.d("ChatRecyclerAdapter", "childcount ${emojiLayout.childCount}")
                emojiLayout.addView(emojiView)
            }

            emojiMessageViewGroup.setOnLongClickListener {
                onLongMessageClickListener.onLongMessageClick(message)
                true
            }
        }
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is AlienMessageDelegateItem
}

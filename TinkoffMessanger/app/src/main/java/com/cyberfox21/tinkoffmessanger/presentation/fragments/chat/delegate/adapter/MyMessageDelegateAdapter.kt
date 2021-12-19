package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.data.mapToReaction
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.AdapterDelegate
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.ClickEmojiMode
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.MyMessageDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.viewholder.MyMessageViewHolder
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.CustomEmojiView
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.MyMessageViewGroup

class MyMessageDelegateAdapter(
    private val onReactionClickListener: OnReactionClickListener,
    private val onLongMessageClickListener: OnLongMessageClickListener
) : AdapterDelegate {

    private val lp = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
    )

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return MyMessageViewHolder(MyMessageViewGroup(parent.context)
            .apply { layoutParams = lp }
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        bindMessage(holder, item)
    }

    private fun bindMessage(holder: RecyclerView.ViewHolder, item: DelegateItem) {
        val message = (item as MyMessageDelegateItem)
        with(holder as MyMessageViewHolder) {

            this.myMessage.time = message.time
            this.myMessage.message = message.text

            emojiLayout.apply { removeAllViews() }

            if (message.reactions.isNotEmpty()) {
                val btnAdd = LayoutInflater.from(myMessageViewGroup.context)
                    .inflate(R.layout.btn_add_view, myMessageViewGroup, false)
                btnAdd.setOnClickListener { onLongMessageClickListener.onLongMessageClick(message.id) }
                emojiLayout.addView(btnAdd)
            }

            message.reactions.forEach { listEmoji ->
                val emojiView = (LayoutInflater.from(myMessageViewGroup.context)
                    .inflate(R.layout.custom_emoji_view, myMessageViewGroup, false)
                        as CustomEmojiView)
                emojiView.apply {
                    emoji = listEmoji.reaction
                    count = listEmoji.count
                    isSelected = listEmoji.isSelected
                    setOnClickListener { reactionView ->
                        if (reactionView.isSelected.not()) {
                            onReactionClickListener.onReactionClick(
                                ClickEmojiMode.ADD_EMOJI, message.id, listEmoji.mapToReaction()
                            )
                        } else {
                            onReactionClickListener.onReactionClick(
                                ClickEmojiMode.DELETE_EMOJI,
                                message.id,
                                listEmoji.mapToReaction()
                            )
                        }
                    }
                }
                emojiLayout.addView(emojiView, emojiLayout.size - 1)
            }

            myMessageViewGroup.setOnLongClickListener {
                onLongMessageClickListener.onLongMessageClick(message.id)
                true
            }
        }
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is MyMessageDelegateItem
}

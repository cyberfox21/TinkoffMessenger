package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.data.mapToReaction
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.AdapterDelegate
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.AlienMessageDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.viewholder.AlienMessageViewHolder
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.enums.BottomDialogMode
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.enums.ClickEmojiMode
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.AlienMessageViewGroup
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.CustomEmojiView

class AlienMessageDelegateAdapter(
    private val onReactionClickListener: OnReactionClickListener,
    private val onLongMessageClickListener: OnLongMessageClickListener
) : AdapterDelegate {

    private val lp = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
    )

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = AlienMessageViewGroup(parent.context)
            .apply { layoutParams = lp }
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

            Glide.with(alienMessageViewGroup.context).load(message.senderAvatarUrl).into(imageView)
            alienMessage.name = message.senderName
            alienMessage.message = message.text
            alienMessage.time = message.time

            emojiLayout.apply { removeAllViews() }

            if (message.reactions.isNotEmpty()) {
                val btnAdd = LayoutInflater.from(alienMessageViewGroup.context)
                    .inflate(R.layout.btn_add_view, alienMessageViewGroup, false)
                btnAdd.setOnClickListener {
                    onLongMessageClickListener.onLongMessageClick(
                        message,
                        BottomDialogMode.REACTION_LIST
                    )
                }
                emojiLayout.addView(btnAdd)
            }

            message.reactions.forEach { listEmoji ->
                val emojiView = (LayoutInflater.from(alienMessageViewGroup.context)
                    .inflate(R.layout.custom_emoji_view, alienMessageViewGroup, false)
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
                                ClickEmojiMode.DELETE_EMOJI, message.id, listEmoji.mapToReaction()
                            )
                        }
                    }
                }
                emojiLayout.addView(emojiView, emojiLayout.size - 1)
            }

            alienMessageViewGroup.setOnLongClickListener {
                onLongMessageClickListener.onLongMessageClick(message, BottomDialogMode.OPTIONS)
                true
            }
        }
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is AlienMessageDelegateItem
}

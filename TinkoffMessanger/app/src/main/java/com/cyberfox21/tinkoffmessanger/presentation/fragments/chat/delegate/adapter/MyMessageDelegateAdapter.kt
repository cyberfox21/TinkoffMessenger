package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.core.text.toSpannable
import androidx.recyclerview.widget.RecyclerView
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.AdapterDelegate
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.adapter.OnLongMessageClickListener
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.MyMessageDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.viewholder.MyMessageViewHolder
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.CustomEmojiView
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.MyMessageViewGroup
import com.cyberfox21.tinkoffmessanger.presentation.util.EmojiFormatter

class MyMessageDelegateAdapter(private val onLongMessageClickListener: OnLongMessageClickListener) :
    AdapterDelegate {

    private val lp = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
    )

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return MyMessageViewHolder(MyMessageViewGroup(parent.context).apply { layoutParams = lp })
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
            this.myMessage.message = message.text.toSpannable()

            val btnAdd = this.emojiLayout.getChildAt(0)
            this.emojiLayout.removeView(btnAdd)
            this.emojiLayout.addView(btnAdd)

            for (i in message.reactions.indices) {
                val emojiView = CustomEmojiView(this.emojiLayout.context).apply {
                    onEmojiClickListener = object : CustomEmojiView.OnEmojiClickListener {
                        override fun onEmojiClick(view: CustomEmojiView) {
                            view.isSelected = !view.isSelected
                        }
                    }
                }
                EmojiFormatter.stringToEmoji(message.reactions[i].reaction)?.let {
                    emojiView.apply {
                        emoji = it
                        count = ""
                    }
                }
                Log.d("ChatRecyclerAdapter", "childcount ${this.emojiLayout.childCount}")
                this.emojiLayout.addView(emojiView)
            }

            myMessageViewGroup.setOnLongClickListener {
                onLongMessageClickListener.onLongMessageClick(message)
                true
            }
        }
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is MyMessageDelegateItem
}

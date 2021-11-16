package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.AdapterDelegate
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.MyMessageDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.viewholder.MyMessageViewHolder
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.CustomEmojiView
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.views.MyMessageViewGroup
import com.cyberfox21.tinkoffmessanger.util.EmojiFormatter

class MyMessageDelegateAdapter : AdapterDelegate {

    var onLongMessageClickListener: OnLongMessageClickListener? = null

    interface OnLongMessageClickListener {
        fun onLongMessageClick(message: MyMessageDelegateItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
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
                emojiView.apply {
                    emoji = EmojiFormatter.stringToEmoji(message.reactions[i].reaction)
                    count = ""

                }
                Log.d("ChatRecyclerAdapter", "childcount ${emojiLayout.childCount}")
                emojiLayout.addView(emojiView)
            }

            myMessageViewGroup.setOnLongClickListener {
                onLongMessageClickListener?.onLongMessageClick(message)
                true
            }
        }
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is MyMessageDelegateItem
}
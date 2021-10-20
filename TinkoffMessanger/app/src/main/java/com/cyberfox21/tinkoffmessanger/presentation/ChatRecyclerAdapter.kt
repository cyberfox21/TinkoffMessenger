package com.cyberfox21.tinkoffmessanger.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cyberfox21.tinkoffmessanger.databinding.DateItemDecorationBinding
import com.cyberfox21.tinkoffmessanger.domain.entity.Message

class ChatRecyclerAdapter :
    ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffUtilCallback()) {

    var onLongMessageClickListener: OnLongMessageClickListener? = null

    enum class ViewType(type: Int) {
        MESSAGE(0),
        DATE_DEVIDER(1)
    }

    interface OnLongMessageClickListener {
        fun onLongMessageClick()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder = when (viewType) {
            ViewType.MESSAGE.ordinal -> MessageViewHolder(EmojiMessageViewGroup(parent.context))
            ViewType.DATE_DEVIDER.ordinal -> DateViewHolder(
                DateItemDecorationBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> throw RuntimeException("Unknown view type $viewType")
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MessageViewHolder -> {
                bindMessage(holder, position)
            }
//            //is DateViewHolder -> {
//                bindDate(holder, position)
//            }
        }
    }

//    override fun getItemViewType(position: Int): Int {
//        if (position == 0) return ViewType.DATE_DEVIDER.ordinal
//        if (currentList[position].time != currentList[position - 1].time)
//            return ViewType.DATE_DEVIDER.ordinal
//        return ViewType.MESSAGE.ordinal
//    }

    private fun bindMessage(holder: MessageViewHolder, position: Int) {
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
            root.setOnLongClickListener {
                onLongMessageClickListener?.onLongMessageClick()
                true
            }
        }
    }

    private fun bindDate(holder: DateViewHolder, position: Int) {
        holder.binding.textDate.text = currentList[position].time
    }

    fun getTime(position: Int): String {
        return currentList[position].time
    }

}

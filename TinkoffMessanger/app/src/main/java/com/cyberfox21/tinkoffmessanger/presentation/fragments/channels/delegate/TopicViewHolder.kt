package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberfox21.tinkoffmessanger.databinding.ItemTopicBinding
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic

class TopicViewHolder(
    val binding: ItemTopicBinding,
    private val onTopicClick: TopicDelegateAdapter.OnTopicDelegateClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(topic: Topic) {
        this.binding.root.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        with(binding) {
            tvTopicTitle.text = topic.title
            tvTopicMsgCount.text = topic.messagesCount.toString()
            root.setOnClickListener {
                onTopicClick.onTopicClick(topic, adapterPosition)
            }
        }
    }
}

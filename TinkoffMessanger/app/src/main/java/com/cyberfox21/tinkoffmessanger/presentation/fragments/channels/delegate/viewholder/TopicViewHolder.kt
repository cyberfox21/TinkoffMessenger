package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberfox21.tinkoffmessanger.databinding.ItemTopicBinding
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.adapter.TopicDelegateAdapter
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.item.TopicDelegateItem

class TopicViewHolder(
    val binding: ItemTopicBinding,
    private val onTopicClick: TopicDelegateAdapter.OnTopicDelegateClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(topic: TopicDelegateItem) {
        this.binding.root.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        with(binding) {
            tvTopicTitle.text = topic.name
            tvTopicMsgCount.text = topic.msgCount.toString()
            root.setOnClickListener {
                onTopicClick.onTopicClick(topic.name)
            }
        }
    }
}

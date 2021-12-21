package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.databinding.ItemTopicBinding
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.adapter.TopicDelegateAdapter
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.item.TopicDelegateItem

class TopicViewHolder(
    val binding: ItemTopicBinding,
    private val onTopicClick: TopicDelegateAdapter.OnTopicDelegateClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(topic: TopicDelegateItem) {
        this.binding.root.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        this.binding.root.setBackgroundResource(getBackgroundColor(adapterPosition))
        with(binding) {
            tvTopicTitle.text = topic.name
            tvTopicMsgCount.text = topic.msgCount.toString()
            root.setOnClickListener {
                onTopicClick.onTopicClick(topic.name)
            }
        }
    }

    private fun getBackgroundColor(index: Int) = when (index % 2) {
        0 -> R.color.yellow
        1 -> R.color.green
        else -> R.color.green
    }

}

package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberfox21.tinkoffmessanger.databinding.ItemTopicBinding
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import com.cyberfox21.tinkoffmessanger.presentation.AdapterDelegate
import com.cyberfox21.tinkoffmessanger.presentation.DelegateItem

class TopicDelegateAdapter(private val onTopicClick: OnTopicDelegateClickListener) :
    AdapterDelegate {

    interface OnTopicDelegateClickListener {
        fun onTopicClick(item: Topic, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = ItemTopicBinding.inflate(LayoutInflater.from(parent.context))
        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return TopicViewHolder(
            binding.apply {
                root.layoutParams = lp
            },
            onTopicClick
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int

    ) {
        (holder as TopicViewHolder).bind(item.content() as Topic)
    }

    override fun isOfViewType(item: DelegateItem): Boolean {
        return item is TopicDelegateItem
    }
}

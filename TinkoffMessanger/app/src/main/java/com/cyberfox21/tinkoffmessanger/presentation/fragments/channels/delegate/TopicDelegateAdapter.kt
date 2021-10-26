package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberfox21.tinkoffmessanger.databinding.ItemTopicBinding
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic

class TopicDelegateAdapter(private val onTopicClick: OnTopicDelegateClickListener) :
    AdapterDelegate {

    interface OnTopicDelegateClickListener {
        fun onTopicClick(item: Topic, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return TopicViewHolder(
            ItemTopicBinding.inflate(LayoutInflater.from(parent.context)),
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

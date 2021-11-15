package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberfox21.tinkoffmessanger.databinding.ItemChannelBinding
import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.presentation.AdapterDelegate
import com.cyberfox21.tinkoffmessanger.presentation.DelegateItem

class ChannelDelegateAdapter(private val onChannelClick: OnChannelDelegateClickListener) :
    AdapterDelegate {

    interface OnChannelDelegateClickListener {
        fun onChannelClick(item: Channel, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = ItemChannelBinding.inflate(LayoutInflater.from(parent.context))
        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return ChannelViewHolder(
            binding.apply {
                root.layoutParams = lp
            },
            onChannelClick
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as ChannelViewHolder).bind(item.content() as Channel)
    }

    override fun isOfViewType(item: DelegateItem): Boolean {
        return item is ChannelDelegateItem
    }
}

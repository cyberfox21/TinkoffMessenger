package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberfox21.tinkoffmessanger.databinding.ItemChannelBinding
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.AdapterDelegate
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.item.ChannelDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.viewholder.ChannelViewHolder

class ChannelDelegateAdapter(private val onChannelClick: OnChannelDelegateClickListener) :
    AdapterDelegate {

    interface OnChannelDelegateClickListener {
        fun onChannelClick(channelId: Int, channelName: String, isSelected: Boolean)
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
        (holder as ChannelViewHolder).bind(item as ChannelDelegateItem)
    }

    override fun isOfViewType(item: DelegateItem): Boolean {
        return item is ChannelDelegateItem
    }
}

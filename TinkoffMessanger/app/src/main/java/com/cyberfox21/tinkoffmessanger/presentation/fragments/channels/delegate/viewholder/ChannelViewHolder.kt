package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.databinding.ItemChannelBinding
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.adapter.ChannelDelegateAdapter
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.item.ChannelDelegateItem

class ChannelViewHolder(
    val binding: ItemChannelBinding,
    private val onChannelClick: ChannelDelegateAdapter.OnChannelDelegateClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ChannelDelegateItem) {
        binding.tvChannelTitle.text = item.name
        binding.root.setOnClickListener {
            binding.ivExpandChannel.setImageResource(getImageResource(item.isSelected))
            onChannelClick.onChannelClick(item.id, item.name, item.isSelected)
        }
    }

    private fun getImageResource(selected: Boolean): Int {
        return when (selected) {
            true -> R.drawable.ic_selected_channel
            false -> R.drawable.ic_unselected_channel
        }
    }

}
